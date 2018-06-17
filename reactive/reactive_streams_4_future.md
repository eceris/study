# Future

future.get(); //blocking


Future를 object로 만든 것 : FutureTask

```java
package com.eceris.reactive;

import java.util.concurrent.*;

public class FutureEx {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> f = new FutureTask<>(() -> {
            Thread.sleep(2000);
            System.out.println("h2");
            return "Hello";
        });
        es.execute(f);

        Future<String> hello = es.submit(() -> {
            Thread.sleep(2000);
            System.out.println("h1");
            return "Hello";
        });

        System.out.println(hello.get());
        System.out.println("exit");
    }
}
```

executorService.shutdown(); // 모든 작업을 끝내고 난 뒤에 종료하라...


FutureTask를 익명클래스로 override 한 경우 
```java
package com.eceris.reactive;

import java.util.concurrent.*;

public class FutureEx {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> f = new FutureTask<>(() -> {
            Thread.sleep(2000);
            System.out.println("h2");
            return "Hello";
        }) {
            @Override
            protected void done() {
                try {
                    System.out.println(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        es.execute(f);
        es.shutdown();

        System.out.println("exit");
    }
}
```


Future를 이용해서 결과를 받아오는 것(but, blocking이면서 try catch룰 구현해야함.)
Callback 을 직접 구현하여 만드는 것.(비지니스로직과 인프라로직이 혼재되어있는 문제가 발생 ....)




Spring 에서의 비동기 
@EnableAsync를 application context에 알려주고

@Async를 메소드 레벨에 작성하여 사용
ex :

```java

@Async
public Future<String> hello() {
	return new AsyncResult<>("hello");
}

hello().get(); //blocking
```


ListenableFuture << 스프링에서 구현된 객체(callback 형식으로 등록 가능)

CompletableFuture << java 에 들어간 것(혁명적인 것.... 나중에 따로 볼것)


@Async annotation의 스레딩 정책을 설정하는 방법
ThreadPoolTaskExecutor를 @Bean으로 등록
default는 SimpleAsyncTaskExecutor는 호출 될 때마다 스레드를 생성하고 삭제



DefferredResult (스프링 비동기의 꽃)

ResponseBodyEmitter
한번의 요청에 여러번 데이터를 나눠서 보내는 ....
