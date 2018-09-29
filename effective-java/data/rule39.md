# 규칙 39. 필요하다면 방어적 복사본을 만들라

자바는 직접적으로 native method를 사용하지 않으면, 안전한 언어<sup>safe language</sup> 이다. 그러나 스스로 노력하지 않으면 다른 클래스로부터 영향을 받지 않을 수 없다.

#### 내가 만든 클래스의 클라이언트가 불변식을 망가뜨릴 것이라고 생각하고 최대한 방어적으로 프로그래밍 하자.
실수로라도 클라이언트는 이상하게 동작하는 코드를 작성할 수 있으니, 클라이언트가 이상한 짓을 해도 정상 동작하는 클래스를 만들기 위해 노력하자.

아래 예는 변경불가능해 보이지만 실제로는 클라이언트가 변경가능한 클래스이다.
```java
//변경불가능성이 보장되지 않는 이름뿐인 변경 불가능 클래스
public final class Period {
	private final Date start;
	private final Date end;
	/*
	*	@param start 기간의 시작 시점
	*	@param end 기간의 끝 시점. start 보다 작은 값일 수 없다
	*	@throw IllegalArgumentException start 가 end 보다 뒤면 발생
	*	@throw NullPointerException start 나 end 가 null 이면 발생
	*/
	public Period(Date start, Date end) {
		if (start.compareTo(end) > 0) {
			throw new IllegalArgumentException(start + " after " + end);
		}
		this.start = start;
		this.end = end;
	}

	public Date start() {
		return start;
	}
	public Date end() {
		return end;
	}
	... //이하 생략
}
```
얼핏 보면 변경 불가능해 보이지만, 아래와 같이 클라이언트에서 사용할 경우 불변식을 깨뜨릴수 있다.
```java
// Period 객체 내부 구조를 공격
Date start = new Date();
Date end = new Date();
Period p = new Period(start, end);
end.setYear(78); // p의 내부를 변경!
```
이러한 경우 오동작 할 수 있으므로, **Period 객체 내부를 보호** 하기 위해 **생성자로 전달되는 변경 가능 객체**를 반드시 **방어적**으로 복사 해서 그 **복사본을 Period 객체의 컴포넌트로 이용**해야 한다.
생성자를 아래와 같이 변경한다.
```java
//수정된 생성자 - 인자를 방어적으로 복사함
public Period(Date start, Date end) {
	this.start = new Date(start.getTime());
	this.end = new Date(end.getTime());

	if (this.start.compareTo(this.end) > 0) {
		throw new IllegalArgumentException(this.start + " after " + this.end);
	}
}
```
#### 인자의 유효성 검사는 방어적 복사본을 만들고 그 복사본에 수행해야 한다.
- 자연스러워 보이지 않을 수도 있지만, TICTOU 공격<sup>time-of-check/time-of-use 공격</sup> 을 피하기 위해 필요하다.
- 방어적 복사본을 만들때 clone() 메소드를 이용하지 않았는데 이것은, 인자로 전달된 객체의 자료형의 제3자가 상속 받을 수 있는 자료형 인 경우, clone 메소드가 반드시 Date를 반환할 것이라는 보장이 없기 때문.

#### 변경 가능내부 필드에 대한 방어적 복사본을 반환하도록 접근자를 수정하자.
생성자를 통한 공격은 막았으나, 접근자에 대한 공격은 처리하지 않았는데, 아래와 같이 공격이 가능하다.
```java
// Period 객체 내부를 노린 두번째 공격
Date start = new Date();
Date end = new Date();
Period p = new Period(start, end);
p.end().setYear(78); // p의 내부를 변경!
```
위의 공격을 막으려면 접근자를 아래와 같이 변경해야 한다.
```java
// 수정된 접근자 - 내부 필드의 방어적 복사본 생성
public Date start() {
	return new Date(this.start.getTime());
}

public Date end() {
	return new Date(this.end.getTime());
}
```
이렇게 되면 Period 객체는 진짜 변경 불가능 클래스가 된다.

> 내가 만드는 클래스가 변경 가능이건 불가능이건, 변경 가능한 내부 컴포넌트에 대한 참조를 반환할 때는 한번더 생각 할 것.

#### 방어적 복사본의 단점
방어적 복사본을 만들면 성능상 손해를 보기 마련이기에 적절치 않을 때도 있다.

- 만약 클라이언트가 패키지 내부에 있어서 클라이언트가 내부 상태를 변경하지 못하는 상황이라면 방어적 복사본을 만들 필요가 없다.


> 클라이언트로부터 인자를 받거나 클라이언트에게 반환되는 변경 가능 컴포넌트가 있는 경우, 꼭 방어적으로 복사하여 반환해줄 필요가 있다. 만약 오버헤드가 너무 크다면, 해당 컴포넌트를 변경해서는 안된다는 문서를 명시하고 넘어갈 수 있다.
