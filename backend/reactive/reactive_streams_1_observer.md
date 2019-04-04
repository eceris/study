# RFP
reactive functional programming

리액티브의 특징

Duality(쌍대성)

Observer Pattern

Reactive Streams - 표준 - Java9 API

Observer Pattern의 문제점 
onError??
onComplete??


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
        ExecutorService es = Executors.newCachedThreadPool();
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