# 규칙 64. 실패 원자성 달성을 위해 노력하라

메서드 호출이 정상적으로 처리되지 못한 **객체의 상태**는, 메서드 호출 전 상태와 동일해야 하는데, 이 속성을 갖춘 메소드는 **실패원자성**<sup>failure atomicity</sup>을 갖추었다고 한다.

## 실패 원자성을 달성하는 방법 

### [변경 불가능 객체로 설계하는 것이다.](rule15.md)
- **변경 불가능한 객체**의 경우, 실패 원자성은 덤.
- 메서드 호출이 실패하면 새로운 객체가 만들어지진 않지만, 기존 객체의 일관성이 깨지진 않는다. 일단 객체가 만들어지면, 상태를 변경할 수 없기 때문.

### [변경 가능한 객체의 경우에는 실제 연산을 수행하기 전에 인자의 유효성을 먼저 검사하는 것이 보편적인 방법이다.](rule38.md)
- 객체를 변경하는 도중에 **예외가 발생**하는 것을 막아준다. 예를 들면 [규칙 6](rule6.md)에서 다뤘던 Stack.pop() 메소드 코드를 보자.
```java
public Object pop() {
	if (size == 0)
		throw new EmptyStackException();
	Object result = elements[--size];
	elements[size] = null; // 만기(obsolute) 참조 제거
	return result;
}
```
메소드의 첫 두줄이 없다면 size필드의 **일관성이 깨져서** 음수로 바뀌게 될 가능성이 존재한다. 

### 실패할 가능성이 존재하는 코드를 전부 객체 상태를 바꾸는 코드 앞에 배치하는것.
- 계산을 실제로 수행해보기 전에는 인자를 검사할 수 없을때 이용가능한 방법.
- 예를 들면, TreeMap 에 추가할 원소는 해당 TreeMap의 순서대로 비교 가능한 자료형이어야 한다.

### 연산 수행 도중에 발생하는 오류를 가로채는 복구 코드<sup>recovery code</sup>를 작성하는 것.
- 복구 코드는 연산이 시작되기 이전 상태로 객체를 되돌린다.<sup>rollback</sup>  디스크 기반의 **지속성**<sup>durable</sup> 자료 구조에 주로 사용되는 기법.

### 객체의 임시 복사본상에서 필요한 연산을 수행하고, 연산이 끝난 다음에 임시 복사본의 내용으로 객체 상태를 바꾸는것.
- 데이터를 **임시 자료구조**에 **복사**한 다음에 훨씬 신속하게 실행될 수 있다면, 이 접근법을 추천
- 예를 들면, Collections.sort() 메소드는 주어진 리스트를 정렬 전에 **배열**로 **복사**하고, 정렬에 실패해도 원래 리스트에는 손상이 없다.

#### 실패 원자성은 언제나 달성할 수는 없다.
 병렬프로그래밍에서 적절한 동기화 없이 동시에 접근할 경우, 객체 상태의 일관성은 깨질 수 있다. 그러니 `ConcurrentModificationException` 이 발생한 이후에 **객체 상태는 망가져 있으리라** 보는 것이 좋다.

 - 예외와 달리 **오류**는 **복구가 불가능**하며, 오류를 던지는 경우, 실패 원자성을 보존하려 **애쓸 필요가 없다.**

 > 명심할 규칙은, 메서드 명세에 포함된 **예외**가 발생하더라도 객체 상태는 메서드 호출 이전과 **동일**하게 유지해야함. 만약 이 규칙을 지키지 못할 경우, 객체상태가 어떻게 변하는지에 대한 내용을 **API 문서**에 명확하게 **기록**할 것.