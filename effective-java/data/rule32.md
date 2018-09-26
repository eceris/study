# 규칙 32. 비트 필드 대신 EnumSet을 사용하라

열거 자료형 원소들이 주로 집합에 사용될 경우, 전통적으로 int enum 패턴을 아래처럼 사용했다.

```java
//비트 필드 열거형 상수 - 이제는 피해야 할 구현법
public class Text {
	public static final int STYLE_BOLD				= 1 << 0; // 1
	public static final int STYLE_ITALIC			= 1 << 1; // 2
	public static final int STYLE_UNDERLINE			= 1 << 2; // 4
	public static final int STYLE_STRIKETHROUGH		= 1 << 3; // 8

	// 이메소드의 인자는 STYLE 상수를 비트별 OR 한 값이거나 0.
	public void applyStyle(int style) { ... }
}
```
이렇게 하면 상수들을 집합에 넣을 때 비트별 OR 연산을 할 수 있다.
```java
text.applyStyle(STYLE_BOLD | STYLE_ITALIC);
```

이러한 연산을 할 수 있는 더 좋은 방법이 있다. EnumSet은 특정한 enum 자료형의 값으로 구성된 집합을 효율적으로 표현.

더 짧고 간결하고, 안전하다.
```java
// EnumSet - 비트필드를 대신할 현대적 기술
public class Text {
	public enum Style { BOLD, ITALIC, UNDERLINE, STRIKETHROUGH }
	//어떤 Set 객체도 인자로 전달할 수 있으나, EnumSet이 분명 최선
	public void applyStyle(Set<Style> style) { ... }
}
```

클라이언트 코드는 아래와 같다.
```java
text.applyStyle(EnumSet.of(Style.BOLD, Style.ITALIC));
```
메소드를 호출하는 클라이언트는 항상 인터페이스를 자료형으로 사용하는 것이 낫다. 

> 열거 자료형을 집합에 사용해야 한다고 해서 비트 필드로 표현하면 곤란하다. EnumSet 클래스는 간결하고 우수하다. 
