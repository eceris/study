package com.eceris.shard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ShardApplication {

    public static void main(String[] args) {

//        web, h2, shardingsphere master slave

        // 1. readOnly 타입으로 분기쳐서 master slave로 가져오는것 확인,
        //https://velog.io/@kingcjy/Spring-Boot-JPA-DB-Replication-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0
        // 2. 샤딩스피어 붙인다.
        //


        SpringApplication.run(ShardApplication.class, args);
    }

}
