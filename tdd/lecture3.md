# TDD 좀 더 잘하기

### 테스트 케이스 클래스의 위치
일반적으로 가장 많이 사용하는 방법으로 **소스 폴더는 다르게, 패키지는 동일하게**

| 위치 | 설명 |
|:--------|:--------|
| /src/main/java | 제품 코드가 들어가는 위치 |
| /src/main/resources | 제품 코드에서 사용하는 각종 파일, XML 등의 리소스 파일들 |
| /src/test/java | 테스트 코드가 들어가는 위치 |
| /src/test/resources | 테스트 코드에서 사용하는 각종 파일, XML 등의 리소스 파일들 |


### 테스트 메소드 작성 방식
가장 선호하는 방식은 테스트 대상 **메소드의 이름** 뒤에 **추가적인 정보**를 기재.
```java
@Test
public void testWithdraw_마이너스통장인출() {...}

@Test
public void testWithdraw_잔고가0원일때() {...}
```

### 테스트 케이스 작성 접근 방식
테스트를 작성하는 데 있어서 어떤 형태로 접근할 것인가에 대한 논의가 필요하다.

#### 설계자와 개발자가 분리되어 있는 경우
- 설계자는 `테스트 시나리오`를 적어서 개발자에게 전달해야함. 보통 개발자는 테스트케이스를 최선을 다해 작성하거나 고민하지 않기 때문.

#### 개발자가 설계와 개발을 함께 하는 경우
- 제품자체에 주인의식을 갖고 스펙을 작성할 수 있다면, 애자일에서 얘기하는 사용자 스토리와 테스트 케이스 조건을 스토리 카드에 적어서 진행 한다.

#### 무엇을 테스트 케이스로 작성할 것인가?

1. **해피데이 시나리오**<sup>happy day scenario</sup>
```
정상적인 흐름일 때 동작해야 하는 결과값을 선정하는 방식
```
2. **블루데이 시나리오**<sup>blue day scenario</sup>
```
발생할 수 있는 예외나 에러 상황에 대한 결과값을 적은 방식
```

`실용주의 프로그래머를 위한 단위테스트 with JUnit` 에서는 아래와 같은 질문을 해보라고 권고.
- 결과가 옳은가?
- 모든 경계조건이 옳은가?
- 역<sup>inverse</sup> 관계를 확인할 수 있는가?
- 다른 수단을 사용해서 결과를 교차확인할 수 있는가?
- 에러 조건을 강제로 만들어낼 수 있는가?
- 성능이 한도 내에 있는가?

### TDD의 한계

#### 동시성 문제 
- **동시성**<sup>concurrency</sup>이 걸려있는 코드에 대한 테스트 코드 자체를 무결하게 유지하기가 매우 어려움.

#### 접근 제한자<sup>private/ protected 메소드</sup>
- 기본적으로 private 메소드는 테스트 불가능, 그러나 `public 으로 되어있는 메소드만 테스트해도 무방하다` 라는 의견이 많다. 실제로 public 메소드에 의해 private 메소드가 테스트 된다.

#### GUI
- **화면에 해당하는 영역**에서는 **최대한 업무 코드를 배제**하는 형태로 작성해야 함.

> 기본적으로 TDD는 시간과 노력이 들어감. 그래서 당장은 이점을 크게 못 누릴 수도 있음. 하지만 TDD를 `적용하면 품질이 높아지고, 시간지연을 상쇄하고도 남는다.` 라는 의견이 많으니 꼭 실천하자.