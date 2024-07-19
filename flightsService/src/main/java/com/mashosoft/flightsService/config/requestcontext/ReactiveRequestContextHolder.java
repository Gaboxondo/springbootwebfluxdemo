package com.mashosoft.flightsService.config.requestcontext;

import org.springframework.http.server.ServerHttpRequest;
import reactor.core.publisher.Mono;

public class ReactiveRequestContextHolder {

    public static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;

    public static Mono<ServerHttpRequest> getRequest(){
        return Mono.deferContextual( Mono::just).map( ctx -> ctx.get( CONTEXT_KEY ) );
    }
}
