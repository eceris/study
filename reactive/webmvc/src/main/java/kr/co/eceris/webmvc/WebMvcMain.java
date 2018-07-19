package kr.co.eceris.webmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebMvcMain {

    public static void main(String[] args) {
        System.setProperty("server.port", "8081");
        SpringApplication.run(WebMvcMain.class, args);
    }
}

