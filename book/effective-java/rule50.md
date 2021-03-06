# 규칙 50. 다른 자료형이 적절하다면 문자열 사용은 피하라
문자열은 **텍스트 표현**과 **처리**에 걸맞도록 설계되었다.

### 문자열은 값 자료형<sup>value type</sup> 을 대신하기에는 부족하다.

숫자라면 int, float, BigInteger 와 같은 **수 자료형**<sup>numeric type</sup>으로 변환해야 한다. 예 아니오를 묻는 질문의 답이라면 boolean으로 변환해야 한다. 

적당한 자료형을 찾을 수 없다면 **새로 만들더라도 적절한 자료형**을 사용하자. 

### 문자열은 enum 자료형을 대신하기에는 부족하다.
[enum](rule30.md)은 문자열보다 훨씬 좋은 열거 자료형 상수<sup>enumerated type constant</sup> 를 만들어 낸다.

### 문자열은 혼합 자료형<sup>aggregated type</sup> 을 대신하기에는 부족하다.
여러 컴포넌트가 있는 개체를 문자열로 표현하는 것은 좋지 않다. 아래 예가 좋지않은 구현이다.
```java
// 문자열을 혼합 자료형으로 써먹은, 부적절한 사례
String compoundKey = className + "#" + i.next();
```
이런 접근법에는 여러 문제가 있는데,

1. **필드 구분자**<sup>여기서는 `#`</sup> 가 필드로 들어가면 문제가 발생한다.
2. 각 필드를 사용하려면 항상 문자열을 `parsing` 해야하는데 이것은 **느릴뿐** 아니라, **멍청**하고 **오류**가 발생할 여지가 많다.
3. equals, toString, compareTo 와 같은 메소드를 사용하지 못하고 단순히 String에서 제공하는 기능만 사용해야 한다.

혼합 자료형을 표현할 클래스는 종종 [**private static 멤버 클래스**로 구현한다.](rule22.md)

#### 문자열은 권한<sup>capability</sup> 을 표현하기에는 부족하다. 

아래의 예를 보이기 위해 **ThreadLocal**을 예제로 사용함.

1. 악의적인 목적으로 권한 정보를 제공하는 문자열을 변경하기가 쉽다.
2. 유일성을보장하기가 쉽지 않다. 

> 더 좋은 **자료형**이 있거나 만들 수 있을 때는 객체를 문자열로 표현하는 것은 피하라. 문자열은 유연성이 떨어지고 느리고, 문제가 발생할 여지가 많다. 문자열이 적합하지 못한 자료형으로는 **기본자료형, enum, 혼합자료형** 등이 있다