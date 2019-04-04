# Operator
publisher를 구현하여 operator 역할을 하도록 한다.

ex) pub -> op -> sub
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

# Scheduler
Scheduler에는 두가지 방식이 있다.
subscribeOn(); 라는 operator에 scheduler를 인자로 넘겨서 구현.
publishOn(); 라는 operator에 scheduler를 인자로 넘겨서 구현.

### reactor.core.publisher.flux.subscribeOn()
https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#subscribeOn-reactor.core.scheduler.Scheduler-

Typically used for slow publisher(e.g., blocking IO), fast consumers scenarios.
publisher가 느린 경우, publisher를 별개의 스레드에서 구현(subscribeOn을 별개의 스레드에서...)


### reactor.core.publisher.flux.publishOn()
https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#publishOn-reactor.core.scheduler.Scheduler-

Typically used for fast publisher, slow consumers scenarios.
publisher는 빠르나 subsciber가 느린 경우에 subscriber를 별개의 스레드에서 구현(onNext...둥등의 메소드를 별개의 스레드에서..)


스프링 환경에서 스레드 풀을 만들때 여러가지 옵션을 주고 싶은 경우, CustomizableThreadFactory()를 구현하여 Executors.newSingleThreadExcutor() 의 인자로 넘긴다. 

### Flux
reactor.core.publisher.Flux 패키지에는 여러 operator들을 사용할 수 있도록 구현되어있다.

user thread 와 daemon thread 의 차이
daemon thread만 남아있을 경우 그냥 JVM이 종료됨.

Flux.interval() --> operator 설명
.take() --> operator 설명


Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate();
를 구현하면 정해진 rate로 계속해서 수행하는 executor
