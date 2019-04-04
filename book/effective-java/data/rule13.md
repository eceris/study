# 규칙 13. 클래스와 멤버의 접근 권한은 최소화 하라.

잘 설계된 모듈은 내부의 데이터와 구현 세부사항을 감추고, API를 통해서만 접근한다는 개념이 정보 은닉 혹은 캡슐화이다.

#### 각 클래스와 멤버는 가능한 한 접근이 불가능하게 만들자.
- 정상 동작하는 범위 안에서 최대한 낮은 접근 권한을 부여할 것.


객체 필드<sup>instance field</sup>는 절대로 public으로 선언하면 안된다.
변경 가능 public 필드를 가진 클래스는 다중 스레드에 안전하지 않다.
길이가 0 아닌 배열은 언제나 변경 가능하므로, public static final 배열 필드로 두거나, 배열 필드를 반환하는 접근자<sup>accessor</sup>를 정의하면 안된다.

```java
public static final Thing[] VALUES = {...}; // 이렇게하면 보안 문제를 초래할 수 있다.
```
보통 IDE에서 자동 생성하면 위와 같은 배열 필드에 대한 접근자도 자동으로 생성한다. 주의 하자.


```java
private static final Thing[] PRIVATE_VALUES = {...}; // private로 바꾸고
public static final List<Thing> VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));
```
public 으로 선언된 배열을 private 로 바꾸고, 변경이 불가능한 public list를 하나 만든다.
위의 방법 외에 한가지 방법이 더 존재한다. 
```java
private static final Thing[] PRIVATE_VALUES = {...}; // private로 바꾸고
public static final Thing[] values() {
	return PRIVATE_VALUES.clone();
}
```
배열을 복사해서 반환하는 방법이 있는데 클라이언트의 성격에 따라 잘 사용하자. 

> 접근 권한은 가능한 난추고 public static final 필드를 제외한 나머지는 public으로 선언하지 마라. public static final 필드가 참조하는 객체는 변경불가능 객체로 만들어라.


