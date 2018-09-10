# 규칙 10. clone()을 재정의할 때는 신중할 것

Clonable 은 객체가 복제<sup>clone</sup>을 허용한다는 사실을 알리는데 쓰려고 고안된 [믹스인 인터페이스<sup>mixin interface</sup> 이다.](rule18.md) 조금 특이한 형태인데, interface에는 clone() 메서드가 존재하지도 않는다. 

#### final이 아닌 클래스에 clone()을 재정의 할때는 반드시 super.clone()을 호출해서 얻은 객체를 반환할 것.

#### Clonable 인터페이스를 구현하는 클래스는 제대로 동작하는 public clone 메소드를 제공할 것.
PhoneNumber 클래스에 clone을 재정의 한다고 하면, Clonable 인터페이스를 구현한다고 선언후, Object 클래스의 protected clone() 메소드를 접근할 수 잇도록 하는 public 메소드를 구현하면 된다.
```java
@Override
public PhoneNumber clone() {
	try {
		return (PhoneNumber) super.clone();
	} catch (CloneNotSupportedException ex) {
		throw new AssertionError(); // 수행될리 없음.
	}
}
```






equals() 메소드를 재정의 하는 클래스는 반드시 hashcode() 메소드도 재정이 해야한다.

hashcode() 를 꼭 재정의해야하는 이유는 같은 객체는 같은 해시코드 값을 가져야 하기 때문이다. 아래를 보면 equals() 메소드는 규칙 8에서 설명한대로 구현되어있다.

```java
public final class PhoneNumber {
	private final short areaCode;
	private final short prefix;
	private final short lineNumber;

	public PhoneNumber(int areaCode, int prefix, int lineNumber) {
		rangeCheck(areaCode, 999, "areaCode");
		rangeCheck(prefix, 999, "prefix");
		rangeCheck(lineNumber, 999, "lineNumber");
		this.areaCode = (short) areaCode;
		this.prefix = (short) prefix;
		this.lineNumber = (short) lineNumber;
	}

	private static void rangeCheck(int arg, int max, String name) {
		if (arg < 0 || arg > max)
			throw new IllegalArgumentException(name + ":" + arg);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) 
			return true;
		if (!(o instanceof PhoneNumber))
			return false;

		PhoneNumber pn = PhoneNumber.class.cast(o);
		return pn.lineNumber == lineNumber && pn.prefix == prefix && pn.lineNumber == lineNumber;
	}

	// hashcode 메소드가 없으므로 문제 발생
}

```

만약 위의 클래스를 HashMap과 함께 사용한다면
```java
Map<PhoneNumber, String> m = new HashMap<>();
m.put(new PhoneNumber(707, 867, 5309), "Jenny");

String result = m.get(new PhoneNumber(707, 867, 5309));
```
result의 값은 null 이다. PhoneNumber 클래스의 hashcode() 를 재정의 하지 않아서 각 객체는 서로 다른 hashcode를 갖기 때문이다. 
이런 경우 동일성 검사조차 하지 않는다.


## 해시코드 메소드는 어떻게 구현 하는가?

1. 필드가 boolean 이면 (f?1:0) 를 계산
2. 필드가 byte, char, short, int 이면 (int) f 를 계산
3. 필드가 long 이면 (int) (f^(f>>>32)) 를 계산
4. 필드가 float 이면 Float.floatToBits(f) 를 계산
5. 필드가 double 이면 Doulble.doubleToLongBits(f) 를 계산

위의 절차에서 구해진 해시코드 c 를 
result = 31 * result + c;
로 결합.

이것을 phoneNumber 클래스에 적용하면 
```java
@Override
public int hashcode() {
	int result = 17;
	result = 31 * result + areaCode;
	result = 31 * result + prefix;
	result = 31 * result + lineNumber;
	return result; 
}
```
꽤 괜찮은 해시 함수이다. 

> 주의할 것은, 성능을 개선하려고 객체의 중요 부분을 해시코드 계산 과정에서 생략하면 안된다.

