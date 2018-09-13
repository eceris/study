# 규칙 21. 전략을 표현하려면 함수 객체를 사용하라
다른 객체에 작용하는 메서드, 인자로 전달된 객체에 뭔가를 하는 메소드를 정의하는 것이 가능하다.
그런 메소드를 갖고 있는 객체를 함수 객체<sup>function object</sup> 라고 부른다.
```java
class StringLengthComparator {
	public int compare(String s1, String s2) {
		return s1.length() - s2.length();
	}
}
```
StringLengthComparator 객체는 문자열을 비교하는데 사용될수 있는, 실행가능 전략<sup>concrete strategy</sup> 이다.
또한 실행가능 전략 클래스들이 대체로 그렇듯, 무상태<sup>stateless</sup>이다.

위의 기능을 싱글턴으로 처리하면 [쓸데없는 객체생성을 피할 수 있다.](rule3.md)
```java
class StringLengthComparator {
	private StringLengthComparator() {}
	public static final StringLengthComparator INSTANCE = new StringLengthComparator();
	public int compare(String s1, String s2) {
		return s1.length() - s2.length();
	}
}
```
StringLengthComparator 객체를 메서드에 전달하기 위해서는 인자의 자료형이 맞아야 하는데, 제네릭을 활용할 수 있다.

전략 인터페이스가 자료형인 public static 필드들을 갖는 호스트 클래스를 정의하는 방법이 있다.
```java
// 실행 가능 전략들을 외부에 공개하는 클래스
class Host {
	private static class StrLenCmp implements Comparator<String>, Serialisable {
		public int compare(String s1, String s2) {
			return s1.length() - s2.length();
		}
	}

	//비교자는 직렬화가 가능
	public static final Comparator<String> STRING_LENGTH_COMPARATOR = new StrLenCmp();
	...
}
```
String 클래스는 이 패턴을 사용하여 대소문자 구별없는 문자열 비교자를 **CASE_INSENSITVE_ORDER** 라는 필드로 공개하고 있다.

> 함수 객체의 주된 용도는 전략 패턴을 구현하는 것. 자바로 구현하기 위해서는 전략을 표현하는 인터페이스를 선언, 실행 가능 전략 클래스가 전부 해당 인터페이스를 구현하도록 한다. 그리고 자주 사용되는 것이라면 Singleton으로 객체를 외부에 공개.

