# Async Spring


Thread Pool Hell(LinkedIn)
Latency(응답시간)가 급격하게 떨어진다.....
여러 service간 통신으로 인해 ... 위와 같은 현상이 발생..


CyclicBarrier barrier = new CyclicBarrier(100);

barrier.await();
100개가 될때까지 기다렸다가 
100번 만나게 되면 넘어감.



application.properties를 overrideㅎ ㅏ는 방법은 
System.setProperty("SERVER_PORT", "8081"); 
이런식으로 override;



AsyncRestTemplate rt = new AsyncRestTemplate();
얘만 쓰면 실제로 서블릿 스레드는 안생기지만 내부적으로 thread가 갯수만큰 생성됨.

이게 실제로는 논블락킹이긴 하지만, 스레드를 갯수만큼 생성하기에 좋은 방법은 아님 ..
그래서 아래와 같이 사용....netty 를 사용
AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
thread 1개를 사용해서 처리 ....

AsyncRestTemplate을 사용해서 나온 responseEntity는 ListenableFuture로 래핑되어 나오는데 이걸 그냥 반환하면 onSuccess, onError콜백을 만들지 않더라도 스프링이 알아서 해줌 ...

AsyncRestTemplate을 사용해서 나온 responseEntity를 가공하고자 하는 경우 
DeferredResult를 사용하여 ListenableFuture의 콜백에서 result를 set해주면 됨 





future.get(); //blocking


Future를 object로 만