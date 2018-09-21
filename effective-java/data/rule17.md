# 규칙 17. 계승을 위한 설계와 문서를 갖추거나, 그럴수 없다면 계승을 금지하라.

재정의 가능한 메서드를 내부적으로 어떻게 사용하는지 꼭 주석에 남길 것.
계승이 캡슐화의 원칙을 침해하기 때문에 반드시 문서에 구현 세부사항을 기록하자.

상속 받은 클래스의 내부동작에 개입 할 수 있는 훅을 신중하게 고른 후<sup>상속 받은 메소드를 호출해서 다른 메소드가 영향을 받을 수 있기 때문</sup>, protected 메소드 형태로 제공해야 한다.

#### 상속을 고려해서 클래스를 설계할 때 protected<sup>상속으로만 사용 가능한</sup>로 선언할 멤버는 어떻게 정함?
- 방법없음. 이문제를 푸는 마법탄환<sup>magic bullet</sup>은 없음.
- 열심히 신중하게 생각해서 하위 클래스를 코드로 만들고, 그것을 테스트 해봐야 함.

#### 생성자는 직접적이든 간접적이든 재정의 가능한 메소드를 호출해서는 안됨.
- 재정의한 메소드를 사용할 경우 하위클래스의 생성자보다 먼저 실행되서 코드가 깨진다.

예를 들어보자.

```java
public class Super {
	//생성자가 재정의 가능 메소드를 호출하는 잘못된 사례
	public Super() {
		overrideMe();
	}
	public void overrideMe() {}
}
```
위의 클래스를 상속 받아서 아래 코드를 실행하면,

```java
public final class Sub extends Super {
	private final Date date; //생성자가 초기화 하는 final 메소드

	Sub() {
		date = new Date();
	}

	@Override
	public void overrideMe() {
		System.out.println(date);
	}

	public static void main(String args[]) {
		Sub sub = new Sub();
		sub.overrideMe();
	}
}
```
아마도 날짜를 두번 호출 할 것이라 생각하겠지만, 실제로는 null, new Date() 가 출력 된다. 그리고 만약 overrideMe() 메소드에서 Date의 메소드를 호출하면 NPE<sup>null pointer exception</sup> 이 발생 할 것 이다.

#### Cloneable 이나 Serializable 인터페이스를 사용할 경우에는 더 까다롭다. 
- clone 이나 readObject 메소드 안에서 재정의 가능한 메소드를 호출하지 않도록 해야한다.(역 직렬화 과정에서 실행되어 오류를 일으킴.)

### 계승에 맞게 설계하고 문서화 하지 않을 꺼면 상속하지도 마라!!!

#### 하위 클래스의 생성을 막는 방법
- 클래스를 final 로 선언하는 방법
- 모든 생성자를 private나 package-protected로 선언하고 생성자 대신 public static 팩토리 메소드를 추가.

