# 규칙 25. 배열 대신 리스트를 써라

## 제네릭은 배열과 두가지 중요한 차이점을 갖고 있다. 
#### 배열은 공변 자료형<sup>covariant</sup>, 제네릭은 불변 자료형<sup>invariant</sup>
Sub가 Super의 하위 자료형<sup>subtype</sup>이라면 Sub[]도 Super[]의 하위 자료형, 그러나 List<Type1\>은 List<Type2\>의 상위 자료형이나 하위 자료형이 될 수 없고 그것이 런타임에 결정된다.

예를 보자
```java
//실행중에 문제를 일으킴
Object[] objectArray = new Long[1];
objectArray[0] = "I don't fit in"; //ArrayStoreException 발생
```
하지만 아래는 컴파일도 되지 않는다.
```java
// 컴파일 되지 않는 코드
List<Object> ol = new ArrayList<Long>(); //자료형 불일치
ol.add("I dont't fit in");
```
컴파일 시점에 문제를 발견하는 것이 더 낫다.

#### 배열은 실체화<sup>reification</sup> 되는 자료형이다
배열의 각 원소 자료형은 런타임에 결정.
반면에 제네릭은 삭제<sup>erasure</sup> 과정을 통해 구현
- 자료형에 관련된 조건들은 컴파일 시점에만 적용, 런타임에 각 원소의 자료형 정보는 삭제

```
자료형 삭제<sup>erasure</sup> 덕에, [제네릭 자료형은 제네릭을 사용하지 않고 작성된 오래된 코드와 연동된다.](rule23.md)
```

제네릭 배열 생성 오류에 대한 가장 좋은 해결책은 E[] 대신 List<E> 를 쓰는 것.<sup>성능이 떨어지고 코드가 길어지지만, 형안정성과 호환성은 좋아짐</sup>

```java
static Object reduce(List list, Function f, Object initVal) {
	synchronized(list) {
		Object result = initVal;
		for (Object o : list) {
			result = f.apply(result, o);
		}
		return result;
	}
}
```
[동기화<sup>synchronized</sup> 영역 안에서 불가해 메소드<sup>alien method</sup> 를 호출하면 안됨](rule67.md)
내부적으로 lock을 걸고 리스트를 복사하고, 할수 있는 방법이 있다. 하지만, 리스트로 선언하는게 간단하다.
```java
static <E> E reduce(List<E> list, Function<E> f, E initVal) {
	List<E> snapshot;
	synchronized(list) {
		snapshot = new ArrayList<E>(list);
	}
	E result = initVal;
	for (E e : snapshot) {
		result = f.apply(result, e);
	}
	return result;
}
```

> 제네릭은 컴파일 타임에 형안정성을 보장하지만, 배열은 그렇지 않다. 배열과 제네릭을 혼용하는 것은 좋지 않은 습관이며, 만약 그렇게 하던중 컴파일 오류나 경고 메시지를 만나면, 배열을 리스트로 바꿔야겠다는 생각을 하라.
