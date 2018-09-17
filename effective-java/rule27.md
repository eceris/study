# 규칙 27. 가능하면 제네릭 메소드로 만들 것
static 유틸리티 메서드는 제네릭화<sup>generification</sup> 하기 좋은 후보다.<sup>Collections에 구현된 모든 알고리즘(binarySearch, sort...)은 제네릭으로 구현</sup>

제네릭으로 바꿔보는 연습을 해보자
```java
public static Set union(Set s1, Set s2) {
	Set result = new HashSet(s1);
	result.addAll(s2);
	return result;
}
```
컴파일이 되긴하는데 경고가 뜬다. 제네릭으로 바꿔보자
```java
public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
	Set<E> result = new HashSet<E>(s1);
	result.addAll(s2);
	return result;
}
```
형인자를 선언하는 형인자 목록<sup>type parameter list</sup>은 메소드의 수정자와 반환값 자료형 사이에 둔다. 여기에서 형인자 목록은 <E\> 이다.

제네릭 싱글턴 패턴<sup>generic singleton pattern</sup>
```java
private static UnaryFunction<Object> IDENTITY_FUNCTION =
	new UnaryFunction<Object> () {
		public Object apply(Object arg) { return arg; }
	};

// IDENTITY_FUNCTION 은 무상태 객체, 형인자는 비한정인자(unbounded) 이므로 모든 자료형이 같은 객체를 공유해도 안전하다.
@SuppressWarnings
public static <T> UnaryFunction<T> identityFunction() {
	return (UnaryFunction<T>) IDENTITY_FUNCTION;
}
```

재귀적 자료형 한정<sup>recursive type bound</sup>
```java
public static <T extends Comparable<T>> T max(List<T> list) {
	Iterator<T> i = list.iterator();
	T result = i.next();
	while(i.hasNext()) {
		T t = i.next();
		if (t.compareTo(result) > 0)
			result = t;
	}
	return result;
}
```
위 코드에 사용된 자료형 한정 <T extends Comparable<T\>\> 는 `자기 자신과 비교가능한 모든 자료형 T` 라는 뜻.

> 클라이언트가 입력값과 반환값의 형변환을 해야한다면 제네릭 메소드를 사용하여 형변환 없이 사용할 수 있을지 살펴 볼것.
