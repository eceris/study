package com.prodo.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiHandler {

    private final EventPublisher publisher;

    public Mono<ServerResponse> get(ServerRequest request) {
        log.info("api request thread : {}", Thread.currentThread().getName());
        log.info("api request time : {}", System.currentTimeMillis());
        final String name = "prodo";
        publisher.get(name);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(name), String.class);
    }

    public Mono<ServerResponse> publish(ServerRequest request) {
        final String name = "hi";
        publisher.post(name);

        String apiId = "aaa";

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(name), String.class)
                .doOnEach(logOnNext(r -> System.out.println(r)))
                .subscriberContext(Context.of("apiID", apiId));
    }


    private static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) return;
            Optional<String> apiIDMaybe = signal.getContext().getOrEmpty("apiID");

            apiIDMaybe.ifPresentOrElse(apiID -> {
                try (MDC.MDCCloseable closeable = MDC.putCloseable("apiID", apiID)) {
                    logStatement.accept(signal.get());
                }
            }, () -> logStatement.accept(signal.get()));
        };
    }
}
