# 규칙 28. 한정적 와일드카드를 써서 API 유연성을 높여라

형인자<sup>parameterized type</sup> 자료형은 불변형<sup>invariant</sup> 자료형. Type1과 Type2 가 있을 때, List<Type1\> 과 List<Type2\> 사이에는 어떠한 상위-하위 자료형 관계가 성립하지 않음.

한정적 와일드카드 자료형<sup>bounded wildcard type</sup>을 사용하는 예를 보자.
```java
// 와일드카드 자료형을 사용하지 않는 pushAll 메소드 - 문제가 있다.
public void pushAll(Iterable<E> src) {
	for (E e : src) {
		push(e);
	}
}
```
위의 코드를 아래와 같이 사용하면 에러가 난다.
```java
Stack<Number> numberStack = new Stack<>();
Iterable<Integer> integers = ...;
numberStack.pushAll(integers);
```
에러는 ...
```
pushAll(Iterable<Number>) in Stack<Number> cannot be applied to (Iterable<Integer>) 
```

이것을 해결하기 위해 한정적 와일드카드 자료형<sup>bounded wildcard type</sup>을 사용하여 pushAll의 인자 자료형을 `E 의 Iterable` 이 아니라 `E의 하위자료형의 Iterable` 이라고 명시해야 한다.
```
Iterable<? extends E>
```
한정적 와일드카드 자료형을 사용하여 고치면 ...
```java
// E 객체 생산자 역할을 하는 인자에 대한 와일드 카드 자료형
public void pushAll(Iterable<? extends E> src) {
	for (E e : src) {
		push(e);
	}
}
```

이번엔 popAll도 바꾸어보자
```java
// 와일드 카드 자료형 없이 구현한 popAll 메소드 - 문제가 있다.
public void popAll(Collection<E> dst) {
	while( !isEmpty() ) {
		dst.add(pop());
	}
}
```
위의 코드도 앞에서와 비슷하게 Collection<Object\> 가 Collection<Number\> 의 하위자료형이 아니라는 에러가 나오는데 아래와 같이 와일드카드 자료형을 사용하여 변경해보자.

```java
//E 의 소비자 역할을 하는 인자에 대한 와일드카드 자료형
public void popAll(Collection<? super E> dst) {
	while( !isEmpty() ) {
		dst.add(pop());
	}
}
```

> 유연성을 최대화 하려면, 객체 생산자<sup>producer</sup> 나 소비자<sup>consumer</sup> 역할을 하는 메서드 인자의 자료형은 와일드카드 자료형으로 할것. 외우기 어렵다면 

> \- PECS(Produce - Extends, Consumer - Super) T가 데이터를 생산하는 역할이라면 extends를, T가 데이터를 소비하는 역할이라면 super 를 사용하자.


반환값에는 와일드카드 자료형을 쓰지 말 것.

Comparable<T\> 대신에 Comparable<? super T\> 를 쓰는 것을 권장한다. Comparator<T\> 대신에 Comparator<? super T\> 를 쓰는 것을 권장

컴파일러가 타입이 뭔지 추론해야 하는 상황이 오기도 하는데 해결책은 세가지.... 

1. generic T로 바꾸는 방법
2. 일종의 helper 메소드를 만들어서 capture 할수 있도록 하는 방법
3. raw type으로 하는 방법

2번을 권장하는데 예를 들어보면
```java
public static void swap(List<?> list, int i, int j) {
	swapHelper(list, i, j);
}

public static <E> void swapHelper(List<E> list, int i, int j) {
	list.set(i, list.set(j, list.get(i)));
}
```
이렇게 함으로써 swapHelper 메소드는 list가 list<E\> 라는 것을 알게<sup>capture</sup> 된다.

> 까다롭긴 하지만, API에는 와일드카드 자료형을 쓸 것. 와일드 카드 자료형을 적절히 사용하는 것은 필수다. `PECS` 를 잊지말고, 모든 Comparable과 Comparator 는 소비자라는 것을 기억.

참고로 아래 링크에서 더 재밌는 것들을 설명

- [토비의 봄 TV 4회 (1) 자바 Generics](https://youtu.be/ipT2XG1SHtQ)
- [토비의 봄 TV 4회 (2) Generics에서 와일드카드 활용법, 람다와 인터섹션 타입을 이용한 동적인 기능확장법](https://youtu.be/PQ58n0hk7DI)




