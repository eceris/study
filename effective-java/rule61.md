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
작성중 ....