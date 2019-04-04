# 규칙 78. 직렬화된 객체 대신 직렬화 프락시를 고려해보자

일반 생성자 대신 언어 외적인 메커니즘을 통해 객체를 생성하면 위험이 생기기 마련인데, 이 위험을 크게 줄일 수 있는 기술이 바로 **직렬화 프락시 패턴**<sup>serialization proxy pattern</sup>이다.

## 패턴을 구현하는 방법
1. 바깥 클래스 객체의 논리적 상태를 간결하게 표현하는 직렬화 가능 클래스를 **private static** 중첩클래스로 설계.
2. 이 클래스를 **직렬화 프락시**<sup>serialization proxy</sup>라고 부르는데, **바깥 클래스**를 **파라미터로 받는 생성자**를 하나만 소유하고, 이 생성자는 인자에서 **데이터를 복사**하기만 함.
3. 일관성, 방어적 복사가 필요없음. 직렬화 프락시의 직렬화 형식은 바깥 클래스의 완벽한 직렬화 형태.
4. 직렬화 프락시도 Serializable 인터페이스를 구현할 것.

예를 들어, [규칙 39](rule39.md)에서 만들었던 변경 불가능 period 클래스에 직렬화 프락시 코드를 구현했다.
```java
// Period 클래스의 직렬화 프락시
private static class SerializationProxy implements Serializable {
	private final Date start;
	private final Date end;

	SerializationProxy(Period p) {
		this.start = p.start;
		this.end = p.end;
	}

	private static final long serialVersionUID = 23845783834837945L; // 아무 수나 가능 규칙 75;
}
```
위의 프락시를 구현한 다음, 바깥 클래스에 아래의 writeReplace 메서드를 구현한다. (이 메서드는 직렬화 프락시가 구현되어있다면 아무런 변경없이 사용가능)

```java
private Object writeReplace() {
	return new SerializationProxy(this);
}
```
위의 메서드가 있으면 직렬화 시스템은 바깥 클래스 대신 프락시 객체를 직렬화 한다.

- writeReplace 메서드는 직렬화가 이루어지기 전에 바깥 클래스 객체를 **직렬화 프락시 객체**로 변환.

공격자가 클래스 불변식을 훼손하고자 할 경우 공격을 막기 위해 아래 readObject 메소드를 바깥 클래스에 추가하자.
```java
// 직렬화 프락시 패턴을 구현하기 위한 readObject 메서드
private void readObject(ObjectInputStream ois) {
	throw new InvalidObjectException("Proxy required");
}
```

마지막으로, SerializationProxy 클래스에 자기와 논리적으로 동일한 바깥 클래스 객체를 반환하는 readResolve 메서드를 추가해야 함.

- 이 메서드가 있으면 직렬화 시스템은 역식렬화를 끝내자 마자, 직렬화 파릭시 객체를 다시 바깥 클래스 객체로 변환.
- readResolve 메서드는 그 public API 만 사용해서 바깥 클래스 객체를 만드는데, 이것은 언어 외적인 메커니즘이 아니므로, 직렬화의 언어 외부적<sup>extralinguistic</sup> 특성이 대부분 제거됨.

아래 Period.SerailizationProxy의 readResolve 메서드 코드를 보자.
```java
// Period.SerializationProxy 의 readResolve 메서드
private Object readResolve() {
	return new Period(start, end); //public 생성자 이용
}
```

**직렬화 프락시 접근법** 을 이용하면 Period 의 필드를 final 로 선언할 수 있어서, [Period 를 진정한 변경 불가능 클래스로 만들 수 있다.](rule15.md)

## EnumSet 의 사례
1. EnumSet 은 기본 static 생성자는 64개 이하의 원소인 경우 RegularEnumSet, 그 이상인 경우 JumboEnumSet 을 반환한다. 
2. 64개 이하의 원소로 EnumSet을 생성하고 그 이후에 5개를 추가한다음, 직렬화 하면 생성은 RegularEnumSet 이지만, 역직렬화 할 경우 JumboEnumSet 이 된다.
3. 이것은 실제로 EnumSet이 **직렬화 프락시 패턴**으로 구현되었기 때문이다. 

아래 구현 모습을 보자. 정말 이정도로 단순하다고...하더라..;;

```java
// EnumSet의 직렬화 프락시
private static class SerailizationProxy <E extends Enum<E>> implements Serializable {
	// 이 enum 집합의 원소 자료형
	private final Class<E> elementType;

	// 이 enum 집합에 담긴 원소들
	private final Enum[] elements;

	SerializationProxy(EnumSet<E> set) {
		elementType = set.elementType;
		elements = set.toArray(EMPTY_ENUM_ARRAY);
	}

	private Object readResolve() {
		EnumSet<E> result = EnumSet.noneOf(elementType);
		for (Enum e : elements) {
			result.add((E) e);
		}
		return result;
	}

	private static final long serialVersionUID = 34785903748953479L;
} 
```

## 직렬화 프락시의 단점
1. [클라이언트가 확장할 수 있는 클래스에는 적용할 수 없음.](rule17.md)
2. 객체 그래프에 순환되는 부분이 있는 클래스에는 적용 불가.


> 클라이언트가 확장 할 수 없는 클래스에 readObject 나 writeObject 를 구현해야 할 때는 직렬화프락시패턴 도입을 고려해보자. 불변식을 만족해야 하는 객체를 안정적으로 직렬화 하는 쉬운 방법이 된다.


















