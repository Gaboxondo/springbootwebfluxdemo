package com.mashosoft.flightsService.interfaces.web.aspect;

import com.mashosoft.flightsService.config.exceptionHandling.model.exception.ControlledErrorException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Date;
import java.util.function.Consumer;

@Aspect
@Component
public class ReactiveRequestLoggingAspect {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    WebFilter slf4jMdcFilter() {
        return (exchange, chain) -> chain.filter(exchange).contextWrite(
            Context.of("path", exchange.getRequest().getPath().toString()));
    }

    @Around("@annotation(io.swagger.v3.oas.annotations.Operation)")
    public Object logInOut(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> clazz = joinPoint.getTarget().getClass();
        Logger logger = LoggerFactory.getLogger(clazz);

        logger.info("controller-direction=in method-name={}", clazz.getSimpleName());
        Date start = new Date();
        Object result;
        Throwable exception = null;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            result = joinPoint.proceed();
            stopWatch.stop();
            Long duration = stopWatch.getTotalTimeMillis();
            if (result instanceof Mono<?> monoOut) {
                return logMonoResult(joinPoint, clazz, logger, start, monoOut,duration);
            } else if (result instanceof Flux<?> fluxOut) {
                return logFluxResult(joinPoint, clazz, logger, start, fluxOut,duration);
            } else {
                return result;
            }
        } catch (Exception e) {
            doOutputLogging(joinPoint, clazz, logger, "[error]", e,null);
            throw e;
        }
    }

    private <T, L> Mono<T> logMonoResult(ProceedingJoinPoint joinPoint, Class<L> clazz, Logger logger, Date start, Mono<T> monoOut,Long duration) {
        return Mono.deferContextual(contextView ->
            monoOut.switchIfEmpty(Mono.<T>empty()
                    .doOnSuccess(logOnEmptyConsumer(contextView, () -> doOutputLogging(joinPoint, clazz, logger, "[empty]", null,duration))))
                    .doOnEach(logOnNext(v -> doOutputLogging(joinPoint, clazz, logger, v, null,duration)))
                    .doOnEach(logOnError(e -> doOutputLogging(joinPoint, clazz, logger, null, e,null)))
                    .doOnCancel(logOnEmptyRunnable(contextView, () -> doOutputLogging(joinPoint, clazz, logger, "[cancelled]", null,null)))
        );
    }

    private <T> Flux<T> logFluxResult(ProceedingJoinPoint joinPoint, Class<?> clazz, Logger logger, Date start, Flux<T> fluxOut,Long duration) {
        return Flux.deferContextual(contextView ->
            fluxOut
                .switchIfEmpty(Flux.<T>empty()
                    .doOnComplete(logOnEmptyRunnable(contextView, () -> doOutputLogging(joinPoint, clazz, logger, "[empty]", null, duration))))
                    .doOnEach(logOnNext(v -> doOutputLoggingDebug(joinPoint, clazz, logger, v, null, duration)))
                    .doOnEach(logOnError(e -> doOutputLogging(joinPoint, clazz, logger, null, e,null)))
                    .doOnCancel(logOnEmptyRunnable(contextView, () -> doOutputLogging(joinPoint, clazz, logger, "[cancelled]", null,null)))
                    .doOnComplete( logOnEmptyRunnable(contextView, () -> doOutputLogging(joinPoint, clazz, logger, "[complete]", null,duration)) )
        );
    }

    private static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) {
                return;
            }
            T t = signal.get();
            logStatement.accept(t);
        };
    }

    private static <T> Consumer<Signal<T>> logOnError(Consumer<Throwable> errorLogStatement) {
        return signal -> {errorLogStatement.accept(signal.getThrowable());};
    }

    private static <T> Consumer<T> logOnEmptyConsumer(final ContextView contextView, Runnable logStatement) {
        return signal -> {logStatement.run();};
    }

    private static Runnable logOnEmptyRunnable(final ContextView contextView, Runnable logStatement) {
        return logStatement::run;
    }

    private <T> void doOutputLogging(final ProceedingJoinPoint joinPoint, final Class<?> clazz, final Logger logger, final T responsePart, final Throwable exception, Long duration) {
        if(responsePart != null && exception == null) {
            if(duration == null){
                duration = 0L;
            }
            String finalResult = "[OK]";
            logger.info( "Controller-direction=out method-name={} result={} duration={}ms", clazz.getSimpleName(), finalResult, duration );
        }
        if(responsePart != null && exception != null) {
            if(exception instanceof ControlledErrorException){
                ControlledErrorException controlledErrorException = (ControlledErrorException) exception;
                String finalResult = "[CONTROLLEDERROR]";
                logger.info( "Controller-direction=out method-name={} result={} errorCode={} errorMessage={}", clazz.getSimpleName(), finalResult, controlledErrorException.getErrorCode(), controlledErrorException.getErrorMessage() );
            } else {
                String finalResult = "[BUG]";
                logger.info( "Controller-direction=out method-name={} result={} exceptionType={}, errorMessage={}", clazz.getSimpleName(), finalResult, exception.getClass().toString(), exception.getMessage() );
            }
        }
    }

    private <T> void doOutputLoggingDebug(final ProceedingJoinPoint joinPoint, final Class<?> clazz, final Logger logger, final T responsePart, Long duration) {
        if (responsePart != null ) {
            if (duration == null) {
                duration = 0L;
            }
            String finalResult = "[OK]";
            logger.debug( "Controller-direction=out method-name={} bodyResponsePart={} result={} duration={}ms", clazz.getSimpleName(),
                responsePart.toString(),finalResult, duration );
        }
    }
}
