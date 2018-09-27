# 규칙 34. 확장 가능한 enum을 만들어야 한다면 인터페이스를 이용하라

enum 자료형을 상속 받는 것은 바람직 하지 않음. 그러나 인터페이스를 만들고 그것을 상속받는 enum은 가능하다.

좋은 케이스가 바로 연산코드<sup>opcode</sup>를 만들 때 이다.
```java
//인터페이스를 이용해 확장 가능하게 만든 enum 자료형
public interface Operation {
	double apply(double x, double y);
}

public enum BasicOperation implements Operation {
	PLUS("+") {
		public double apply(double x, double y) { return x + y; }
	},
	MINUS("-") {
		public double apply(double x, double y) { return x - y; }
	},
	TIMES("*") {
		public double apply(double x, double y) { return x * y; }
	},
	DIVIDE("/") {
		public double apply(double x, double y) { return x / y; }
	};

	public final String symbol;
	BasicOperation(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}
}
```
위의 코드에 새로운 연산을 추가하려면, 아래와 같이 Operation을 구현하는 enum을 다시 만들면 된다.

```java
public enum ExtendedOperation implements Operation {
	EXP("^") {
		public double apply(double x, double y) { return Math.pow(x, y); }
	},
	REMINDER("%") {
		public double apply(double x, double y) { return x % y; }
	};

	private final String symbol;

	ExtendedOperation(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}
}
```

toString과 같은 중복 코드가 있다. 만약 공유해야 되는 기능이 많다면 Helper class나 static helper method 를 이용해 중복을 제거할 수 있다.

> 상속가능한 enum은 만들수 없지만 인터페이스를 만들고 그 인터페이스를 상속받는 enum을 만들면 상속을 비슷하게 흉내낼 수 있다. 

