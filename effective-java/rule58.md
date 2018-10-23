# 규칙 58. 복구 가능 상태에는 점검 지정 예외를 사용하고, 프로그래밍 오류에는 실행지점 예외를 사용하라

자바는 세가지 종류의 `throwable`<sup>checked exception, runtime exception, error</sup> 을 제공

### 호출자<sup>caller</sup> 측에서 복구 할 것으로 여겨지는 상황에 대해서는 점검지정 예외<sup>checked exception</sup>를 사용.

- 메소드에 선언된 점검지정 예외는 메소드를 호출하면 해당 예외와 관련된 상황이 발생할 수 있음을 클라이언트에게 알리는 역할.
- 점검지정 예외를 준다는 것은 그 상태를 복구할 권한을 준다는 것.

### 무점검 `throwable` <sup>unchecked</sup> 에는 런타임 예외와 오류 두가지가 있으며 동작방식은 같다.
- 둘다 catch로 처리할 필요가 없으며, 일반적으로는 처리하면 안됨

#### 프로그래밍 오류를 표현할 때는 런타임 예외를 사용하라.
- 대부분의 런타임 예외는 선행조건의 위반<sup>precondition violation</sup>을 의미.
- 보통 오류는 JVM의 자원부족<sup>resource deficiency</sup> 혹은 불변식 위반<sup>invariant failure</sup> 등, 더이상은 프로그램을 실행할 수 없는 상태에 도달했음을 알리기 위해 사용.
- Error 의 하위클래스는 만들지 말자.

#### 사용자 정의 무점검 throwable 은 RuntimeException의 하위 클래스로 만들어야 한다.
- Exception, RuntimeException, Error 가 아닌 Throwable을 구현할 수도 있지만, 절대로 하면 안된다. 나은 점이 없다.

> 요약하자면, 복구 가능한 상태에는 **점검지정 예외** 를 사용하고, 프로그래밍 오류를 나타내고 싶을 때는 **실행지점 예외**를 사용하라는 것.