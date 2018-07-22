# RFP
## Reactive Functional Programming

리액티브의 특징

- Duality [쌍대성](https://ko.wikipedia.org/wiki/%EC%8C%8D%EB%8C%80%EC%84%B1)
- Observer Pattern
- Reactive Streams - 표준 - Java9 API

Observer Pattern의 문제점 

```java
package com.daou.webmvc;

import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class Test {

    static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public static void main(String[] args) throws InterruptedException {

        Map map = new HashMap<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = REST_TEMPLATE.getForObject(url, String.class);
                map.put("response", result);
                synchronized (map) {
                    map.notify();
                }
            }
        });
        thread.start();
        if (map.get("response") == null) {
            synchronized (map) {
                map.wait();
            }
        }
        System.out.println(map.get("response"));
    }
}

```
onComplete??
onError??

Observable
reactive의 spec(토비는 프로토콜이라고 함...)
publisher는 data provider, subscriber 는 data consumer
```java
onSubscribe onNext* (onError | onComplete)? //onError와 onComplete는 상호배타적 
```


```java
package com.eceris.reactive;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.TimeUnit;

public class PubSub {
    public static void main(String args[]) throws InterruptedException {

        Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);
        ExecutorService es = Executors.newCachedThreadPool(); //jvm이 적절히 만들어주는 thread Pool
        Publisher pub = sub -> {
            Iterator<Integer> it = itr.iterator();
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    es.execute(() -> {
                        int i = 0;
                        while (i++ < n) {
                            if (it.hasNext()) {
                                sub.onNext(it.next());
                            } else {
                                sub.onComplete();
                                break;
                            }
                        }
                    });
                }

                @Override
                public void cancel() {

                }
            });
        };

        pub.subscribe(new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println(Thread.currentThread().getName() + " onSubscribe");
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println(Thread.currentThread().getName() + " onNext : " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError : " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
        es.awaitTermination(1000, TimeUnit.MILLISECONDS);
        es.shutdown();
    }
}
```

로그는 아래
```console
main onSubscribe
pool-1-thread-1 onNext : 1
pool-1-thread-2 onNext : 2
pool-1-thread-1 onNext : 3
pool-1-thread-2 onNext : 4
pool-1-thread-1 onNext : 5
onComplete
```

## Operator

Publisher -> [Data1] -> Operator -> [Data2] -> Subscriber
```java
Publisher pub = sub -> {
	sub.onSubscribe(new Subscription(
		request();
		cancel();
	));
};

Publisher operator = sub -> {
	pub.subscribe(sub);
	sub.onSubscribe(new Subscription(
		request();
		cancel();
	));
};

Subscriber sub = new Subscriber() {
	onSubscribe(Subscription subscription);
	onNext(Integer integer);
	onError(Throwable throwable);
	onComplete();
};

operator.subscribe(sub);
```

iterPub -> [Data1] -> mapPub -> [Data2] -> logSub

```java
package com.daou.webflux;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Operator {

    public static void main(String[] args) {
        Publisher<Integer> iterPub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));
        Publisher<Integer> mapPub = mapPub(iterPub, s -> s * 10);
        mapPub.subscribe(logSub());
    }

    static Publisher<Integer> iterPub(List<Integer> iter) {
        return sub -> sub.onSubscribe(new Subscription() {
            public void request(long n) {
                iter.forEach(s -> sub.onNext(s));
                sub.onComplete();
            }

            public void cancel() {

            }
        });
    }

    static Publisher<Integer> mapPub(Publisher<Integer> iterPub, Function<Integer, Integer> f) {
        return sub -> iterPub.subscribe(new Subscriber<Integer>() {
            public void onSubscribe(Subscription s) {
                sub.onSubscribe(s);
            }

            public void onNext(Integer i) {
                try {
                    sub.onNext(f.apply(i));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            public void onError(Throwable t) {
                sub.onError(t);
            }

            public void onComplete() {
                sub.onComplete();
            }
        });
    }

    static Subscriber<Integer> logSub() {
        return new Subscriber<Integer>() {
            public void onSubscribe(Subscription s) {
                System.out.println("logSub onSubscribe()");
                s.request(Long.MAX_VALUE);
            }

            public void onNext(Integer i) {
                System.out.println("logSub onNext:" + i);
            }

            public void onError(Throwable t) {
                System.out.println("logSub onError:" + t);
            }

            public void onComplete() {
                System.out.println("logSub onComplete()");
            }
        };
    }
}

```

로그는 아래
```console
logSub onSubscribe()
logSub onNext:10
logSub onNext:20
logSub onNext:30
logSub onNext:40
logSub onNext:50
logSub onNext:60
logSub onNext:70
logSub onNext:80
logSub onNext:90
logSub onNext:100
logSub onComplete()
```

```java
package com.daou.webflux;
import reactor.core.publisher.Flux;
import java.util.stream.Stream;

public class FluxOperator {

    public static void main(String[] args) {
        Flux
            .<Integer>create(e -> {
                Stream.iterate(1, a -> a + 1).limit(10).forEach((i) -> e.next(i));
                e.complete();
            })
            .doOnSubscribe(subscription -> System.out.println("onSubscribe()"))
            .map(s -> s * 10)
            .doOnComplete(() -> System.out.println("onComplete()"))
            .subscribe(System.out::println);
    }
}
```
로그는 아래
```console
onSubscribe()
10
20
30
40
50
60
70
80
90
100
onComplete()
```

Funtion
BiFunction
...
publisher를 구현하여 operator 역할을 하도록 한다.


# Scheduler
Scheduler에는 두가지 방식이 있다.
subscribeOn(); 라는 operator에 scheduler를 인자로 넘겨서 구현.
publishOn(); 라는 operator에 scheduler를 인자로 넘겨서 구현.

### [reactor.core.publisher.flux.subscribeOn()](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#subscribeOn-reactor.core.scheduler.Scheduler-)

Typically used for slow publisher(e.g., blocking IO), fast consumers scenarios.
publisher가 느린 경우, publisher를 별개의 스레드에서 구현(subscribeOn을 별개의 스레드에서...)
-> onNext()를 호출할 때마다 공이 퍼블리셔에서 섭스크라이버 또는 오퍼레이터로 하나씩 떨어진다.
-> subscribeOn() 메소드 안의 스케줄러가 퍼블리셔의 역할을 한다.



### [reactor.core.publisher.flux.publishOn()](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#publishOn-reactor.core.scheduler.Scheduler-)

Typically used for fast publisher, slow consumers scenarios.
publisher는 빠르나 subsciber가 느린 경우에 subscriber를 별개의 스레드에서 구현(onNext...둥등의 메소드를 별개의 스레드에서..)
-> 데이터를 받아서 처리하는 subscriber 쪽을 별개의 스레드로 만들어서 데이터를 처리하도록 할 때 사용한다.
-> 빠른 퍼블리셔, 느린 섭스크라이버 환경에서 사용한다.

스프링 환경에서 스레드 풀을 만들때 여러가지 옵션을 주고 싶은 경우, CustomizableThreadFactory()를 구현하여 Executors.newSingleThreadExcutor() 의 인자로 넘긴다. 

# Flux
reactor.core.publisher.Flux 패키지에는 여러 operator들을 사용할 수 있도록 구현되어있다.

user thread 와 daemon thread 의 차이
daemon thread만 남아있을 경우 그냥 JVM이 종료됨.

Flux.interval() --> operator 설명
.take() --> operator 설명


Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate();
를 구현하면 정해진 rate로 계속해서 수행하는 executor

# Future...는 뭘까요?

# Future
Future.get(); //blocking

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
```java

@Async
public Future<String> hello() {
	return new AsyncResult<>("hello");
}

hello().get(); //blocking
```
문제는 매 요청마다 Thread를 생성한다.(100번 호출하면 100개의 Thread가 생성)


ListenableFuture << 스프링에서 구현된 객체(callback 형식으로 등록 가능)

CompletableFuture << java 9에 들어간 것(혁명적인 것.... 나중에 따로 볼것)


@AsyncAnnotation의 스레딩 정책을 설정하는 방법
ThreadPoolTaskExecutor를 @Bean으로 등록
default는 SimpleAsyncTaskExecutor는 호출 될 때마다 스레드를 생성하고 삭제

Thread는 block 되면 CPU 자원을 많이 차지하는 이유는 ContextSwitching 때문.
Thread 갯수를 늘리는 것도 결국은 ContextSwitching이 많아지는 것이기 때문에, 속도가 느려질 수 있다.


DefferredResult (스프링 비동기의 꽃)
```java
@RestController
public class Application {
    Queue<DeferredResult<String>> results = new ConcurrentLinkedDeque<>();

    @GetMapping("/dr")
    public DeferredResult<String> dr() {
        log.info("/dr");
        DeferredResult<String> dr = new DeferredResult<>(Long.MAX_VALUE);
        results.add(dr);
        return dr;
    }

    @GetMapping("/dr/event")
    public String drevent(String msg) {
        results.forEach(dr -> {
            dr.setResult("Hello " + msg);
            results.remove(dr);
        });
        return "OK";
    }

    @GetMapping("/dr/count")
    public int drcount() {
        return results.size();
    }
}
```
setResult가 오기전까지 다른 모든 요청의 응답이 대기(worker thread가 대기하는것도 아니고, servlet thread도 대기하는 것이 아니다. 단지 메모리에 상주할 뿐)
DefferedResult를 사용해서 간단한 채팅도 구현이 가능할 것 같다.

ResponseBodyEmitter
HTTP STREAMING .. SSE와 비슷한 개념
한번의 요청에 여러번 데이터를 나눠서 보내는 ....
```java
@GetMapping("/emitter")
public ResponseBodyEmitter emitter() {
    ResponseBodyEmitter emitter = new ResponseBodyEmitter();
    Executors.newSingleThreadExecutor().submit(() -> {
        IntStream.range(1, 50).forEach(i -> {
            try {
                emitter.send("<p>Stream " + i + "</p>");
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    });
    return emitter;
}
```

## 밍 (6) AsyncRestTemplate의 콜백 헬
# ListenableFuture
spring 4에 처음 들어간 Future.




# ~~AsyncRestTemplate~~
deprecated 되었네요 .. ㄷㄷ

#CompletableFuture
Future를 비동기 작업의 결과를 직접 다룰수 있다. 

complete();
completeExceptionally(new RuntimeException());

CompletionStage java8에 추가(Promise 라고도 하고 ...)
-> 하나의 비동기 작업을 수행하고 여ㄱ기에 의존적으로 뭔가를 할수 있도록 하는 것 


CompletableFuture.runAsync(() -> {})
.thenRun(() -> {})
.thenRun(() -> {})
.thenRun(() -> {})


(모나드!! 함수형 언어에서 나오는 모나드...꼭 볼것)
CompletableFuture.supplyAsync(() -> s)
.thenApply(s -> s1)
.thenApply(s1 -> s2)
.thenApply(s2 -> s3) // function
.exceptionally(e -> e)
.thenAccept(s3 -> {}); //consumer   , .thenAcceptAsync(s3 -> {}); //비동기로 하는데 무조건 thread 정책을 어떻게 할것인가에 대한 내용이 필요함.(ExecutorSerivce)


ForkJoinPool.commonPool().shutdown();

thenCompose vs thenApply

thenApply의 리턴값으로 completable future를 받고 싶을 경우 thenCompose()로 할것.

thenCompose() : Stream의 flatMap
thenApply() : Map 
으로 생각하면 된다.
