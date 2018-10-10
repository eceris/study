# [자바 11] HttpClient 사용하기

[동영상 링크 1](https://www.youtube.com/watch?v=vUa_Rrso06Q)
[동영상 링크 2](https://www.youtube.com/watch?v=Epv3eiAaAuU&t=20s)


### Java 11 이전에는 ...
pure 하게 URLConnection 을 사용하는 방법이 있는데, 동기 방식.

```java
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
```

### 자주 사용했던 방식이 RestTemplate
프로젝트 하면서 자주 사용했던 방식이 RestTemplate 인데 .... 이것도 동기방식이다. 
new RestTemplate() 해도 좋지만 빌더도 제공하니까... 빌더를 사용하는게 좋을듯

```java
private static void restTemplate() {
    RestTemplate restTemplate = new RestTemplateBuilder().build();
    String response = restTemplate.getForObject("http://localhost:8080/delay?seconds=2", String.class);
    System.out.println(response);
    System.out.println(System.currentTimeMillis());
}
```

### 그리고 Java 11에서 제공하는 httpClient
Java 11 이니까 var 키워드를 사용해서 `HttpResponse<String>` 를 명시하지 않아도 됨...
근데 얘도 동기방식...
뭔가 빌더도 제공하고 static한 생성자도 제공하면서 우아해보이지만... 그렇지 않음..

```java
private static void httpclientsync() throws IOException, InterruptedException {
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/delay?seconds=3")).GET().build();
    var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(response.body());
    System.out.println(System.currentTimeMillis());
}
```

### httpClient 를 비동기로 해볼까?
위에 소개한 httpClient api에 sendAsync라는 메소드가 있는데, 이걸 사용하면 비동기로 동작.

```java
private static void httpclientasync() {
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/delay?seconds=3")).GET().build();
    httpClient
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println);
    System.out.println(System.currentTimeMillis());
}
```
비동기로 동작하는 것처럼 보이지만, 실제로 시스템의 리소스가 부족한 상황에서는 동기로 동작할 때도 있더라...<sup>뭔가 조금 이상함......</sup>

### 그래서 spring에 webClient 를 사용하자.
httpClient가 비동기메소드를 제공하고 코드를 우아하게 작성할 수 있지만, 동작이 조금 이상하더라...
그래서 Spring에서 제공하는 WebClient를 사용했는데, 얘는 우리 예상한대로, Non-Block 비동기로 동작한다!!!.

```java
private static void webclient() {
    WebClient.builder().build().get().uri("http://localhost:8080/delay?seconds=3")
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(r -> {
                System.out.println(r);
                System.out.println(System.currentTimeMillis());
            });
}
```

그래서 결론은 ...

> HttpClient를 진짜 low 하게 들여다 보고 사용할게 아니면.... 그냥 **WebClient** 를 사용하자. 그게 좋을듯 싶다.
