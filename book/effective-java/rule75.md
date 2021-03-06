# 규칙 75. 사용자 지정 직렬화 형식을 사용하면 좋을지 따져 보라
바쁠 때는 구현에 집중한 나머지 Serializable 의 기본 구현을 사용하는데, 다음 릴리즈에는 버려야 할 구현체가 생긴 경우에 그 구현체를 완벽하게 버리는 것은 불가능하다.

## 어떤 직렬화 형식이 적절한지 살펴보지 않고, 무조건 기본 직렬화 형식<sup>default Serialized form</sup>을 그대로 받아들이지 마라.

## 기본 직렬화 형식은 그 객체의 물리적 표현이 논리적 표현과 동일할 때만 적절하다.

예를 들어 사람의 이름을 표현하는 아래 코드는 기본 직렬화 형식을 사용해도 무방하다.
```java
public class Name implements Serializable {
    /**
    * 성 : null 이 될 수 없다.
    * @serial  
    */
    private final String lastName;
    /**
    * 이름 : null 이 될 수 없다.
    * @serial  
    */
    private final String firstName;
    /**
    * 중간이름 : 없을 때는 null 이다.
    * @serial  
    */
    private final String middleName;
    
}
``` 
논리적으로 Name 에 선언된 필드들은 그 논리적 내용을 충실히 반영한다.
- 각 필드는 pivate final 인데도 @serial 을 명시하였는데, 이것은 public API, 즉 클래스의 직렬화 형식을 규정하기 때문.

### 설사 기본 직렬화 형식이 만족스럽다고 하더라도, 불변식이나 보안조건으로 인해 readObject를 구현해야 하느 경우도 많다.
- Name 클래스의 경우, lastName 과 firstName이 null이 될 수 없다는 조건을 만족하도록 구현해야 함.

## 객체의 물리적 표현 형태가 논리적 내용과 많이 다를 경우 기본 직렬화 형식을 그대로 받아들이면 네가지 문제가 발생
- 공개 API 가 현재 내부 표현 형태에 영원히 종속됨.
- 너무 많은 공간을 차지하는 문제가 생길수 있다. 
- 너무 많은 시간을 소비하는 문제가 생길 수 있다.
- 스택 오버플로 문제가 생길 수 있다.

## transient 키워드는 직렬화 제외하겠다는 의미.
기본 직렬화 형식을 사용하건 말건, transient 로 선언되지 않은 모든 객체 필드는 `defaultWriteObject` 메서드가 호출될 때 직렬화됨.

## 객체의 논리적 상태를 구성하는 값이라는 확신이 들기 전까진, 비-transient 필드로 만들어야 겠다는 결정을 내리지 말것??
- 기본 직렬화 형식을 사용하는 경우, `transient` 라벨이 붙은 필드들은 역직렬화 되었을 때, 기본값으로 초기화(int 면 0, boolean 이면 false)

## 객체를 직렬화 할때는, 객체의 상태를 전부 읽는 메서드에 적용할 동기화 수단을 반드시 적용할 것.

## 어떤 직렬화 형식을 사용하건, 무조건 직렬 버전 UID를 명시적으로 선언할 것.
- 기존 버전과 호환되지 않는 클래스를 만들어도 상관 없다면, 그냥 UID를 바꿔도 무방하다.
 