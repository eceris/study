package kr.co.eceris.demojava11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

@SpringBootApplication
public class Demojava11Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Demojava11Application.class);
        pure();
        rest();
        httpclientsync();
        httpclientasync();
        webclient();
    }

    /**
     * spring5 의 webclient 를 이용해서 비동기 non-block 으로 호출하는 방법
     */
    private static void webclient() {
        WebClient.builder().build().get().uri("http://localhost:8080/delay?seconds=3")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(r -> {
                    System.out.println(r);
                    System.out.println(System.currentTimeMillis());
                });
    }

    /**
     *  java 11에서 비동기방식으로 호출하는 방법
     */
    private static void httpclientasync() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/delay?seconds=3")).GET().build();
        httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println);
        System.out.println(System.currentTimeMillis());
    }

    /**
     * java 11에서 동기방식으로 호출하는 방법
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private static void httpclientsync() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/delay?seconds=3")).GET().build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        System.out.println(System.currentTimeMillis());
    }

    /**
     * RestTemplate 를 사용하여 동기방식으로 호출하는 방법
     */
    private static void rest() {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String response = restTemplate.getForObject("http://localhost:8080/delay?seconds=2", String.class);
        System.out.println(response);
        System.out.println(System.currentTimeMillis());
    }

    /**
     * Java11 이전에 pure 하게 동기방식으로 호출하는 방법
     * @throws IOException
     */
    private static void pure() throws IOException {
        URL url = new URL("http://localhost:8080/delay?seconds=3");
        URLConnection connection = url.openConnection();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        }
        System.out.println(System.currentTimeMillis());
    }
}
