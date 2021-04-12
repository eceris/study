# 엘레강트 오브젝트

Yegor Bugayenko 의 `객체지향 프로그래밍에 대한 전면적 반기를 든 23가지 조언` 을 읽고 정리한다.

# 목차
## 1장 출생
1.1 -er로 끝나는 이름을 사용하지 마세요
1.2 생성자 하나를 주 생성자로 만드세요
1.3 생성자에 코드를 넣지 마세요

## 2장 학습
2.1 가능하면 적게 캡슐화하세요
2.2 최소한 뭔가는 캡슐화하세요
2.3 항상 인터페이스를 사용하세요
2.4 메서드 이름을 신중하게 선택하세요
2.5 퍼블릭 상수(public constant)를 사용하지 마세요
2.6 불변 객체로 만드세요
2.7 문서를 작성하는 대신 테스트를 만드세요
2.8 모의 객체(Mock) 대신 페이크 객체(Fake)를 사용하세요
2.9 인터페이스를 짧게 유지하고 스마트(smart)를 사용하세요

## 3장 취업
3.1 5개 이하의 public 메서드만 노출하세요
3.2 정적 메서드를 사용하지 마세요
3.3 인자의 값으로 NULL을 절대 허용하지 마세요
3.4 충성스러우면서 불변이거나, 아니면 상수이거나
3.5 절대 getter와 setter를 사용하지 마세요
3.6 부 ctor 밖에서는 new를 사용하지 마세요
3.7 인트로스펙션과 캐스팅을 피하세요

## 4장 은퇴
4.1 절대 NULL을 반환하지 마세요
4.2 체크 예외(checked exception)만 던지세요
4.3 final이거나 abstract이거나
4.4 RAII를 사용하세요



### 1.1 -er로 끝나는 이름을 사용하지 마세요
- er 로 끝나는 명명 원칙이 인기가 많지만 좋지않은데, 클래스 이름은 객체가 노출하고 있는 기능에 기반해서는 안됨. 
- 클래스 이름은 무엇을 하는지(what he does)가 아니라 무엇인지(what he is)에 기반해야함.
- CashFormatter 라는 이름 보다는 아래 코드와 같이 제공하는 것이 좋음.

```java
class Cash {
	private int dollars;
	Cash(int dlr) {
		this.dollars = dlr;
	}
	public String usd() {
		return String.format("$ %d", this.dollars);
	}
}
```

- PrimeNumbers 라는 예로 설명하는데 그럼 소수를 뽑는 절차는 어떻게 클래스로 만들지? 라는 의문이 생김.


### 1.2 생성자 하나를 주 생성자로 만드세요
- 권장하는 것은 클래스는 많은 수의 생성자와 적은 메소드로 구성해야 하며 생성자가 많을수록 클래스는 더 개선되고 클라 입장에서 편하게 사용가능하다.
- 메서드가 많아지면 클래스의 초점이 흐려진다?고 하는데 의문임.
- 생성자는 프로퍼티를 초기화만 해야지 안에서 뭔가를 해서는 안된다. 유지보수성 때문이라도..
```java
//잘된 예
class Cash {
	private int dollars;
	Cash(Float dlr) {
		this((int)dlr);
	}
	Cash(String dlr) {
		this(Cash.parse(dlr));
	}
	Cash(int dlr) {
		this.dollars = dlr;
	}
}

//잘못된 예
class Cash {
	private int dollars;
	Cash(Float dlr) {
		this.dollars = (int)dlr; //틀렸음
	}
	Cash(String dlr) {
		this.dollars = Cash.parse(dlr); //틀렸음
	}
	Cash(int dlr) {
		this.dollars = dlr;
	}
}
```


### 1.3 생성자에 코드를 넣지 마세요
- 객체 초기화에는 `코드가 없어야` 하고 인자를 건드리면 안됨.
- 아래 코드의 잘된 예와 잘못된 예의 차이점은, 초기화 시점에 파싱하는게 아니라 사용하는 시점에 파싱하는 것.
- 자주 사용된다면 매번 파싱하는 것이 성능측면에서는 안좋을 수 있지만, 만약 캐싱한다면(데코레이터 패턴을 사용하여) 그것도 극복 가능함. 
- 잘못된 예의 경우 무언가를 하려면 파싱을 하고 난 이후에나 가능, 심지어 유효성검사 조차도...

```java
//잘못된 예
class Cash {
	private int dollars;
	Cash(String dlr) {
		this.dollars = Integer.parseInt(dlr);
	}
}

//잘된 예
class Cash {
	private int dollars;
	Cash(String dlr) {
		this.dollars = new StringAsInteger(dlr);
	}
}

class StringAsInteger implements Number {
	private String source;
	StringAsInteger(String src) {
		this.source = src;
	}
	int intValue() {
		return Integer.parseInt(this.source);
	}
}
```




