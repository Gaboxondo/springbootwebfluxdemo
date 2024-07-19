package com.mashosoft.flightsService.interfaces.web.aspect;

import com.mashosoft.flightsService.config.exceptionHandling.model.exception.ControlledErrorException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.UUID;
import java.util.function.Consumer;

@Aspect
@Component
public class ReactiveRequestLoggingAspect {

    private static final String TRACE_ID = "traceId";

    private static final String OK = "[OK]";
    private static final String ERROR = "[ERROR]";
    private static final String EMPTY_MONO = "[EMPTY-MONO]";
    private static final String EMPTY_FLUX = "[EMPTY-FLUX]";
    private static final String COMPLETE_FLUX = "[COMPLETE-FLUX]";
    private static final String CANCEL = "[CANCEL]";
    private static final String CONTROLLED_ERROR = "[CONTROLLED-ERROR]";
    private static final String BUG = "[BUG]";

    //FIXME: THIS IS WHAT I WILL HAVE TO SOLVE WITH MICROMETER, ALSO GET NOT PRINTED IN HANDLER
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    WebFilter slf4jMdcFilter() {
        return (exchange, chain) -> {
            String requestId = UUID.randomUUID().toString();
            MDC.MDCCloseable trx = MDC.putCloseable(TRACE_ID, requestId);
            return chain.filter(exchange)
                .contextWrite( Context.of(TRACE_ID, requestId));
        };
    }

    @Around("@annotation(io.swagger.v3.oas.annotations.Operation)")
    public Object logInOut(ProceedingJoinPoint joinPoint) throws Throwable {

        Class<?> clazz = joinPoint.getTarget().getClass();
        Logger logger = LoggerFactory.getLogger(clazz);
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();

        logger.info("controller-direction=in method-name={}", methodName);

        Object result;
        Throwable exception = null;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            result = joinPoint.proceed();
            stopWatch.stop();
            Long duration = stopWatch.getTotalTimeMillis();
            if (result instanceof Mono<?> monoOut) {
                return logMonoResult(joinPoint, clazz, logger, monoOut,duration, methodName );
            } else if (result instanceof Flux<?> fluxOut) {
                return logFluxResult(joinPoint, clazz, logger, fluxOut,duration, methodName );
            } else {
                return result;
            }
        } catch (Exception e) {
            doOutputLogging( logger,null, ERROR, e,null,methodName);
            throw e;
        }
    }

    private <T, L> Mono<T> logMonoResult(ProceedingJoinPoint joinPoint, Class<L> clazz, Logger logger, Mono<T> monoOut,Long duration,String methodName) {
        return Mono.deferContextual(contextView ->
            monoOut.switchIfEmpty(Mono.<T>empty()
                    .doOnSuccess(logOnEmptyConsumer(contextView, () -> doOutputLogging( logger, null,EMPTY_MONO, null,duration,methodName))))
                    .doOnEach(logOnNext(responseObject -> doOutputLogging( logger, responseObject,OK, null,duration,methodName)))
                    .doOnEach(logOnError(exception -> doOutputLogging( logger, null,null, exception,null,methodName)))
                    .doOnCancel(logOnEmptyRunnable(contextView, () -> doOutputLogging( logger, null,CANCEL, null,null,methodName)))
        );
    }

    private <T> Flux<T> logFluxResult(ProceedingJoinPoint joinPoint, Class<?> clazz, Logger logger, Flux<T> fluxOut,Long duration,String methodName) {
        return Flux.deferContextual(contextView ->
            fluxOut
                .switchIfEmpty(Flux.<T>empty()
                    .doOnEach(logOnNext(data -> doOutputLoggingDebugForFluxData( logger, data, duration, methodName)))
                    .doOnEach(logOnError(exception -> doOutputLogging( logger,null, null, exception,null,methodName)))
                    .doOnComplete(logOnEmptyRunnable(contextView, () -> doOutputLogging( logger,null, EMPTY_FLUX, null, duration,methodName))))
                    .doOnComplete(logOnEmptyRunnable(contextView, () -> doOutputLogging( logger,null, COMPLETE_FLUX, null,duration,methodName)))
                    .doOnCancel(logOnEmptyRunnable(contextView, () -> doOutputLogging(logger,null, CANCEL, null,null,methodName)))
        );
    }

    private <T> void doOutputLogging( final Logger logger, final T responsePart,String result, final Throwable exception, Long duration,String methodName) {
        if(exception == null) {
            if(duration == null){
                duration = 0L;
            }

            logger.info( "Controller-direction=out method-name={} result={} duration={}ms", methodName, result, duration );
        }
        else {
            if(exception instanceof ControlledErrorException){
                ControlledErrorException controlledErrorException = (ControlledErrorException) exception;
                String finalResult = CONTROLLED_ERROR;
                logger.info( "Controller-direction=out method-name={} result={} errorCode={} errorMessage={}", methodName, finalResult, controlledErrorException.getErrorCode(), controlledErrorException.getErrorMessage() );
            } else {
                String finalResult = BUG;
                logger.info( "Controller-direction=out method-name={} result={} exceptionType={}, errorMessage={}", methodName, finalResult, exception.getClass().toString(), exception.getMessage() );
            }
        }
    }

    private <T> void doOutputLoggingDebugForFluxData( final Logger logger, final T responsePart, Long duration, String methodName) {
        if (responsePart != null ) {
            if (duration == null) {
                duration = 0L;
            }
            logger.debug( "Controller-direction=out-flux-part method-name={} bodyResponsePart={} duration={}ms", methodName,
                responsePart.toString(), duration );
        }
    }
    //-----------------------------------------------------------------------------------------------

    //FIXME: THIS IS WHAT I WILL HAVE TO SOLVE WITH MICROMETER, ALSO GET NOT PRINTED IN HANDLER
    private static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) {
                return;
            }
            String trxIdVar = signal.getContextView().getOrDefault(TRACE_ID, "");
            try (MDC.MDCCloseable trx = MDC.putCloseable(TRACE_ID, trxIdVar)) {
                T t = signal.get();
                logStatement.accept(t);
            }
        };
    }

    private static <T> Consumer<Signal<T>> logOnError(Consumer<Throwable> errorLogStatement) {
        return signal -> {
            if (!signal.isOnError()) return;
            String trxIdVar = signal.getContextView().getOrDefault(TRACE_ID, "");
            try (MDC.MDCCloseable trx = MDC.putCloseable(TRACE_ID, trxIdVar)) {
                errorLogStatement.accept(signal.getThrowable());
            }
        };
    }

    private static <T> Consumer<T> logOnEmptyConsumer(final ContextView contextView, Runnable logStatement) {
        return signal -> {
            if (signal != null) return;
            String trxIdVar = contextView.getOrDefault(TRACE_ID, "");
            try (MDC.MDCCloseable trx = MDC.putCloseable(TRACE_ID, trxIdVar)) {
                logStatement.run();
            }
        };
    }

    private static Runnable logOnEmptyRunnable(final ContextView contextView, Runnable logStatement) {
        return () -> {
            String trxIdVar = contextView.getOrDefault(TRACE_ID, "");
            try (MDC.MDCCloseable trx = MDC.putCloseable(TRACE_ID, trxIdVar)) {
                logStatement.run();
            }
        };
    }
}
