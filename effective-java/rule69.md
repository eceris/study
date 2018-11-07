# 규칙 69. wait나 notify 대신 병행성 유틸리티를 이용하라
wait와 notify를 정확하게 사용하는 것이 어렵기 때문에, 자바 1.5 부터 포함된 고수준 병행성 유틸리티<sup>high-level concurrency utility</sup>를 반드시 이용해야 한다. 총 세가지 범주로 나눌수 있는데 

1. [실행자 프레임워크](rule68.md)<sup>executor service</sup>
2. 병행 컬렉션<sup>concurrent collection</sup>
3. 동기자<sup>synchronizer</sup>
에 대해 알아본다.

## 병행 컬렉션
병행 컬렉션은 List, Queue, Map 등의 표준 컬렉션 인터페이스에 대한 concurrency를 보장하는 컬렉션 구현을 제공.
- [이 컬렉션들은 병행성을 높이기 위해 동기화를 내부적으로 처리](rule67.md)

예를 들면, 컬렉션 인터페이스중 일부는 상태 종속 변경 연산<sup>state-depended modify operation</sup>으로 확장 되었는데, ConcurrentMap은 putIfAbsent()와 같은 메소드를 제공. 다중스레드에 안전한 정규화 맵<sup>canonicalizing map</sup>을 쉽게 구현가능.

## ConcurrentHashMap을 사용하자. 
- Collections.synchronizedMap 이나, HashTable 대신 ConcurrentHashMap을 사용하는 것이 좋은데, 이렇게 구식을 사용하기 보다 병렬형 맵으로 변경하기만 해도 성능은 극적으로 개선된다.

## 동기자<sup>synchronizer</sup>
- synchronizer 는 스레드들이 서로 기다릴 수 있도록 하여 상호 협력이 가능하다. 
자주 사용하는 것은

- CountDownLatch
- Semaphore
- CyclicBarrier
- Exchanger

### 특정 구간의 실행 시간을 잴 때는 System.currentTimeMillis(); 보다는 System.nanoTime()을 사용하는 것이 낫다. 
- 시스템의 실시간 클락<sup>real-time clock</sup> 변동에도 영향을 받지 않음.

## wait 메서드를 호출 할때는 반드시 대기 순환문<sup>wait loop</sup> 숙어를 이용할 것.

```java
// wait 메서드를 사용하는 표준적 숙어
synchronized(obj) {
	while (<이 조건이 만족되지 않을 경우에 순환문 실행>)
		obj.wait(); // 락 해제. 깨어나면 다시 락 획득.

	... // 조건이 만족되면 그에 맞는 작업 실행
}
```

> 새로 만드는 프로그램에 wait나 notify를 사용할 이유는 없다. 만약 레가시를 유지보수해야 한다면 wait-while 숙어를 사용 하라.
