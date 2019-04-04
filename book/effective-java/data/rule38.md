# 규칙 38. 인자의 유효성을 검사하라

대부분의 메소드나 생성자는 파라미터로 사용할 수 있는 값을 제한<sup>index값은 음수가 아니거나, 객체 참조는 null이 될 수 없다거나</sup>
이러한 제한은 문서로 남기거나 **메서드 시작 부분**에 검사해야 한다.

#### public 메소드라면 인자가 유효하지 않을 경우에 javadoc을 이용하자
유효하지 않은 인자일 경우 javadoc 의 [@throws 태그를 이용해 문서화하라](data/rule62.md)
[보통 IllegalArgumentException, IndexOutOfBoundException, NullPointerException 을 이용.](data/rule60.md)
예제를 보자
```java
/*
*	(this mod m)인 BigInteger 값 반환. 이메서드는 remainder 메서드와는 다르다.
*	remainder 메서드는 항상 음수 아닌 BigInteger 객체를 반환한다.
*	
*	@param mod 연산을 수행할 값. 반드시 양수.
*	@return this mod m
*	@throws ArithmeticException(m <= 0 일때)
*/
public BigInteger mod(BigInteger m) {
	if (m.signum() <= 0) {
		throw new ArithmeticException("Modulus <= 0: " + m);
	}
	... //계산 수행 
}
```

#### public 이 아닌 메소드라면 assertion을 사용하자
public 이 아닌 메소드라면 확증문<sup>assertion</sup>을 사용한다. 아래의 예제를 보자.

```java
// 재귀적으로 정렬하는 private helper 함수
public static void sort(long[] a, int offset, int length) {
	assert a != null;
	assert offset >= 0 && offset <= a.length;
	assert length >= 0 && length <= a.length - offset;
	... //계산 수행 =
}
```
확증문은 패키지가 어떻게 이용하건 확증 조건<sup>asserted condition</sup> 은 항상 참이 되어야 한다 고 주장하는 것. 만족하지 않을 경우 AssertionError 발생.

#### 생성자는 나중을 위해 보관될 인자의 유효성을 반드시 검사해야 한다 
클래스 불변식<sup>invariant</sup> 를 위반하는 객체가 만들어지는 것을 막기 위해 생성자에 전달되는 인자의 유효성을 반드시 검사하자

주의할 것은, Collections.sort()를 사용할때 내부적으로는 객체 비교간 암묵적인 유효성검사를 하는데, 이런 암묵적 유효성검사에 의지하면 [실패원자성](data/rule64.md)<sup>failure atomicity</sup> 을 잃게 된다.

> 메서드나 생성자를 구현할 때는 받을수 있는 인자에 제한이 있는지 따져보고, 있을 경우 그 사실을 문서에 남기고, 메서드 앞부분에서 검사하자.
