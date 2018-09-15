# 규칙 23. 새 코드에는 무인자 제네릭 자료형을 사용하지 마라

선언부에 **형인자**<sup>type parameter</sup> 가 포함된 클래스나 인터페이스는 **제네릭 클래스나 인터페이스**로 부름.
제네릭 클래스와 인터페이스는 **제네릭 자료형**이라 부른다.
각 제네릭 자료형은 형인자 자료형<sup>parameterized type</sup> 집합을 정의하고, 집합은 이름 뒤에 다이아몬드 로 감싼 실 형인자<sup>actual type parameter</sup> 목록이 붙은 클래스나 인터페이스들로 구성되는데, 이 실 인자들은 제네릭 자료형의 형식 형인자<sup>formal type parameter</sup> 각각에 대응된다.
마지막, 각 제네릭 자료형은 새로운 무인자 자료형<sup>raw type</sup>을 정의하는데, 실 형인자 없이 사용되는 자료형이다.
아래의 표에서 확인.


| 용어 | 예 | 규칙 |
|:--------|:--------|:--------|
| 형인자 자료형<sup>parameterized type</sup> | List<String\> | [23](rule23.md) |
| 실 형인자<sup>actual type parameter</sup> | String | [23](rule23.md) |
| 제네릭 자료형<sup>generic type</sup> | List<E\> | [23](rule23.md), [26](rule26.md)  |
| 형식 형인자<sup>formal type parameter</sup> | E | [23](rule23.md) |
| 비한정적 와일드카드 자료형<sup>unbounded wildcard</sup> | List<?\> | [23](rule23.md) |
| 무인자 자료형<sup>raw type</sup> | List | [23](rule23.md) |
| 한정적 형인자<sup>bounded type parameter</sup> | <E extends Number\> | [26](rule26.md) |
| 재귀적 형 한정<sup>recursive type bound</sup> | <T extends Comparable<T\>\> | [27](rule27.md) |
| 한정적 와일드카드 자료형<sup>bounded wildcard type</sup> | List<? extends Number\> | [28](rule28.md) |
| 제네릭 메서드<sup>generic method</sup> | static <E> List<E> asList(E[] a) | [27](rule27.md) |
| 자료형 토큰<sup>type token</sup> | String.class | [29](rule29.md) |


무인자 자료형을 쓰면 **형 안전성**이 사라지고, 제네릭의 장점 중 하나인 표현력<sup>expressiveness</sup> 측면에서 손해를 봄.

#### 근데 왜 무인자 자료형을 사용하나??? 
이전 호환성<sup>migration compatibility</sup> 때문에...

예를 보면
```java
//실행도중에 오류를 일으키는 무인자 자료형(List) 사용 예
public static void main(String[] args) {
	List<String> strings = new ArrayList<String> ();
	unsafeAdd(strings, new Integer(42));
	String s = strings.get(0); //컴퍼일러가 자동으로 형변환
}

private static void unsafeAdd(List list, Object o) {
	list.add(o);
}
```
컴파일은 잘 되지만 무인자 자료형 list 를 사용했기 때문에 경고가 뜸.
```
warning: unchecked call to add(E) in raw type List
list.add(o);
```
실제로 돌려보면 런타임에 ClassCastException 예외가 발생, 그러니 사용하면 안됨!!!

**제네릭 자료형**을 쓰고 싶으나 **실제 형 인자**가 무엇인지는 모르거나 신경쓰지 않고 싶으면 형인자로 **`?`** 를 사용하여 비한정적 와일드카드 자료형<sup>unbounded wildcard type</sup>이라는 안전한 방법을 사용할 것.

#### 비한정적 와일드 카드 자료형 Set<?> 과 무인자 자료형 Set의 차이
1. 와일드카드 자료형은 안전하지만 무인자 자료형은 그렇지 않다.
2. 무인자 자료형 컬렉션은 아무 객체나 넣을수 있어서, 자료형 불변식(type invariant) 이 쉽게 깨진다.

#### 새로 작성하는 코드에도 무인자 자료형을 쓰는 경우가 있다. 
1. 클래스 리터럴에는 무인자 자료형을 사용해야 하는데, List.class, String[].class, int.class 는 사용가능.
2. instanceof 연산자는 비한정적 와일드카드 자료형 이외의 형인자 자료형에 적용 불가.  아래처럼 하는 것이 좋다. 
```java
// instanceof 연산자에는 무인자 자료형을 써도 OK
if (o instanceof Set) {	//무인자 자료형
	Set<?> m = (Set<?>) o; //와일드카드 자료형
}
```

> 무인자 자료형을 쓰면 런타임에 예외가 발생할 수 있으므로 사용하지 말것. 정리하면, Set<Object> 는 어떤 자료형의 객체도 담을 수 있는 집합이 형인자 자료형, Set<?> 는 모종의 자료형 객체만 담을 수 있는 집합을 표현하는 와일드카드 자료형이다. 
