# 규칙 49. 객체화된 기본 자료형 대신 기본 자료형을 이용하라.

자바에는 int, double, boolean 등의 **기본 자료형**<sup>primitive type</sup>과 모든 기본 자료형에 대응되는 참조자료형이 있는데 각각 Integer, Double, Boolean 과 같은 **객체화된 기본 자료형**<sup>boxed primitive type</sup>이 있다.

1.5 부터 **자동객체화**<sup>autoboxing</sup>과 **자동 비객체화**<sup>auth-unboxing</sup> 가 언어의 일부가 되어 기본 자료형과 객체화된 기본 자료형의 **차이를 희미하게** 만들었다. 

### 기본 자료형과 객체화된 기본 자료형은 세가지 차이점이 있다.
1. 기본자료형은 값만 가지지만 **객체화된 기본자료형**은 `id`를 갖는다. 객체화된 기본 자료형은 값은 같아도 id가 다를 수 있다.
2. 기본 자료형에 저장되는 값은 기본적으로 **완전한 값**<sup>fully functional value</sup> 이지만, 객체화된 기본 자료형은 그외에 아무런 값이 없는 **null**값이 존재한다.
3. 기본자료형은 **시간**이나 **공간**적인 측면에서 **효율적이다**.

### 객체화된 기본 자료형에 `==` 연산자를 사용하는 것은 거의 항상 오류이다.
아래 코드를 보자.
```java
Comparator<Integer> naturalOrder = new Comparator<> () {
	public int compare(Integer first, Integer second) {
		return first < second ? -1 : (first == second) ? 0 : 1;
	}
}
```
위의 함수는 항상 오동작 하는데 `==` 연산자가 **객체참조** 를 통해 두 객체의 id를 비교하기 때문이다. 
문제가 되는 **신원비교**<sup>idntity comparison</sup> 을 피하기 위해 아래와 같이 만들 수 있다. 
```java
Comparator<Integer> naturalOrder = new Comparator<> () {
	public int compare(Integer first, Integer second) {
		int f = first; // auto-unboxing
		int s = second; // auto-unboxing
		return f < s ? -1 : (f == s) ? 0 : 1; // no boxing
	}
}
```

### 기본 자료형과 객체화된 기본 자료형을 한 연산 안에서 같이 사용할 경우 객체화된 기본 자료형은 기본 자료형으로 변환된다.

객체화된 기본 자료형이 **auto-unboxing** 되어 기본자료형으로 변환된다는 말이다.
하지만 이렇게 반복적으로 auto-boxing, auto-unboxing이 사용될 경우 성능이 느려진다. 

### 그러면 객체화된 기본 자료형은 언제 써야되???

#### 컬렉션의 요소, 키, 값으로 사용할 때
컬력센에서는 기본 자료형을 넣을 수 없으므로 객체화된 기본 자료형을 써야한다.

#### 형인자 자료형<sup>parameterized type</sup> 의 형인자로는 기본 자료형을 사용할 수 없다
예를 들면 `ThreadLocal<int>` 는 사용불가, `ThreadLocal<Integer>` 는 사용 가능.

#### 리플렉션을 통해 메서드를 호출 때

> 가능하다면 **기본자료형**을 사용하라. 더 단순하고 빠르다. 객체화된 기본 자료형을 `==` 연산자로 비교하는 것은 두 객체의 id를 비교한다는 것. 기본자료형을 객체화된 기본 자료형으로 변환하는 **auto-boxing**이 자주 발생할 경우 **성능에 문제**가 발생할 수 있다.

