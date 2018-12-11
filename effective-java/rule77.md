# 규칙 77. 개체 통제가 필요하다면 readResolve 대신 enum 자료형을 이용하라
[규칙 3](rule3.md)에서 아래의 싱글턴 예제를 다루었다.
```java
public class Elvis {
    public static final Elvis INSTANCE - new Elvis();
    private Elvis() { ... }

    public void leaveTheBuilding() { ... }
}
```
이 클래스는 선언부에 `implements Serializable` 을 붙이는 순간 **더이상 싱글턴 클래스**가 아니다.

- 기본 직렬화 형태를 쓰건,
- 사용자 정의 직렬화 형태를 쓰건,
- 클래서에 명시적으로 readObject 메서드가 정의되어 있건,

이 객체는 더이상 싱글턴이 아니다.

## readResolve 를 이용하면 readObject가 만들어낸 객체를 다른 것으로 대체 할 수 있다. 
역직렬화 이후에 readResolve가 호출되는데, 새로 만들어진 객체 대신, 이 메서드가 반환하는 객체가 사용자게에 반환.

아래와 같이 readResolve 를 구현하면 싱글턴 속성을 만족하기에 충분
```java
// 개체 통제 를 위해 readResolve 를 활용한 사례
// 이보다는 더 잘할 수 있다.
private Object readResolve() {
    // 유일한 Elvis 객체를 반환하고, Elvis를 가장한 새 객체는 바로 GC 가 처리하도록 한다.
    return INSTANCE;
}
```
위의 메서드는 역직렬화된 객체는 무시하고 클래스가 초기화될 당시에 만들어진 유일한 Elvis 객체를 반환

개체 통제를 위해 readResolve를 활용할 때는, 객체 참조 자료형으로 선언된 모든 객체 필드를 반드시 transient로 선언할 것.
- 그렇지 않으면 MutablePeriod 공격과 비슷한 기술을 사용하여 readResolve 메소드가 호출되기 전에 역직렬화된 객체에 대한 참조를 가로챌수 있음.


> 개체 수와 관련된 **불변식**을 강제 하고 싶을 때는 가능하면 **enum** 을 이용하라. 그럴 상황이 되지 않는다면 **readResolve 메소드를 구현**해야 할 뿐 아니라 클래스의 모든 필드를 transient로 선언해야 안전하다.