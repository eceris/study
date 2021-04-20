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


### 2.1 가능하면 적게 캡슐화하세요
- 언어 설계의 결함으로 == 비교와 equals 의 기본 구현 역시 정확한 판단을 하지 못함. 
- 해결하기 위해 항상 equals() 메소드를 오버라이드 하자.<sub>lombok의 @EqualsAndHashCode는 좋은 대안이다.</sub>

### 2.2 최소한 뭔가는 캡슐화하세요

### 2.3 항상 인터페이스를 사용하세요

### 2.4 메서드 이름을 신중하게 선택하세요
- 빌더는 어떤 것을 만들고, 조정자는 뭔가를 조작하는데, 이 둘을 한꺼번에 하는 것을은 옳지 않다.

#### 2.4.1 빌더는 명사다
```java
// 잘못된 예
InputStream load(URL url);
String read(File file);
int add(int x, int y);

// 잘된 예
InputStream stream(URL url);
String content(File file);
int sum(int x, int y);
```

#### 2.4.2 조정자는 동사다
- 조정자와 빌더의 다른 점은 반환값의 유무. 빌더만이 값을 반환할 수 있고, 이름은 명사.
- 빌더패턴을 기본적으로 반대하지만 만약 사용한다면...
```java
class Book {
	Book withAuthor(String author);
	Book withTitle(String title);
	Book withPage(Page page);
}
```

#### 2.4.3 빌더와 조정자 혼합하기
```java
class Document {
	int write(InputStream content);
} 
```
- 위의 `write`함수는 데이터를 쓰는 동시에 쓰여진 바이트를 반환하는데 초점이 뚜렷한 좋은 코드는 아님.
- 차라리 아래와 같이 리팩토링 하는 것이 좋음.
```java
class Document {
	OutputPipe OutputPipe();
}
class OutputPipe {
	void write(InputStream content);
	int bytes();
	long time();
}
```

#### 2.4.4 Boolean 값을 결과로 반환하는 경우
```java
// 잘못된 예
boolean isEmpty();
boolean isReadable();
boolean isNegative();

boolean equals(Object obj);
boolean exists();

//잘된 예
boolean empty(); // is empty
boolean readable();	// is readable
boolean negative(); // is negative

boolean equalTo(Object obj);
boolean present();
```
- 빌더인 동시에 조정자여서는 안됨. 빌더라면 이름을 명사로, 조정자라면 이름을 동사로..
 

### 2.5 퍼블릭 상수(public constant)를 사용하지 마세요
- 코드중복이라는 문제를 해결하기 위해 결합도가 높아지고 응집도가 낮아지게 하는 실수를 한다.

#### 2.5.1 결합도 증가
- 퍼블릭 상수가 변경시 어떤 객체에 어떤 영향을 미치는지 알 수 없음.
- 상수가 복잡해질수록 문제는 더 심각해짐.

#### 2.5.2 응집도 저하
- 상수는 자신에 대해 알지 못하고 삶의 의미?? 가 명확하지 않음. 이말은 즉 응집도가 저하된다는 얘기.
- 만약 구현한다면 아래와 같이 래핑하고 의미를 부여하여 사용.
```java
class EOLString {
	private final String origin;
	EOLString(String src) {
		this.origin = src;
	}
	@Override
	String toString() {
		return String.format("%s\r\n", origin);
	}
}

class Records {
	void write(Writer out) {
		for (Record rec : this.all) {
			out.write(new EOLString(rec.toString()));
		}
	}
}
```
- EOLString 에 대한 결합은 계약을 통해 추가되었고 이 결합은 언제라도 분리가 가능하여 유지보수성을 높힘.
- 사실, 애플리케이션을 구성하는 클래스의 수가 많을수록 설계가 더 좋아지고 유지보수하기도 쉬워진다?????고 하는데 잘 모르겠음.


### 2.6 불변 객체로 만드세요
- 지연로딩이라는 기술에 불변객체를 사용해서 만드는 것은 불가능.
- 그래서 언어레벨에서 `@OnlyOnce` 와 같은 애노테이션을 제공해야 한다고 저자는 생각함.

#### 2.6.1 식별자 가변성
- map에 이미 키로써 put 된 객체가 가변하다면, map에서 오동작.

#### 2.6.2 실패 원자성

#### 2.6.3 시간적 결합
- 코드의 순서로 인해 결함이 발생할 수 있음. 애초에 불변하게 만들었다면, 코드의 구문 사이에 존재하는 `시간적 결합` 을 제거할 수 있음.

#### 2.6.4 부수효과 제거

#### 2.6.5 NULL 참조 없애기
- 사용하는 곳 마다 null체크를 해야 하면 유지보수성이 너무 떨어짐.
- 객체를 불변으로 만들면 애초에 Null을 할당할수 없기에 응집도 높은 객체를 생성할 수 밖에 없게 만듬.

#### 2.6.6 스레드 안정성
- 

#### 2.6.7 더 작고 더 단순한 객체

### 2.7 문서를 작성하는 대신 테스트를 만드세요
- 



