# 규칙 1. 생성자 대신 정적 팩터리 메서드를 사용할 수 없는지 생각해 보라

객체를 생성하는데 일반적인 public 생성자를 통해 하는것 보다 static 팩토리 매소드를 이용해서 만드는 것을 고려할 것.
아래 코드는 primitive타입의 값을 Boolean 객체에 대한 참조로 반환한다.
```java
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
} 
```

## 장점 1 : 생성자와는 다르게 static 팩토리 메소드에는 이름이 있다.
생성자는 어떤 객체가 생성되는지 설명하지 못하지만 static 팩토리 메소드는 이름을 통해 **간접적으로 설명**.(메소드의 네이밍이 그만큼 중요) 사용하기도 쉽고 코드의 가독성도 높아짐.
그리고 **생성자가 여러개** 있는 경우, 실수할 가능성이 있으니 static 팩토리 메소드를 고려할 것.

## 장점 2 : 호출할 때마다 새로운 객체를 생성할 필요가 없다.
불필요하게 생성되는 일을 피할수 있다. Boolean.valueOf()는 좋은 예이고 **경량(Flyweight)** 패턴과 유사.
같은 객체를 반복적으로 반환하므로 이 객체의 생명주기에 대해 정밀하게 제어가능<sup>이런 기능을 갖춘 클래스를 개체 통제 클래스(instance-controlled class)라고 부른다.</sup>

## 장점 3 : 생성자랑 다르게 반환값 자료형의 하위 자료형 객체를 반환 할 수 있다.
public으로 선언되지 않은 클래스의 객체를 반환 가능. [인터페이스 기반 프레임워크](rule18.md) 구현에 적합하다.
인터페이스는 static 메소드를 가질수 없으므로 관습상 반환값 자료형이 Type이라는 이름의 인터페이스인 static 팩토리 메소드는 Types<sup>Collections와 같은..</sup>라는 이름의 [nonInstantiable 클래스](rule4.md)에 둔다. 

반환값 자료형이 Type라는 이름의 인터페이스인 정적 팩토리 메소드는 Types 라는 이름의 [객체 생성 불가능 클래스](rule4.md) 안에 둔다

JDK 1.5의 [java.util.EnumSet](rule32.md)에는 public 생성자가 없고 팩토리 메서드만 제공한다. JDBC와 같은 `서비스 제공자 프레임워크` 가 이런 유연한 static 팩토리 메소드로 구성되어 있다.
```java
//서비스 인터페이스
public interface Service {
	//서비스에 공한 메서드들이 이자리에 온다.
}

//서비스 제공자 인터페이스
public interface Provider {
	Service newService();
}

//서비스 등록과 접근에 사용되는 객체 생성 불가능 클래스
public class Services {
	private Services() {} //객체 생성 방해(규칙4)

	//서비스 이름과 서비스 간 대응관계 보관
	private static final Map<String, Provider> providers = new ConcurrentHashMap<String, Provider>();
	public static final String DEFAULT_PROVIDER_NAME = "<def>";

	//제공자 등록 API
	public static void registerDefaultProvider(Provider p) {
		registerProvider(DEFAULT_PROVIDER_NAME, p);
	}
	public static void registerProvider(String name, Provider p) {
		providers.put(name, p);
	}

	//서비스 접근 API
	public static Service newInstance() {
		return newInstance(DEFAULT_PROVIDER_NAME);
	}

	//서비스 접근 API
	public static Service newInstance(String name) {
		Provider p = providers.get(name);
		if (p == null) {
			throw new IllegalArgumentException("No provider registered with name: " + name);
		}
		return p.newService();
	}
}

```

## 장점 4 : 형인자 자료형 객체를 만들때 편하다.

아래처럼 자료형 명세를 중복하면, 형인자가 늘어남에 따라 코드가 복잡해진다. 
```java
Map<String, List<String>> m = new HashMap<String, List<String>>();
```

아래와 같이 만들수 있는데, JDK 1.7 부터 Java가 기본 제공하는 다이아몬드 연산자를 제공한다.
```java
Map<String, List<String>> m = HashMap.newInstance(); // EX

Map<String, List<String>> m = new HashMap<>(); // JDK 1.7

```

## 단점 1 : public 이나 protected 로 선언된 생성자가 없으므로 하위 클래스를 만들 수 없다.
예를 들면 자바의 `Collections` 패키징 포함된 기본 클래스들은 하위 클래스를 만들 수 없는데, 상속 대신 구성<sup>composition</sup> 을 장려한다는 측면에서 더 좋다는 사람도 있다.

## 단점 2 : static 팩토리 메소드가 다른 static 메소드들과 구별이 잘 안된다는 점.
그래서 보통은 관례적으로 아래와 같이 사용한다.

| 관례적 이름 | 설명 |
|:--------|:--------|
| valueOf | 인자로 주어진 값과 같은 값을 가진 객체를 반환 |
| of | valueOf 보다 더 간단한 형태로 [EnumSet](rule32.md) 덕분에 널리 퍼짐 |
| getInstance | 싱글턴 패턴을 따르며 인자 없이 항상 같은 객체를 반환 |
| newInstance | getInstance와 같지만, 호출될때마다 새로운 객체를 반환 |
| getType | getInstance와 같지만, 반환될 객체의 클래스와 다른 클래스에 팩토리 메소드가 있는 경우, Type은 팩토리 메소드가 반환할 객체의 자료형 |
| newType | newInstance와 같지만, 반환될 객체의 클래스와 다른 클래스에 팩토리 메소드가 있는 경우, Type은 팩토리 메소드가 반활할 객체의 자료형 |

> `static 팩토리 메소드`와 `public 생성자`는 용도가 다르다. 그 둘의 차이점을 알고 **무조건 적인 선택**을 하지 말자.
