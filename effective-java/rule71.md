# 규칙 71. 초기화 지연은 신중하게 해라

초기화 지연<sup>lazy initialization</sup>은 필드 초기화를 실제로 그 값이 쓰일 때까지 미루는 것. 아주 기본적인 최적화 기법이며, 클래스나 객체의 초기화 과정에서 발생하는 해로운 순환성<sup>circularity</sup>을 해소하기 위해서도 사용.

## 정말로 필요하지 않으면 하지마라
- 초기화 지연은 클래스를 초기화와 객체 생성의 비용은 줄이지만, 필드의 사용 비용은 증가시킨다. -> 양날의 검이다.
- 초기화 지연이 필요한 곳은 필드 사용빈도가 낮고 초기화 비용이 높으면 쓸만하다.

## 대부분 지연된 초기화를 하느니 일반 초기화를 하는 편이 낫다
- 다중 스레드 환경에서 초기화 지연 기법 구현은 까다로움. 
- [적절한 동기화가 되지 않으면, 심각한 버그를 유발](rule66.md)
```java
//객체 필드를 초기화하는 일반적인 방법
private final FieldType field = computeFieldValue();
```

## 초기화 순환성 문제를 해서하기 위해서 초기화 지연을 사용할 경우, 동기화된 접근자를 사용하라
- 가장 간단하고 안전한 방법이 아래의 예이다.
```java
//동기화된 접근자를 사용한 객체 필드 초기화 지연 방법
private FieldType field;

synchronized FieldType getField() {
	if (field == null) {
		field = computeFieldValue();
	}
	return field;
}
```

## 성능 문제 때문에 정적 필드 초기화를 지연시키고 싶을 때는 초기화지연담당 클래스 숙어<sup>lazy initialization holder class</sup>를 적용하라
- 요청 기반 초기화 담당 클래스 숙어<sup>initialize-on-demand hodler class</sup>라고도 하는데, 실제로 사용되는 순간 초기화 됨.
```java
//정적 필드에 대한 초기화 지연 담당 클래스 숙어
private static class FieldHolder {
	static final FieldType field = computeFieldValue();
}
static FieldType getField() { return FieldType.field; }
```
- getField() 메소드에 동기화를 하지 않았기에 초기화 지연을 하더라도 메서드 비용이 증가하지 않음.

## 성능 문제 때문에 객체 필드 초기화를 지연시키고 싶다면 이중검사<sup>double-check</sup> 숙어를 적용하라
- [이 숙어를 사용하면 초기화가 끝난 필드를 이용하기 위해 락을 걸지 않아도 된다.](rule67.md)
- 이미 초기화된 필드에는 락을 걸지 않으므로, 필드는 반드시 **volatile**로 선언해야함.
```java
//이중 검사 패턴을 통해 객체 필드 초기화를 지연시키는 숙어
private volatile FieldType field;

FieldType getField() {
	FieldType result = field;
	if (result == null) { // 첫번째 검사(락 없음)
		synchronized(this) {
			result = field;
			if (result == null) { // 두번째 검사(락)
				field = result = computeFieldValue();
		}
	}
	return result;
}
```

## 여러번 초기화되어도 상관없는 객체 필드 초기화를 지연시키고 싶을때는 단일 검사 숙어<sup>single-check</sup>를 적용하라
- 이중 검사 숙어의 두번째 검사는 없애버려도 될 것이다.
```java
private volatile FieldType field;

private FieldType getField() {
	FieldType result = field;
	if (result == null) { // 두번째 검사(락)
		field = result = computeFieldValue();
}
```

> 대부분의 **필드 초기화**는 **지연시키지 않아야 한다.** 성능을 위하거나 해로운 초기화 순환성을 제거할 목적이라면 적절한 초기화 지연 기술을 이용하라.