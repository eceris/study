

1. 문제
오이도에서는 예상치 않은 예외가 발생했을 경우 처리를 위해 AbstractErrorWebExceptionHandler를 상속받아 로그를 남기도록 구현하고 있습니다. AsyncAppender와 함께 SentryAppender를 사용해 센트리에 메시지와 stacktrace 등등 여러 정보를 남기고 있는데요....
Sentry에 stacktrace가 기대한대로 남지 않는 이슈가 있었습니다.

이렇게요..

```
at com.kakaobank.oido.was.domain.account.AccountHandler.getKakaoBankAccountList(AccountHandler.java:103)
```

파일에는..
```
java.lang.RuntimeException: null
	at com.kakaobank.oido.was.domain.account.AccountHandler.getKakaoBankAccountList(AccountHandler.java:103)
	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
Error has been observed at the following site(s):
	|_ checkpoint ⇢ com.kakaobank.oido.was.router.filter.ServicePossibleTimeFilter [DefaultWebFilterChain]
	|_ checkpoint ⇢ com.kakaobank.oido.was.router.filter.WebLoggingFilter [DefaultWebFilterChain]
	|_ checkpoint ⇢ org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter [DefaultWebFilterChain]
	|_ checkpoint ⇢ HTTP GET "/oapi/v1/accounts/tbIMqjLSUB/kakaobank" [ExceptionHandlingWebHandler]
Stack trace:
		at com.kakaobank.oido.was.domain.account.AccountHandler.getKakaoBankAccountList(AccountHandler.java:103)
		at com.kakaobank.oido.was.router.filter.ContextInfoHandlerFilter.filter(ContextInfoHandlerFilter.java:55)
		at org.springframework.web.reactive.function.server.HandlerFilterFunction.lambda$apply$2(HandlerFilterFunction.java:72)
		at org.springframework.web.reactive.function.server.support.HandlerFunctionAdapter.handle(HandlerFunctionAdapter.java:61)
		at org.springframework.web.reactive.DispatcherHandler.invokeHandler(DispatcherHandler.java:161)
		at org.springframework.web.reactive.DispatcherHandler.lambda$handle$1(DispatcherHandler.java:146)
		at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:118)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:67)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.MonoNext$NextSubscriber.onNext(MonoNext.java:76)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxConcatMap$ConcatMapImmediate.innerNext(FluxConcatMap.java:274)
		at reactor.core.publisher.FluxConcatMap$ConcatMapInner.onNext(FluxConcatMap.java:851)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxMap$MapSubscriber.onNext(FluxMap.java:114)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:192)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:67)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxMap$MapSubscriber.onNext(FluxMap.java:114)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:67)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxMap$MapSubscriber.onNext(FluxMap.java:114)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:67)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxMap$MapSubscriber.onNext(FluxMap.java:114)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:67)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxMap$MapSubscriber.onNext(FluxMap.java:114)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:67)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxMap$MapSubscriber.onNext(FluxMap.java:114)
		at com.kakaobank.oido.was.config.MdcContextLifterConfiguration$MdcContextLifter.onNext(MdcContextLifterConfiguration.java:50)
		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber
```


뭔가... 잘 보면 **Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:** 이후로는 로그가 센트리에 안남는 상황입니다.


2. 추적

2-1. 일단 SentryAppender 를 까보기 시작...했는데.....아래와 같은 순서로 센트리에 전송
	- SentryAppender를 통해 throwable 객체를 받아 EventBuilder 객체로 변경
	- stacktrace 정보는 throwable.getStackEventBuilder.withSentryInterface() 에 저장후 전송.

그러면.... 센트리는 stacktrace 정보를 가공하지 않는거고... 실제로 stacktrace가 한줄이었다는 얘기.....

2-2. 그럼 다시, stacktrace는 누가 만들어주지?
	- 일단 의심이 되는 [OnAssemblyException](https://github.com/reactor/reactor-core/blob/master/reactor-core/src/main/java/reactor/core/publisher/FluxOnAssembly.java#L411) 이놈을 보기 시작..
	- onError에서 호출하는 fail(throwable)을 뜯어보니 뭔가 stackTrace를 조작하는데 요약하면....
	```
		- throwable에 있던 기존 stackTrace 를 suppressed 로 담는다.
		- 그리고 throwable 에는 기존 stackTrace에 있던 한개만 담는다.
		- throwable에 있던 기존 stackTrace 를 onAssemblyException 에 담는다.
	```
	- 진짜로 sentry에 전송되는 throwable에는 stacktrace한개만 담음.

2-3. 왜그럴까?
	- [이슈](https://github.com/reactor/reactor-core/pull/1781)를 찾아보니 reator-core에서 debug모드의 중요정보를 아래에 보여주는 것 보단, 위에 보여주는것이 맞다고 함 -> debug 모드에서는 보기 편함....
	- [근데 사용자가 만드는 코드는 message가 사용자가 보기 편리한건 아니라고 함.](https://github.com/reactor/reactor-core/pull/1781#issuecomment-507715538)
	- 어쨋든 sentry가 필요한 메시지는 suppressed 안에 담겨있음.....

2-4. suppressed는 뭐지?? 
	- 아래에 따로 설명.


3. 해결
	- 개발자는 파일로그보다는 sentry를 보고 예외를 추적함. 일단은 sentry에서 스택정보를 받을 수 있게 직접 넣어주자.
	```
	if (throwable.getSuppressed().length > 0) {
		throwable.setStackTrace(throwable.getSuppressed()[0].getStackTrace());
	}
    log.error(throwable.getMessage(), throwable);
	```



아래. Suppressed
- suppressed Exception은 던져지지만 무시되는 예외. 이것을 알아볼 수 있는 예외는 자바에서 try catch finally 블록의 finally 블록에서 예외를 발생시키는 시나리오. 
- finally에서 던져지는 예외로 인해 원래 Exception이 먹힘. 그래서.... finally에서 발생하는 예외.addSuppressed(firstException)를 하여 추가해주면 throwable에서 접근 가능하다.
```
public static void demoAddSuppressedException(String filePath) throws IOException {
    Throwable firstException = null;
    FileInputStream fileIn = null;
    try {
        fileIn = new FileInputStream(filePath);
    } catch (IOException e) {
        firstException = e;
    } finally {
        try {
            fileIn.close();
        } catch (NullPointerException npe) {
            if (firstException != null) {
                npe.addSuppressed(firstException);
            }
            throw npe;
        }
    }
}
```

- 자바7의 Throwable 객체에 들어간 것.
- 근데 Using try-with-resources 를 쓰면 잘 잡아서 처리해준다.
```
public class ExceptionalResource implements AutoCloseable {
     
    public void processSomething() {
        throw new IllegalArgumentException("Thrown from processSomething()");
    }
 
    @Override
    public void close() throws Exception {
        throw new NullPointerException("Thrown from close()");
    }
}

public static void demoExceptionalResource() throws Exception {
    try (ExceptionalResource exceptionalResource = new ExceptionalResource()) {
        exceptionalResource.processSomething();
    }
}

try {
    demoExceptionalResource();
} catch (Exception e) {
    assertThat(e, instanceOf(IllegalArgumentException.class));
    assertEquals("Thrown from processSomething()", e.getMessage());
    assertEquals(1, e.getSuppressed().length);
    assertThat(e.getSuppressed()[0], instanceOf(NullPointerException.class));
    assertEquals("Thrown from close()", e.getSuppressed()[0].getMessage());
}

```




결론 























