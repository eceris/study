# 규칙 3. private 생성자나 enum 자료형은 싱글턴 패턴을 따르도록 설계할 것.

클래스를 싱글턴으로 만들면 클라이언트를 테스트하기가 어려워질수 있다????(뭔 말인지 모르겠음 ..솔직히..)
생성자를 private로 선언해도 아래와 같이 클라이언트가 리플렉션을 이용해 private 생성자를 호출 할 수 있다.
```java
public class PrivateInvoker {
	public static void main(String [] args) throws Exception {
		// 리플렉션과 setAccessible() 메소드를 통해 private 로선언된 생성자의 호출 권한을 획득
		Constrctor<?> con - Private.class.getDeclaredConstructors()[0];
		con.setAccessible(true);
		Private p = (Private) con.newInstance();
	}
}

class Private {
	private Private() {
		System.out.println("hi!");
	}
}
```
싱글턴을 구현할 때는 아래와 같이 enum 자료형을 이용할 수 있다. 
```java
public enum Elvis {
	INSTATNCE;
	public void leaveTheBuilding() {...}
}
```
### 장점
1. 간결하고 직렬화가 자동으로 처리된다.
2. 직렬화가 아무리 복잡하게 이루어져도 여러 객체가 생길 일이 없으며, 리플렉션을 통한 공격에도 안전하다. 

> 원소가 하나 뿐인 enum 자료형이야 말로 싱글턴을 구현하는 가장 좋은 방법이다.

ㄴ