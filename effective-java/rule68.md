# 규칙 68. 스레드보다는 실행자와 태스크를 이용하라

Executor framework 는 유연성이 높은 인터페이스 기반 태스크<sup>task</sup> 실행 프레임워크이다. 
```java
ExecutorService executor = Executors.newSingleThreadExecutor();
executor.execute(runnable);
executor.shutdown();
```

작업을 처리하는 스레드 여러개를 만들고 싶을 때는 ThreadPool 을 이용하라. 

#### 작은 프로그램이거나 부하가 크지 않은 서버를 만들 때는 보통 Executors.newCachedThreadPool()을 사용.
- 설정이 필요없고 많은 일을 잘 처리.
- 그러나 부하가 많은 경우 태스크가 들어오면 새로운 스레드를 또 만드므로 상황이 더 악화됨.

#### 부하가 심한 환경에 들어갈 서버를 만들 때는 Executors.newFixedThreadPool()을 사용.
- 스레드 갯수를 직접 지정하여 사용. 

## task 에는 두가지가 있다.
Runnable 과 Callable 
- 태스크와 실행자 서비스를 분리할 경우 실행 정책<sup>execution policy</sup>를 더 유연하게 적용가능.
- 실행자는 단순히 태스크를 실행하는 부분을 담당.

## ScheduledThreadPoolExecutor
Timer의 아래 단점을 극복하기 위해 사용. 

- Timer는 하나의 thread를 사용하기에 실행시간이 긴 태스크 일 경우 정확도가 떨어짐.
- 스레드 안에서 발생한 예외<sup>uncaught exception</sup>가 제대로 처리되지 않을 경우, Timer는 동작하지 않음.

ScheduledThreadPoolExecutor는 여러 스레드를 이용, 태스크 안에서 무점검 예외가 발생한 상황도 우아하게 복구.