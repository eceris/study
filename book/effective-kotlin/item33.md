# 아이템33 : 생성자 대신 팩토리 함수를 사용하라

- 팩 토리 함수는 기본 생성자가 아닌 추가적인 생성자(secondary constructor)와 경쟁 관계

 여러 종류가 있는데...
	1. companion 객체 팩토리 함수
	2. 확장 팩토리 함수
	3. 톱레벨 팩토리 함수
	4. 가짜 생성자
	5. 팩토리 클래스의 메서드


## companion 객체 팩토리함수
1. from : 파라미터를 하나 받고, 같은타입의 인스턴스 반환 
```kotlin
val date : Date = Date.from(instant)
```
2. of : 파라미터를 여러개 밭고, 이를 통합해서 인스턴스를 반환
```kotlin
val faceCards : Set<Rank> = EnumSet.of(JACK, QUEEN, KING)
```

3. valueOf: from 또는 of 와 비슷하나 의미를 좀 더 쉽게.

## 정리
- 팩토리 함수를 정의하는 가장 일반적인 방법은 companion 객체를 사용하는 방법