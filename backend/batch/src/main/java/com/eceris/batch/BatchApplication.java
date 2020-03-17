package com.eceris.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableBatchProcessing
@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class BatchApplication implements ApplicationRunner {

    private final PersonRepository repository;
    private final PersonService service;

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("===============run===============");
        repository.save(new Person(1l, "jion", 1));
        repository.save(new Person(2l, "dohee", 1));
        repository.save(new Person(3l, "sunghyun", 1));

        long l = repository.countByAge(1);
        log.info("count : " + l);
    }

    @Bean
    public RouterFunction<ServerResponse> controller() {


        return route()
                .path("/api", builder -> builder
                        .GET("/persons", b -> service.persons())
                )
                .build();
    }
}
