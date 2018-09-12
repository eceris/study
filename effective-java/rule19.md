# 규칙 19. 인터페이스는 자료형을 정의할 때만 사용할 것

인터페이스를 구현하는 클래스를 만들면, 

1. 인터페이스는 **자료형**<sup>type</sup>역할을 하게 됨.
2. 클라이언트에게 클래스로 **어떤 일을 할 수** 있는지 알리는 역할을 하게 됨.

#### 다른 목적으로 인터페이스를 사용하는 것은 적절치 않음.

```java
// 상수 인터페이스 패턴은 사용하지 않는게 좋다 ..... 안티패턴!
public interface PhysicalConstants {
	// 아보가드로 수
	static final double AVOGADROS_NUMBER = 6.02214199e23;
	// 볼쯔만 상수
	static final double BOLTZMANN_CONSTANT = 1.3806503e-23;
	// 전자의 질량
	static final double ELECTRON_MASS = 9.10938188e-31;
}
```

#### 상수 인터페이스 패턴은 인터페이스를 잘못 사용한 것이다. 

- 상수 정의를 인터페이스에 포함시키면 개발자들이 혼돈스러워 함.
- **java.io.ObjectStreamConstants** 처럼 실수로 상수 인터페이스가 포함되기도 했는데, **따라하지는 말것.**

#### 상수는 [객체 생성이 불가능한](rule4.md) 유틸리티 클래스에 넣어서 공개 해야한다.

위의 코드를 개선해보면....

```java
public class PhysicalConstants {

	private PhysicalConstants() {} //객체 생성 막음
	// 아보가드로 수
	public static final double AVOGADROS_NUMBER = 6.02214199e23;
	// 볼쯔만 상수
	public static final double BOLTZMANN_CONSTANT = 1.3806503e-23;
	// 전자의 질량
	public static final double ELECTRON_MASS = 9.10938188e-31;
}
```

이렇게 하고 PhysicalConstants.AVOGADROS_NUMBER 로 사용하는데 PhysicalConstants이게 붙는게 싫으면 JDK1.5부터 들어간 정적 임포트를 사용할 것.

> 인터페이스는 자료형을 정의할 때만 사용하고, 특정 상수를 API의 일부로 공개할 때는 객체생성이 불가능한 유틸클래스로 공개하자...

