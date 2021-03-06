# 규칙 30. int 상수 대신 enum을 사용하라
자바에 enum은 고정개수의 상수들로 값이 구성되는 자료형, 그 전에는 int 형의 상수로 enum을 흉내내서 사용

enum을 흉내낸 int enum 패턴을 보자
```java
//int를 사용한 enum 패턴 - 지극히 불만족스럽다
public static final int APPLE_FUJI = 0;
public static final int APPLE_PIPPIN = 1;
public static final int APPLE_GRANNY_SMITH = 2;

public static final int ORANGE_NAVEL = 0;
public static final int ORANGE_TEMPLE = 1;
public static final int ORANGE_BLOOD = 2;
```
#### int enum 패턴의 단점 
1. == 연산자를 사용하면 오랜지와 사과가 같은 값이 나올 수 있다.
2. 개발자에게 의미전달이 안됨, 디버깅 하면 숫자만 보일뿐.

그래서 변종인 String enum 패턴도 있는데, 이건 더 안좋다.<sup>비교시에 항상 문자열 비교를 하므로 성능이 떨어져!</sup>

그래서 enum을 쓰면
```java
public enum Apple { FUJI, PIPPIN, GRANNY_SMITH }
public enum Orange { NAVEL, TEMPLE, BLOOD }
```
#### enum 열거형 자료형의 장점
1. 실질적으로 final로 선언된 것이나 마찬가지다. 
2. 같은 이름의 상수가 평화롭게 존재 가능<sup>namespace로 분리 가능하기 때문</sup>
3. 고품질의 Comparable, Serializable 인터페이스 등이 구현 되어있음.

enum 상수에 데이터를 넣으려면 객체 필드<sup>instance field</sup>를 선언하고 생성자를 통해 받은 데이터를 그 필드에 저장하여 사용

### enum 에서 각 요소가 다르게 동작해야하는 경우(switch를 사용해야 하는 경우)
단순히 switch를 선언해서 사용 할 수도 있는데...
```java
//자기 값에 따라 분기하는 Enum 자료형 - 의문스럽다!
public enum Operation {
	PlUS, MINUS, TIMES, DIVIDE;

	//this 상수가 나타내는 산술 연산 실행
	double apply(double x, double y) {
		switch(this) {
			case PLUS : return x + y;
			case MINUS : return x - y;
			case TIME : return x * y;
			case DIVIDE : return x / y;
		}
		throw new AssertionError("Unknown Operation : " + this);
	}
}
```
동작 하지만 예쁜 코드는 아님. 
enum 상수를 추가하고 apply 함수를 변경하지 않으면 오동작 가능성 존재.

#### 위의 방법보다 더 좋은 상수별 메소드 구현<sup>constant-specific method implementation</sup>

상수별 클래스 몸체<sup>constant-specific class body</sup> 에서 실제 메소드로 정의하는 방법

```java
//상수별 메소드 구현을 이용한 enum 자료형
public enum Operation {
	PLUS { double apply(double x, double y) { return x + y; } },
	MINUS { double apply(double x, double y) { return x - y; } },
	TIME { double apply(double x, double y) { return x * y; } },
	DIVIDE { double apply(double x, double y) { return x / y; } };

	abstract double apply(double x, double y);
}
```

enum에 상수를 추가하고 apply구현을 잊을 가능성은 없다.

위와 같이 하면 좋은데 보통은 toString을 오버라이드 하여 좀 더 표현력 있게 만들기도 한다.
```java
public enum Operation {
	PLUS("+") { double apply(double x, double y) { return x + y; } },
	MINUS("-") { double apply(double x, double y) { return x - y; } },
	TIME("*") { double apply(double x, double y) { return x * y; } },
	DIVIDE("/") { double apply(double x, double y) { return x / y; } };

	private final String symbol;
	Operation(String symbol) { this.symbol = symbol; }

	@Override public String toString() { return symbol;	}

	abstract double apply(double x, double y);
}
```
어떤 때에 자주 사용하냐면
```java
public static void main(String[] args) {
	double x = Double.parseDouble(args[0]);
	double y = Double.parseDouble(args[1]);

	for (Operation op : Operation.values()) {
		System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
	}
}
```


### enum 상수마다 정책이 있는 경우 좋은 방법
새로운 enum 상수를 추가할 때 초과근무 수당 계산 정책을 반드시 넣어야하는 구현법.
```java
//정책 enum 패턴
enum PayrollDay {
	MONDAY(PayType.WEEKDAY), TUESDAY(PayType.WEEKDAY),
	WENDSDAY(PayType.WEEKDAY), THURSDAY(PayType.WEEKDAY),
	FRIDAY(PayType.WEEKDAY),
	SATURDAY(PayType.WEEKEND), SUNDAY(PayType.WEEKEND);

	private final PayType payType;
	PayrollDay(PayType payType) { this.payType = payType; }

	double pay(double hoursWorked, double payRate) {
		return payType.pay(hoursWorked, payRate);
	}

	//정책 enum 자료형
	private enum PayType {
		WEEKDAY {
			double overtimePay(double hours, double payRate) {
				return hours <= HOURS_PER_SHIFT ? 0 : (hours - HOURS_PER_SHIFT) * payRate / 2;
			}
		},
		WEEKEND {
			double overtimePay(double hours, double payRate) {
				return hours * payRate / 2;
			}
		};

		private static final int HOURS_PER_SHIFT = 0;
		abstract double overtimePay(double hrs, double, payRate);

		double pay(double hoursWorked, double payRate) {
			double basePay = hoursWorked * payRate;
			return basePay + overtimePay(hoursWorked, payRate);
		}
	}
}
```
enum에서 switch문을 만들어서 사용한 코드보다는 복잡하지만 안전하다.

### switch를 쓰면 좋은 상황도 있다.

외부 enum 자료형 상수별로 달리 동작하는 코드를 만들어야 할 때는 enum 상수에 switch문을 적용하는 것이 좋다

```java
// 기존 enum 자료형에 없는 메소드를 switch 문을 사용해 구현한 사례
public static Operation inverse(Operation op) {
	switch(op) {
		case PLUS : return Operation.MINUS;
		case MINUS : return Operation.MINUS;
		case TIMES : return Operation.DIVIDE;
		case DIVIDE : return Operation.TIMES;
		default : throw new AssertionError("Unknown Operation : " + op);
	}
}
```

> enum 은 int 상수에 비해 가독성도 높고 안전하고, 강력하다. 여러 enum 상수가 공통 기능을 이용해야 하는 일이 생길 때는 **정책 enum 패턴** 사용을 고려하자.
