package kr.co.eceris;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private Requester requester;

    @Bean
    ApplicationRunner run() {
        return args -> {
            requester.request();
        };
    }

    @Service
    public class Requester {
        private final RestTemplate template = new RestTemplate();

        public void request() {
            List<Integer> number = Stream.iterate(0, (i) -> i + 1).limit(100).collect(Collectors.toList());

            Publisher<Integer> pub = sub -> {
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        Iterator<Integer> iterator = number.iterator();

                        while (iterator.hasNext()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) { }
                            Integer next = iterator.next();
                            sub.onNext(next);
                        }
                        sub.onComplete();
                    }

                    @Override
                    public void cancel() {
                    }
                });
            };

            pub.subscribe(new Subscriber<>() {
                @Override
                public void onSubscribe(Subscription subscription) {
                    System.out.println("onSubscribe");
                    subscription.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(Integer item) {
                    System.out.println("input : " + item);
                    ResponseEntity<String> result = template.getForEntity("http://localhost:8000/message/" + item, String.class);
                    System.out.println(result.getBody());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("onError : " + throwable);
                }

                @Override
                public void onComplete() {
                    System.out.println("onComplete");
                }
            });
        }

    }
}