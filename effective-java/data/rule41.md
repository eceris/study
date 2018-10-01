# 규칙 41. 오버로딩할 때는 주의하라

아래 프로그램의 목적은 컬렉션을 종류별로 분류하는 것이지만 정상적으로 동작하지는 않는다. 

```java
public class CollectionClassifier {
	public static String classify(Set<?> s) {
		return "Set";
	}
	public static String classify(List<?> list) {
		return "List";
	}
	public static String classify(Collection<?> c) {
		return "Unknown Collection";
	}

	public static void main(String[] args) {
		Collection<?>[] collections = {
			new HashSet<String>(),
			new ArrayList<BigInteger>(),
			new HashMap<String, String>().values()
		};

		for (Collection<?> c : collections) {
			System.out.println(classify(c));
		}
	}
}
```

예상하기를 Set, List, Unknown Collection이 순서대로 출력되길 바라지만, 그렇지 않다. Unknown Collection 만 세번 호출된다. 

### 오버로딩된 메서드 가운데 어떤 것이 호출될 것인지는 컴파일 시점에 결정되기 때문.
- 이미 컴파일 시점에 자료형은 모두 `Collection<?>` 으로 동일하다.

> 오버로딩된 메서드는 정적<sup>static</sup> 으로 선택되지만, 오버라이딩된 메서드는 동적<sup>dynamic</sup> 으로 선택되기 때문

아래 프로그램은 오버라이딩을 구현한 예제이다.
```java
class Wine {
	String name() {
		return "wine";
	}
}

class SpaklingWine extends Wine {
	@Override
	String name() {
		return "SpaklingWine";
	}
}

class Champagne extends SpaklingWine {
	@Override
	String name() {
		return "Champagne";
	}
}

public class Overriding {
	public static void main(String [] args) {
		Wine [] wines = { new Wine(), new SpaklingWine(), new Champagne() };

		for (Wine wine : wines) {
			System.out.println(wine.name());
		}
	}
}
```
분명이 컴파일 시점 자료형은 Wine 이었지만, 메소드 재정의로 인해 Wine, SpaklingWine, Champagne 가 출력된다.

메소드 오버로딩으로 위와같이 구현하려면 
```java
public static String classify(Collection<?> c) {
	return c instanceof Set ? "Set" ? c instanceof List ? "List" : "Unknown Collection";
}
```

### 오버라이딩이 일반적 규범이라면, 오버로딩은 예외에 해당한다. 
그러므로
1. 오버로딩을 사용할 때는 혼란스럽지 않게 사용할 수 있도록 주의해야 한다.
2. 혼란을 피하는 좋은 방법은, 같은 수의 인자를 갖는 두 개의 오버로딩 메소드를 같은 API에 포함하지 않는 것.

### varargs 를 사용하지 않으면 아예 오버로딩 하지 않는 보수적인 전략을 취하자.
예를 들면, ObjectOutputStream의 경우 write 메소드를 재정의하는 대신에 
`writeBoolean(boolean)`, `writeInt(int)` 와 같이 정의되어 있다.

### autoboxing 과 제네릭으로 인해 오버로딩 할 때 주의 해야 한다. 
예를 들면, List 인터페이스에 remove(E)와 remove(Object)는 완전히 다르지 않다. 그러므로 조심할 것.


> 메소드를 오버로딩 할 수 있다고 해서 반드시 그래야 하는 것은 아니다. 인자 개수가 같은 오버로딩 메소드를 추가하는 것은 피해야한다. 



