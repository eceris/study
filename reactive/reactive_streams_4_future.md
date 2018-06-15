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


DeferredResult << setResult를 호출 하는 순간 다른 스레드들의 결과값이 반환됨...

DeferredResult는 어떤 요청에 대한 응답을 이벤트를 Queue에 저장하고 있다가 DeferredResult.setResult() 메소드가 호출되면 DispatcherSerlvet으로 응답을 보낸다.  즉 서버가 Push하는 기술들을 구현할 수 있게 해준다.


ResponseBodyEmitter

하나 이상의 객체가 응답에 written되는 비동기 요청 처리를위한 Controller의 리턴 값 유형. DeferredResult가 단일 결과를 생성하는 데 사용되지만 ResponseBodyEmitter를 사용하여 각 객체가 호환되는 HttpMessageConverter로 작성된 여러 객체를 보낼 수 있습니다.

```java
@RequestMapping(value="/stream", method=RequestMethod.GET)
 public ResponseBodyEmitter handle() {
           ResponseBodyEmitter emitter = new ResponseBodyEmitter();
           // Pass the emitter to another component...
           return emitter;
 }

 // in another thread
 emitter.send(foo1);

 // and again
 emitter.send(foo2);

 // and done
 emitter.complete();
```