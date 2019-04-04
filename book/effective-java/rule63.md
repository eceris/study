# 규칙 63. 어떤 오류인지를 드러내는 정보를 상세한 메시지에 담으라
**무점검 예외**로 프로그램이 죽으면, 시스템은 자동적으로 해당 예외의 **스택 추적 정보**를 출력. 이 정보는 예외 객체의 **toString** 메소드가 **예외정보를 문자열**로 표현한 것인데, 이 문자열에 오류 원인에 **관계된 정보**를 **최대한** 많이 담는 것이 중요.

### 오류 정보를 포착해 내기 위해서, 오류의 상세 메시지에 `예외에 관계된` 모든 인자와 필드의 값을 포함시켜야 한다.

- 예를 들면, IndexOutOfBoundsException에 index의 상한과 하한, 그리고 현재 값을 표현 해주면 많은 것을 알 수 있다.
- 그렇다고 모든 값을 표현하라는 건 아닌데, 관련된 **실제 데이터**<sup>hard data</sup> 를 담는 것이 중요.

### 예외의 상세메시지<sup>detail message</sup>를 사용자 레벨 오류 메시지와 혼동해서는 안된다.
- 예외에 대한 상세 메시지는 프로그래머나 서비스 담당자가 오류 원인을 분석하기 위한 것이다.

오류를 적절히 포착하는 정보를 상세 메시지에 담는 방법은 아래와 같이 **상세한 정보를 요구하는 생성자**를 만드는 것.

```java
/**
*	Construct an IndexOutOfBoundsException.
*
*	@param lowerBound the lowest legal index value.
*	@param upperBound the highest legal index value plus one.
*	@param index the actual index value.
*/
public IndexOutOfBoundsException(int lowerBound, int upperBound, int index) {
	// 오류를 포착하는 상세 메시지 생성
	super("Lower Bound: " + lowerBound + ", Upper Bound: " + upperBound + ", Index:" + index);

	// 프로그램에서 이용할 수 있도록 오류 정보 보관
	this.lowerBound = lowerBound;
	this.upperBound = upperBound;
	this.index = index;
}
```
자바는 이런 숙어를 잘 사용하지 않는데, 추천할 만함.

> 예외 객체에 **오류 포착 정보**를 제공하는 접근자 메서드를 두어도 좋다. **오류를 복구**할 때 유용하기도 하고, 무점검 예외보다 **점검지정 예외**에 더 필요한 메서드이다. 