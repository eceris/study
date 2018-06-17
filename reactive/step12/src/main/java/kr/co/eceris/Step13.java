package kr.co.eceris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Controller
public class Step13 {

    @GetMapping("/rest")
    @ResponseBody
    public Mono<String> rest() {
        System.out.println(Thread.currentThread().getName()  + " / 1");
        Mono<String> hello = Mono.just("Hello").log();
        System.out.println(Thread.currentThread().getName()  + " / 2");
        return hello;
    }



    public static void main(String[] args) {
        SpringApplication.run(Step13.class, "");
    }
}
