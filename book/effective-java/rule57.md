# 규칙 57. 예외는 예외적 상황에만 사용하라
아래에 이상한 코드를 보자.
```java
//예외를 끔직하게 남용한 사례. 이러면 곤란하다.
try {
	int i = 0;
	while(true) {
		range[i++].climb();
	}
} catch(ArrayIndexOutOfBoundsException e) {

}
```
- 가독성 : 무슨 일을 하는 코드인지 잘 알지 못한다.
- 예외를 무시
- 잘못된 추론을 근거로 성능을 개선해보려 한 것 같다.

위와 같은 코드는 아래와 같이 간단하게 작성할 수 있다.
```java
for(Mountain m : range) {
	m.climb();
}
```

### 잘못된 추론에는 세가지 오류가 있다.
- 예외는 예외적 상황<sup>exceptional circumstance</sup>를 위해 고안 된 것이다.
- try-catch 블록안에 넣어둔 코드는 JVM 이 사용하는 최적화 기법 가운데 일부가 적용되지 않는다.
- 배열을 순회하는 표준 숙어<sup>standard idiom</sup> 이 중복검사로 이어질 것이란 생각은 잘못되었다. JVM은 그정도는 최적화 해준다.


### 예외는 예외적인 상황에서만 사용해야 한다. 평상시 **제어흐름**<sup>ordinary control flow</sup> 에 이용해서는 안된다.
- 쉽게 이해할 수 있는 표준적인 숙어대로 코딩해야지, 더 좋은 성능을 내보려고 머리 쓸 필요가 없다.<sup>보통 이렇게 고민하면 할 수록 알수 없는 묘한 버그가 생기기도 한다</sup>

### 잘 설계된 API는 클라이언트에게 평상시 제어 흐름의 일부로 예외를 사용하도록 강요해서는 안된다. 
- 보통 상태 종속적 메서드<sup>state-dependent</sup> 를 가진 클래스에는 메서드를 호출해도 되는지를 알기 위한 상태 검사 메서드<sup>state-testing</sup>가 별도로 존재한다.
- 예를 들면 Iterator에는 상태 종속적인 next() 메소드가 존재하고 상태 검사 메소드로 hasNext()가 존재.

> 요약하면, 예외는 예외적인 상황에서만 사용하도록 설계된 메커니즘. **통상적인 제어흐름**에는 사용하지 말고, 그렇게 강요하는 API 도 만들지 말자.