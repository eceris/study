# 규칙 44. 모든 API 요소에 문서화 주석을 달라

사용할 수 있는 API 가 되려면 문서가 있어야 한다. 

좋은 API 문서를 만드려면 API에 포함된 모든 **클래스, 인터페이스, 생성자, 메소드 그리고 필드 선언** 에 **문서화 주석**을 달아야한다.

## 메서드
메서드에 대한 문서화 주석은 메서드와 클라이언트 사이의 **규약**<sup>contract</sup> 을 간단하고 명료하게 설명해야한다.

문서화 주석에는 메서드의 **선행조건**<sup>precondition</sup> 과 **후행조건**<sup>postcondition</sup> 을 나열해야 함.


### 선행 조건
클라이언트가 메서드를 호출하려면 반드시 **참**<sup>true</sup>가 되어야 하는 조건들.

1. 무점검 예외<sup>unchecked exception</sup> 에 대한 **\@throws** 태그를 통해 암묵적으로 기술
2. 관계된 인자의 **\@param** 태그를 통해 명시.

### 후행 조건
메서드 실행이 성공적으로 끝난 다음에 만족되어야 하는 조건들.


### 메서드 주석
규약을 완벽하게 기술하려면 문서화 주석에는 

1. 인자마다 **\@param** 태그를 달아야 하고,
2. 반환값 자료형이 void가 아니라면 **\@return** 태그도 달아야 하고,
3. 무점검 / 점검 여부에 상관없이 모든 예외에는 **\@throws** 태그도 붙어야 한다. 


## 문서화 주석 사용법
문서화 주석은 html문서로 변환되는데...

### 코드 주변에는 `{@code}` 태그를 사용하자.
아래 처럼 `{@code}` 는 해당 코드가 코드 서체로 표시되도록 한다. 
```java
/**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
```

### HTML 메타문자들, 즉 `<` 나 `&` 와 같은 문자들을 사용할 땐 `{@literal}` 태그를 사용할 것.
이 태그 안에 포함된 HTML마크업이나 Javadoc 태그는 전부 단순 문자로 취급.

### 모든 문서화 주석의 첫번째 `문장`은 해당 주석에 담긴 내용을 요약한 것.
클래스나 인터페이스의 멤버나 생성자에는 요약문이 없어야 함. 

### 요약문에 마침표가 자주 표현되는 경우에는 주의.
가장 좋은 방법은 `{@literal}` 를 사용하여 다음에 바로 공백이 오지 못하도록 한다.

```java
/**
* A college degree, such as B.S., {@literal M.S.} or Ph.D.
* College is a fountain of knowledge where many go to drink.
**/
public class Degree {...}
```

### 문서화 주석의 요약문은 완벽한 문장일 필요는 없다.
메서드나 생성자의 경우 요약문은 메서드가 무슨 일을 하는지 기술하는 완전한 **동사구**<sup>verb phrase</sup> 이다.
예를 들면 아래와 같다

```
- ArrayList(int capacity) - Constructs an empty list with the specified initial capacity.
- Collection.size() - Returns the number of elements in this collection.
```


### 클래스나 인터페이스의 요약문은 해당 클래스나 인터페이스로 만들어진 객체가 무엇을 나타내는지 표현하는 **명사구** 여야 한다.
필드의 요약문은 필드가 나타내는 것이 무엇인지를 설명하는 **명사구** 라야 한다. 

```
- TimerTask - A task that can be scheduled for one0time or repeated execution by a Timer.
- Math.PI - The double value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter.
```

### 제네릭 자료형이나 메서드 주석을 달 때는 모든 자료형 인자들을 설명해야함.
```java
/**
* An object that maps keys to values. A map cannot contain
* duplicate keys; each key can map to at most one value.
*
* ...
* @param <K> the type of keys maintained by this map
* @param <V> the type of mapped values
**/
public interface Map<K, V> {...}
```

### enum  자료형에 주석을 달 때는 상수 각각에도 주석을 달아줘야 한다.
**자료형** 이나 public **메소드** 뿐 아니라 **상수** 각각에도 주석을 달아 주어야 한다.

```java
/**
* 교향악단에서 쓰이는 악기 종류.
**/
public enum OrchestraSection {
	/** 플루트, 클라리넷, 오보에 같은 목관 악기. */
	WOODWIND,

	/** 프렌치 혼이나 트렘펫 같은 금관악기. */
	BRASS,

	/** 팀파니나 심벌즈 같은 타악기. */
	PERCUSSION,

	/** 바이올린이나 첼로 같은 현악기. */
	STRING;
}
```

### annotation 자료형에 주석을 달 때는 모든 멤버에도 주석을 달아야 한다.
멤버에는 마치 **필드**인 것처럼 명사구 주석을 달라. 자료형 요약문에는 동사구를 써서, 언제 이 자료형을 어노테이션으로 붙여야 하는지 설명하라.

```java
/**
* 지정된 예외를 반드시 발생시켜야 하는 테스트 메서드임을 명시.
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
	/**
	* 어노테이션이 붙은 테스트 메서드가 테스트를 통과하기 위해
	* 반드시 발생시켜야 하는 예외. (이 Clss 객체가 나타내는 자료형의
	* 하위 자료형이기만 하면 어떤 예외든 상관없다.)
	**/
	Class<? extends Throwable> value();
}
```

릴리즈 1.5 이후로 패키지 수준 문서화 주석<sup>package-level doc comment</sup> 는 package.html 대신 package-info.java 에 두어야 한다.


> 문서화 주석은 API 문서를 만드는 가장 효과적인 방법이다. 표준적으로 쓰이는 관습을 따르도록 일관된 스타일을 유지할 것. 임의의 HTML 태그를 주석에 넣을 수 있고, HTML 메타문자는 반드시 이스케이프 처리 해야 한다는 것을 기억하자.