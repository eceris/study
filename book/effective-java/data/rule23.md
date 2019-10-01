# 규칙 23. raw type<sup>무인자 자료형</sup>을 사용하지 마라.

새 코드에는 raw type<sup>무인자 제네릭 자료형</sup>을 사용하지 마라
선언부에 **형인자**<sup>type parameter</sup> 가 포함된 클래스나 인터페이스는 **제네릭 클래스나 인터페이스**로 부름.
제네릭 클래스와 인터페이스는 **제네릭 자료형**이라 부른다.
각 제네릭 자료형은 형인자 자료형<sup>parameterized type</sup> 집합을 정의하고, 집합은 이름 뒤에 다이아몬드 로 감싼 실 형인자<sup>actual type parameter</sup> 목록이 붙은 클래스나 인터페이스들로 구성되는데, 이 실 인자들은 제네릭 자료형의 형식 형인자<sup>formal type parameter</sup> 각각에 대응된다.
마지막, 각 제네릭 자료형은 새로운 무인자 자료형<sup>raw type</sup>을 정의하는데, 실 형인자 없이 사용되는 자료형이다.
아래의 표에서 확인.


| 용어 | 예 | 규칙 |
|:--------|:--------|:--------|
| parameterized type<sup>형인자 자료형</sup> | List<String\> | [23](rule23.md) |
| actual type parameter<sup>실 형인자</sup> | String | [23](rule23.md) |
| generic type<sup>제네릭 자료형</sup> | List<E\> | [23](rule23.md), [26](rule26.md)  |
| formal type parameter<sup>형식 형인자</sup> | E | [23](rule23.md) |
| unbounded wildcard<sup>비한정적 와일드카드 자료형</sup> | List<?\> | [23](rule23.md) |
| raw type<sup>무인자 자료형</sup> | List | [23](rule23.md) |
| bounded type parameter<sup>한정적 형인자</sup> | <E extends Number\> | [26](rule26.md) |
| recursive type bound<sup>재귀적 형 한정</sup> | <T extends Comparable<T\>\> | [27](rule27.md) |
| bounded wildcard type<sup>한정적 와일드카드 자료형</sup> | List<? extends Number\> | [28](rule28.md) |
| generic method<sup>제네릭 메서드</sup> | static <E> List<E> asList(E[] a) | [27](rule27.md) |
| type token<sup>자료형 토큰</sup> | String.class | [29](rule29.md) |


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

**제네릭 자료형**을 쓰고 싶으나 **실제 형 인자**가 무엇인지는 모르거나 신경쓰지 않고 싶으면 형인자로 **`?`** 를 사용하여 unbounded wildcard type<sup>비한정적 와일드카드 자료형</sup>이라는 안전한 방법을 사용할 것.

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







## 작성중


선언시에 type parameter<sup>형인자</sup>가 포함된 클래스나 인터페이스는 **제네릭 클래스나 인터페이스**로 부름.
예를 들면 `List`의 경우 하나의 타입 파라미터를 갖는데 이것은 엘리먼트의 타입을 드러낸다. 

```java	
List<E> list;
```

인터페이스의 풀네임은 List<E> <sup>E의 리스트</sup> 인데 사람들은 그냥 줄여서 List 라고 부른다. 제네릭 클래스와 인터페이스들은 집합적으로 제네릭 타입이라고 불린다.

각각의 제네릭타입은 paraemterized type 의 집합으로 정의되고, 제네릭 타입의 formal type parameter에 해당하는 actual type parameter가 꺽쇠 `<`로 감싸져 구성된다.  예를 들면 List<String> <sup>String 의 List</sup> 는 List의 엘리먼트가 String 이라는 것을 드러내는 parameterized type 이다. (String은 formal type parameter E 에 해당하는 actual type parameter 이다.)

마지막으로, 각가의 제네릭 타입은 raw type 을 정의하는데, 이것은 수반되는 type parameter 없이 사용되는 제네릭 타입이다. 예를 들면, List<E>에 해당하는 raw type 은 List 이다. Raw type들은 모든 제네릭 타입 정보가 타입 선언에서 지워진 경우 그냥 Raw type 처럼 행동한다. 그것들은 보통 제네릭 이전의 코드와 호환성을 위해 존재한다.


제네릭이 자바에 추가되기 전에는 모범적인 사례였으나, Java 9에서도 사용할수 있긴 하지만 모범적인 사례는 아니다.
```
private final Collection stamps = ... ;
```
위와 같이 사용할 경우 stamp가 아닌 다른 객체를 stamps에 add 할 수 있다. 이것은 실제로 런타입에 에러를 발생 시킨다.



