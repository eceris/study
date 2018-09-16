# 규칙 24. 무점검 경고<sup>unchecked warnings</sup> 를 제거하라

제네릭을 사용시 만나게 되는 아래 경고들을 코드의 안정성을 위해 가능하다면 없애자.

- 무점검 형변환 경고<sup>unchecked cast warning</sup>
- 무점검 메서도 호출 경고<sup>unchecked method invocation warning</sup>
- 무점검 제네릭 배열 생성 경고<sup>unchecked generic array creation warning</sup>
- 무점검 변환 경고<sup>unchecked conversion warning</sup>

#### 제거 할 수 없는 경고 메시지는 형 안정성이 보장될 경우에만 @SuppressWarning("unchecked") 어노테이션을 사용하여 억제할 수 있다.

- 가능한 작은 범위에 SuppressWarings 어노테이션을 붙일 것<sup>클래스 전체에 적용하지 말 것</sup>
- 한줄 이상인 메서드나 생성자에 사용된 SuppressWarnings는 지역 변수를 만들어서라도 지정하자.

```java
public <T> T[] toArray(T[] a) {
	if (a.length < size) {
		return (T[]) Arrays.copyOf(elements, size, a.getClass());
	}
	System.arraycopy(elements, 0, a, 0, size);
	if (a.length > size) {
		a[size] = null;
	}
	return a;
}
```
컴파일 할 결우 아래와 같이 경고 메시지 출력 되는데,
```
ArrayList.java:305: warning: [unchecked] unchecked cast
found: Object[], required: T[]
	return (T[]) Arrays.copyOf(elements, size, a.getClass());
```
지역 변수를 선언한 다음, 아래와 같이 변경할 수 있다. 
```java
// @SuppressWarnings 의 적용범위를 줄이기 위해 지역변수 사용
public <T> T[] toArray(T[] a) {
	if (a.length < size) {
	// 아래의 형 변환은 배열의 자료형이 인자로 전달된 자료형인 T[]와 같으므로 정확하다.
		@SuppressWarnings("unchecked") T[] result = (T[]) Arrays.copyOf(elements, size, a.getClass());
		return result;
	}
	System.arraycopy(elements, 0, a, 0, size);
	if (a.length > size) {
		a[size] = null;
	}
	return a;
}
```

- @SuppressWarnings("unchecked") 어노테이션을 사용할 때는 왜 형 안정성을 위반하지 않는지 주석을 달 것.

