package kr.co.eceris;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

public class Step12 {

    @GetMapping("/rest")
    public Mono<String> rest(int idx) {
        return Mono.just("Hello");
    }

    public static void main(String[] args) {
        SpringApplication.run(Step12.class, "");
    }
}
