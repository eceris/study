# 규칙 70. 스레드나 안전성에 대해 문서로 남겨라

## Javadoc에는 synchronized 키워드가 없다.
- synchronized 키워드는 메서드의 구현 상세<sup>implementation detail</sup>에 해당하는 정보이며, 공개 API의 일부가 아니다.
- 다중 스레드에 안전하지 않다.

## 병렬적으로 안전한 클래스가 되려면, 어떤 수준의 스레드 안전성을 제공하는 클래스인지 문서로 남겨야 한다. 

### 변경 불가능<sup>immutable</sup>
- 이 클래스로 만든 객체는 상수.
- 별도의 외부 동기화 메커니즘이 없이도 병렬적 사용 가능<sup>ex : String, Long, BigInteger</sup>

### 무조건적 스레드 안전성<sup>unconditionally thread-safe</sup>
- 이 클래스로 만든 객체는 변경 가능하지만 적절한 내부 동기화 메커니즘을 갖고 있음. 
- 외부적으로 동기화 메커니즘을 적용하지 않아도 병렬적으로 사용가능<sup>ex : Random, ConcurrentHashMap</sup>

### 조건부 스레드 안전성<sup>conditionally thread-safe</sup>
- 무조건적 스레드 안전성과 같은 수준이나 몇몇 스레드는 외부적 동기화 없이는 병렬적으로 사용할 수 없음
- 예를 들면 Collections.synchronized 계열 메서드가 반환하는 wrapper 객체의 Iterator는 동기화가 필요.

### 스레드 안전성 없음
- 이 객체들은 변경가능. 
- 병렬적으로 사용하기 위해 동기화 수단이 필요<sup>ArrayList, Map</sup>

### 다중 스레드에 적대적<sup>thread-hostile</sup>
- 호출하는 모든 부분을 동기화 수단으로 감싸더라도 안전하지 않다. 지금은 사용하지 않음.


다른 객체에 대한 뷰 역할을 하는 객체의 경우 클라이언트는 원래 객체에 대해 동기과를 해야함. 
예를 들면, Collections.synchronizedMap 의 문서를 보면 아래와 같이 나와있다.
```
맵에 대한 컬렉션 뷰를 순회할 때는 수동적으로 원래 맵에 동기화하도록 하라.
```
```java
Map<K, V> m = Collections.synchronizedMap(new HashMap<K, V>());
...
Set<K> s = m.keySet(); // 동기화 블럭 안에 있을 필요 없음
...
synchronized(m) { // s가 아니라 m에 대해 동기화!
	for (K key : s) 
		key.f();
}
```

위의 지침을 따르지 않을 경우 프로그램은 비결정적으로<sup>non-deterministic</sup> 동작할 수 있다.

## Dos 공격
외부로 공개한 락을 통해 동기화 하도록 하는 클래스의 경우 유연하긴 하지만 클라이언트가 락을 오래동안 들고 잇을 경우 Dos<sup>denial-of-service attack</sup> 공격도 가능하다. 이런 공격을 막기 위해...
```java
private final Object lock = new Object();

public void foo() {
	synchronized(lock) {
		...
	}
}
```
락 객체는 private이기에 클래스 바깥에서는 사용 불가.

> 무조건 적인 스레드 안전성을 제공하는 클래스를 구현하는 중이라면 메서드를 **synchronized** 로 선언하는 대신 private 락 객체를 사용하면 어떨지 따져보자. 
