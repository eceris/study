# 규칙 31. ordinal 대신 객체 필드를 사용하라.

ordinal() 메소드는 enum 자료형 안에서 상수의 위치를 나타내는 정수를 반환하는데, 이걸 잘 사용하려는 생각은 하지 말자.
아주 좋지 않은 예가 아래 있음.

```java
//ordinal 을 남용한 사례 - 따라하면 곤란
public enum Ensemble {
	SOLO, DUET, TRIO, QUARTET, QUINTET, SEXTET, SEPTET, OCTET, NONET, DECTET;

	public int numberOfMusicians() {
		return ordinal() + 1;
	}
}
```
동작은 하지만, 상수의 위치만 변경해도 상태가 깨진다. 유지보수 측면에서 좋지 않음.
만약 복사중주단<sup>OCTET과 마찬가지로 8명</sup> 이 추가 되어야 한다면? 해결할 수 없다.

#### enum 상수에 연계되는 값을 ordinal()로 표현하지 말고, 객체 필드<sup>instance Field</sup> 에 저장하자.
```java
public enum Ensemble {
	SOLO(1), DUET(2), TRIO(3), QUARTET(4), QUINTET(5), SEXTET(6), SEPTET(7), OCTET(8), NONET(9), DECTET(10);

	private final int numberOfMusicians;
	Ensemble(int size) { this.numberOfMusicians = size; } 
	public int numberOfMusicians() {
		return numberOfMusicians;
	}
}
```

> 자바의 enum 명세에도 일반적인 개발자는 ordinal()을 사용할 일이 없다고 했다. EnumMap이나 EnumSet 처럼 일반적인 용도의 enum 기반 자료구조에서 사용할 목적으로 설계한 메소드. 그러니 ordinal() 메소드는 사용하지 않는 것이 좋다.

