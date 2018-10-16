# 규칙 44. 모든 API 요소에 문서화 주석을 달라

사용할 수 있는 API 가 되려면 문서가 있어야 한다. 

좋은 API 문서를 만드려면 API에 포함된 모든 **클래스, 인터페이스, 생성자, 메소드 그리고 필드 선언** 에 **문서화 주석**을 달아야한다.





### 메서드
메서드에 대한 문서화 주석은 메서드와 클라이언트 사이의 규약<sup>contract</sup> 을 간단하고 명료하게 설명해야한다.

문서화 주석에는 메서드의 선행조건<sup>precondition</sup> 과 후행조건<sup>postcondition</sup> 을 나열해야 함.


#### 선행 조건
클라이언트가 메서드를 호출하려면 반드시 참<sup>true</sup>가 되어야 하는 조건들.

1. 무점검 예외<sup>unchecked exception</sup> 에 대한 \@throws 태그를 통해 암묵적으로 기술
2. 관계된 인자의 \@param 태그를 통해 명시.

#### 후행 조건
메서드 실행이 성공적으로 끝난 다음에 만족되어야 하는 조건들.


#### 메서드 주석
규약을 완벽하게 기술하려면 문서화 주석에는 
1. 인자마다 \@param 태그를 달아야 하고,
2. 반환값 자료형이 void가 아니라면 \@return 태그도 달아야 하고,
3. 무점검 / 점검 여부에 상관없이 모든 예외에는 \@throws 태그도 붙어야 한다. 


### 문서화 주석 사용법
문서화 주석은 html문서로 변환되는데...

#### 코드 주변에는 `{@code}` 태그를 사용하자.
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

#### HTML 메타문자들, 즉 `<` 나 `&` 와 같은 문자들을 사용할 땐 `{@literal}` 태그를 사용할 것.
이 태그 안에 포함된 HTML마크업이나 Javadoc 태그는 전부 단순 문자로 취급.

#### 모든 문서화 주석의 첫번째 `문장`은 해당 주석에 담긴 내용을 요약한 것.

- 클래스나 인터페이스의 멤버나 생성자에는 요약문이 없어야 함. 
- 요약문에 마침표가 자주 표현되는 경우에는 주의.
- 클래스나 인터페이스의 요약문은 해당 클래스나 인터페이스로 만들어진 객체가 무엇을 나타내는지 표현하는 **명사구** 여야 한다.
- 제네릭 자료형이나 메서드 주석을 달 때는 모든 자료형 인자들을 설명해야함.
- enum  자료형에 주석을 달 때는 상수 각각에도 주석을 달아줘야 한다.
- annotation 자료형에 주석을 달 때는 모든 멤버에도 주석을 달아야 한다.
