# 규칙 81. 메소드 레퍼런스에 람다를 사용하자.

익명클래스에서 람다의 첫번째 장점은 간결하다는 것이었다. 자바는 function 객체를 생성하는데 람다보다 더 편한 방법이 바로 메소드 레퍼런스이다. 
아래에 맵에서 임의의 integer 키를 가져오는 람다를 사용한 코드 조각이 존재한다. 

```java
map.merge(key, 1, (count, incr) -> count + incr);
```

위의 메소드가 자바 8 Map 에 추가된 `merge` 함수를 사용하는 것을 기억하자. 만약 주어진 키에 매핑이 존재하지 않으면, 메소드는 값을 삽입한다. 만약 매핑이 이미 존재한다면, `merge` 메소드는 현재 값에 function 을 overwrite 한다. 이 코드는 `merge` 메소드의 전형적인 사용법을 보여준다.
코드가 잘 읽혔다, 그러나 아직 boilierplate 코드가 존재한다. 파라미터인 `count` 와 `incr` 는 많은 값을 추가하지 않고, 많은 공간을 차지한다. 사실, 모든 lambda 함수가 두 개의 인수의 합을 반환한다는 것을 알려준다. 

java8 에서 `Integer` 는 `sum` 이라는 static 메소드를 제공한다. 우리는 간단하게 이것을 메소드레퍼런스를 이용해 아래와 같이 사용할 수 있다.
```java 
map.merge(key, 1, Integer::sum);
```

메소드가 갖고 있는 많은 파라미터와 boilerplate 를 제거할 수 있다. 그러나, 몇 가지 람다에서는 매개 변수 이름이 유용한 문서를 제공하므로 람다가 더 길더라도 메소드 레퍼런스보다 읽기 쉽게 유지관리 할 수 있다.
메소드 레퍼런스를 사용하면 못하는 일들을 람다를 사용하면 가능한 작업들이 있다. (하나의 모호한 예외는 있다.) 거의 모든 메소드 레퍼런스는 보통 짧고, 깨끗하다. 또한, 너무나 복잡한 람다식에서 빠져나올수 있도록 도와준다. 람다에서 코드를 추출하여 새로운 메소드로 만들수 있고, 메소드 레퍼런스를 이용하여 람다 코드를 교체할 수 있다. 또한, 메소드에 당신이 진정으로 원하는 이름을 줄 수 있다.

만약 당신이 IDE를 사용하고 있다면, 람다를 언제나 메소드 레퍼런스로 바꿀 수 있다. 가끔, 람다는 메소드 레퍼런스보다 간결할 때가 있다. 메소드가 람다와 같은 클래스에 존재할때 자주 발생하는데 예를 들면 아래 코드와 같다. 아래 코드는 GoshThisClassNameIsHumongous 라는 클래스로 추정한다.
```java
service.execute(GoshThisClassNameIsHumongous::action);
```
람다로 표현하면 아래와 같다.
```java
service.execute(() -> action());
```
위의 코드조각은 메소드 레퍼런스보다 람다가 더 깨끗하고 보기 좋다. 이것과 비슷하게 `Function` 인터페이스는 identity function 을 위해 몇가지 제네릭 static 메소드를 제공하는데, `Function.identity();` 와 같다. 이것은 람다를 사용한 `x -> x`와 같지만 더 짧고 깨끗하다.

많은 메소드 레퍼런스는 static 메소드를 참조하는데, 그렇지 않은 4가지 케이스가 존재한다. 두가지는 bound 와 unbound 인스턴스 메소드 레퍼런스이다. bound 참조는 받은 객체가 메소드 레퍼런스안에 정의된다. bound 참조는 static 참조와 비슷하다. : function 객체는 refernced method 와 같은 파라미터를 갖는다. unbound 참조는 stream에서 mapping과 filter function 에서 자주 사용된다. 

마지막으로, classes 와 arrays를 위한 두가지 생성자가 존재한다. 생성자 references 는 팩토리 메소드를 제공한다. 5가지의 모든 메소드 레퍼런스는 아래 존재하는 메소드들을 요약한다. 

| MethodRef Type | Example | Lambda Equivalent |
|:--------|:--------|:--------|
| Static | Integer::parseInt | str -> Integer.parseInt(str); |
| Bound | Instant.now()::isAfter | Instant then = Instant.now(); t -> then.isAfter(t); |
| Unbound | String::toLowerCase | str -> str.toLowerCase(); |
| Class Constructor | TreeMap<K,V>::new | () -> new TreeMap<K,V> |
| Array Constructor | int[]::new | len -> new int[len] |

> 요약하면, 메소드 레퍼런스는 람다보다 더 간편함을 제공한다. 메소드 레퍼런스가 짧고 명확하면 그것을 사용하고, 그렇지 않다면 람다 사용을 고수하자.
