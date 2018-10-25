# 규칙 61. 추상화 수준에 맞는 예외를 던져라
추상화 수준이 낮은 곳에서 발생한 예외를 그대로 밖으로 전달하면 현재 메서드가 하는일과 관계없는 에외를 내뱉기도 하는데, 이러한 경우는 **추상화 수준이 높은 API**가 **구현 세부사항**으로 **오염**되는 일이 벌어짐.

### 예외 변환<sup>exception translation</sup>
**상위 계층**에서는 **하위 계층에서 발생하는 예외**를 반드시 받아서 **상위 계층 추상화 수준**에 맞는 예외로 바꿔서 던져야 한다.
예제로 만들어 보면...
```java
//예외 변환
try {
	//낮은 수준의 추상화 계층 이용
	...
} catch() {
	throw new HigherLevelException(...);
}
```
위와 같이 구현된 예외변환 예제가 `AbstractSequentialList` 클래스에 존재한다. `List<E>` 인터페이스의 get 메서드를 보면 아래와 같다.
```java
/**
*	Returns the element at the specified position in this list.
*	@throws IndexOutOfBoundsException if the index is out of range
*	({@code index <0 || index >= size()}).
*/
public E get(int index) {
	ListIterator<E> i = listIterator(index);
	try {
		return i.next();
	} catch(NoSuchElementException e) {
		throw new IndexOutOfBoundsException("Index : " + index);
	}
}	
```

### 예외 연결<sup>exception chaning</sup>
- 하위 계층에서 발생한 예외정보가 상위 계층 예외를 발생시킨 문제를 디버깅하는데 유용할 때 사용.
- 하위계층 예외<sup>cause</sup> 는 상위 계층 예외로 전달되는데, 상위 계층 예외에 있는 접근자 메서드<sup>Throwable.getCause()</sup> 를 호출하여 정보를 꺼낼수 있다.
```java
//예외연결
try {
	... //낮은 수준의 추상화 계층 이용
} catch(LowerLevelException cause) {
	throw new HigherLevelException(cause);
}
```
상위 계층 예외 HigherLevelException 의 생성자는 문제의 `원인`을 예외 연결을 지원하는<sup>chaining-aware</sup> 상위 클래스 생성자에 넘김.
해당 인자는 Throwable 의 예외 연결 지원 생성자에 전달하는데 아래와 같음.
```java
class HigherLevelException extends Exception {
	HigherLevelException(Throwable cause) {
		super(cause);
	}
}
```
이렇게 하면 getCause()를 이용해 예외의 원인에 접근하고, 스택 정보<sup>stack trace</sup>를 상위계층 예외에 통합할 수 있다.

#### 아무 생각없이 아래 계층에서 생긴 예외를 밖으로 전달하기만 하는 것보다야 예외 변환기법이 낫지만, 남용하면 안된다.
- 가능하다면 제일 좋은 방법은 하위 계층에서 예외가 생기지 않도록 하는 것.
- 하위계층에서 예외가 발생하는 것을 막을 수 없다면, 하위 계층에서 생기는 문제를 격리. 어떤식으로든 예외를 처리해버리는 것. logging 할 것.

> 요약 하면, 하위 계층에서 발생하는 예외를 막거나 처리할 수 없다면, 예외 변환을 통해 처리할 것. [예외 연결 패턴을 활용하면 적절한 상위계층 예외를 보여주면서도 하위 계층에서 실제로 발생한 문제까지 확인할 수 있으므로 오류를 분석하기 좋다.](rule63.md)