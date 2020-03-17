package com.eceris.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonService {


    private final PersonRepository repository;

    public Mono<ServerResponse> persons() {
        PageRequest pageable = PageRequest.of(0, 10);
        Mono<Page<Person>> pageMono = Mono.fromCallable(() -> repository.findAll(pageable));
        System.out.println(pageMono);
        return pageMono.flatMap(r ->  ServerResponse.ok().body(Mono.just(r), Page.class));
//        return ServerResponse.ok().body(Mono.just(pageMono), Page.class);
    }
}
