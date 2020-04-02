package com.eceris.shard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class ShardApplication implements ApplicationRunner {

//    private final TodoRepository repository;

    public static void main(String[] args) {

//        web, h2, shardingsphere master slave

        // 1. readOnly 타입으로 분기쳐서 master slave로 가져오는것 확인,
        //https://velog.io/@kingcjy/Spring-Boot-JPA-DB-Replication-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0
//        https://shardingsphere.apache.org/document/current/en/manual/sharding-jdbc/usage/read-write-splitting/
        // 2. 샤딩스피어 붙인다.
        //
        SpringApplication.run(ShardApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        final List<Todo> collect = IntStream.range(0, 5)
//                .mapToObj(i ->
//                        Todo.builder()
//                                .title("title" + i)
//                                .completed(i % 2 == 0)
//                                .userId((long) i)
//                                .build()
//                ).collect(Collectors.toList());
//
//        repository.saveAll(collect);
    }
}
