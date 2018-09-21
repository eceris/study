# 규칙 12. Comparable 구현을 고려할 것

알파벳 순서나 값의 크기, 시간적 선후관계 처럼 명확한 자연적 순서를 따르는 값 클래스를 구현할 때에는 꼭 Comparable 인터페이스 구현을 고려할 것.

객체의 값이 주어진 인수보다 작으면 음수, 같으면 0, 크면 양수를 반환


Object의 equals메소드와 비슷한 특성을 지니고 있지만, 단순한 동치성 검사 외에 순서비교가능.
Comparable 인터페이스를 구현하는 클래스의 객체는 자연적 순서를 가짐.
자바 라이브러리에 포함된 모든 객체는 Comparable 을 구현 


클래스에 선언된 중요 필드가 여러개인 경우, 필드 비교 순서가 중요.
```java
public int compareTo(PhoneNumber pn) {
	// 지역번호 비교
	if (areaCode < pn.areaCode) {
		return -1;
	}
	if (areaCode > pn.areaCode) {
		return 1;
	}
	// 지역번호가 같으니 국번 비교
	if (prefix < pn.prefix) {
		return -1;
	}
	if (prefix > pn.prefix) {
		return 1;
	}
	// 지역번호와 국번이 같으므로 회선번호 비교 
	if (lineNumber < pn.lineNumber) {
		return -1;
	}
	if (lineNumber > pn.lineNumber) {
		return 1;
	}

	return 0; //모든 필드가 일치
}
```
정상 동작하는 메소드 이지만 compareTo 메소드의 일반 규약이 반환값의 부호만 명시할 뿐 크기에는 관심이 없으므로 아래와 같이 개선한다. 

```java
public int compareTo(PhoneNumber pn) {
	// 지역 번호 비교
	int areaCodeDiff = areaCode - pn.areaCode;
	if (areaCode != 0) {
		return areaCodeDiff;
	}

	// 지역 번호가 같으니 국번 비교
	int prefixDiff = prefix - pn.prefix;
	if (prefixDiff !=0 ) {
		return prefixDiff;
	}

	// 지역번호와 국번이 같으므로 회선 번호 비교
	return lineNumber - pn.lineNumber;
}
```
위의 방법은 비교할 필드가 음수가 아닐때만 적용 가능.
> i-j가 int형의 값을 넘어갈 겨웅 overflow 된다. 이런 경우 찾기도 힘든 장애를 일으킨다.

