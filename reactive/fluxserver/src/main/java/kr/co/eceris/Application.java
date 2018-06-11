package kr.co.eceris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Publisher;

@Controller
@SpringBootApplication
public class Application {
    private static int COUNT = 0;
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @GetMapping("message/{message}")
    @ResponseBody
    public String gathering(@PathVariable("message")String message) {
        return "gathering : " + message;
    }

    @Service
    public class Collector {

        public final List<String> BUFFER = List.of();
        public String collect(String message) {
            BUFFER.add(message);
            return message + "added";
        }
    }
}
