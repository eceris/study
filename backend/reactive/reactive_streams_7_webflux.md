#WebFlux
기존에 작성했던 코드들을 스프링 5에 맞춰서 진행한다.

Pom.xml에서 SpringWeb 과 ReactiveWeb은 배타적이다. 

##WebClient
builder 스타일로 호출하는 방식으로 바뀜..

Mono<Mono<String>> 이런 타입으로 나오는 경우가 있는데 이럴 경우에는 flatXXXX를 통해서 한번 flat해준다. (flatMap vs map) 비교 잘 할 것.

WebClient의 worker thread를 지정하고 싶을 경우 
Properties에 reactor.ipc.netty.workerCount = 1로 지정하면 된다.(현재까진...)


CompletableFuture -> Mono로 바꾸려면
Mono.fromCompletionStage();

Mono 타입에서 다른 스레드를 태우고 싶은 경우 scheduler를 사용하면 된다. 