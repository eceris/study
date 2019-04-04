# 규칙 5. 불필요한 객체는 만들지 말 것.
객체를 재사용하는 프로그램은 우아하고 빠르다. [`변경 불가능`한 객체는 언제든지 재사용할 수 있다.](rule15.md)

Boolean(String) 보다는 Boolean.valueOf(String) 을 사용하자.

그렇다고 아래와 같이 하면 안된다. 
```java
public class Person {
	private final Date birthDate;

	public boolean isBabyBoomer() {
		Calendar gmtcal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		gmtcal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
		Date boomStart = gmt.getTime();
		gmtcal.set(1965 Calendar.JANUARY, 1, 0, 0, 0);
		Date boomEnd = gmt.getTime();
		return birthDate.compareTo(boomStart) >= 0 && birthDate.compareTo(boomEnd) < 0;
	}
}
```
위의 isBabyBoomer() 메소드는 호출될 때마다 Calendar 객체 하나, TimeZone 객체 하나, 그리고 Date 객체 두개를 쓸데 없이 만들어 낸다. 이런 코드는 정적 초기화 블록<sup>static initializer</sup> 을 통해 개선하자.

```java
public class Person {
	private final Date birthDate;

	//다른 필드와 메서드, 생성자는 생략!

	private static final Date BOOM_START;
	private static final Date BOOM_END;

	static {
		Calendar gmtcal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		gmtcal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
		Date boomStart = gmt.getTime();
		gmtcal.set(1965 Calendar.JANUARY, 1, 0, 0, 0);
		Date boomEnd = gmt.getTime();
	}

	public boolean isBabyBoomer() {
		return birthDate.compareTo(BOOM_START) >= 0 && birthDate.compareTo(BOOM_END) < 0; 
	}
}
```
위와 같이 작성할 경우 Calendar 객체 생성의 비용이 크기때문에 성능향상을 기대할 수 있다. 
어댑터는 실제 기능을 `후면 객체`<sup>backing object</sup>에 위임하며, 후면 객체에 대한 또 다른 인터페이스를 제공하는 객체.
예를 들면 Map 인터페이스의 keySet 메소드는 Map 객체의 Set 뷰를 반환하는데, 얼핏 생각하면, **keySet()메소드를 호출** 할 때마다, 새로운 Set 이 반환될 것 같지만, 실제로는 **같은 Set이 반환.** 반환된 객체 중 하나가 변경되면, 다른 객체들도 변경된다.(후면 객체가 같기 때문)

객체 표현형 대신에 기본 자료형을 사용하고, 생각지도 못한 자동 객체화가 발생하지 않도록 유의할 것.

직접 관리하는 `객체 풀(object pool)`은 되도록이면 **사용하지 않는게** 좋은데, 단 하나 예외가 바로 Database 와의 **connection pool** 이다. 
