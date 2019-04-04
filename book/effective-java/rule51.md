# 규칙 51. 문자열 연결 시 성능에 주의하라
문자열 연결<sup>concatenation</sup> 연산자 `+` 는 여러 문자열을 하나로 합하는 편리한 수단. 한줄 정도를 출력하는데는 아무런 문제가 없지만,
n개의 문자열에 연결 연산자를 반복 적용할 경우 드는 시간은 n^2 에 비례.

왜냐면 문자열은 [**변경 불가능** 하기 때문](rule15.md)

### 만족스런 성능을 얻으려면 String 대신 StringBuilder 를 써야 한다.

```java
//문자열을 연결하는 잘못된 방법 - 성능이 엉망이다
public String statement() {
	String result = "";
	for (int i = 0; i < numItems(); i++) {
		result += lineForItem(i); // String concatenation
	}
	return result;
}

public String statement() {
	StringBuilder b = new StringBuilder(numItems() * LINE_WIDTH);
	for (int i = 0; i < numItems(); i++) {
		b.append(lineForItem(i)); // String concatenation
	}
	return b.toString();	
}
```

책에는 동기화<sup>synchronization</sup>을 지원하는 StringBuffer를 더이상 지원하지 않는다고 나와있지만 실제는 그렇지 않음.
멀티 스레드 환경에서 동기화가 필요하다면 **StringBuffer**를 사용할 것.


> 성능이 걱정된다면 많은 문자열을 연결할 때 `+` 연산자를 피할 것.
