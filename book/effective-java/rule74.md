# 규칙 74. Serializable 인터페이스를 구현할 때는 신중하라.
직렬화 가능 클래스를 만들기 위해 지금 해야할 일은 무시할 만한 수준이지만, 장기적으로 해야할 일은 그보다 훨씬 많아질 수도 있다.

## Serializable 구현의 가장 큰 문제는 클래스를 릴리즈 하고 나면 구현을 유연하기 바꾸기가 어려워진다
- Serializable 을 구현하면, 그 클래스의 바이트 스트림 인코딩<sup>직렬화 형식 serialized form</sup>도 공개되므로 한번 배포되면 영원히 지원해야 함.
- 기본 직렬화 형식이라면, 그 클래스의 private 도 공개 api가 됨(직렬화하는 순간 인스턴스 한개가 더 생기는 거니까?)

## Serial version UID 를 명시하자.
- 명시하지 않으면 시스템이 복잡한 절차<sup>클래스 이름, 구현하는 인터페이스 이름, 모든 멤버 변수를 조합</sup>를 거쳐서 임의로 생성하는데, 이것들이 하나라도 변경되면 serialVersionUID도 함께 변경되어 호환성이 깨지며, 결과적으로 InvalidClassException이 발생

## Serializable 을 구현하면, 버그나 보안 취약점이 발생할 가능성이 높아짐.
- 직렬화는 언어 외적인 객체 생성방법으로, [불변식 훼손이나 불법 접근 문제에 쉽게 노출된다.](rule76.md)

## Serializable 을 구현하면, 새 버전 클래스를 릴리즈 하기 위한 부담이 높아짐.
- 새로 만든 클래스가 예전 릴리즈에서 역직렬화 가능한가?, 그 역도 가능하진 검사해야 한다. 
- 객체가 원래 객체의 충실한 사본<sup>faithful copy</sup>인지도 확인해야 한다.

## 기본적 규칙은 대부분의 컬렉션 클래스와 마찬가지, Date 나 BigInteger 같은 값 클래스는 Serializable 을 구현해야 한다. 
- 스레드 풀과 같은 활성 개체<sup>active entity</sup>를 나타내는 클래스는 Serializable 을 구현할 일이 거의 없다.

## 상속을 염두에 둔 클래스는 Serializable 을 구현하지 않는 것이 바람직, 인터페이스는 가급적 Serializable 를 상속받지 말아야 한다.
- 그러나 marker 형태의 Serializable 구현은 상황에 따라 다름.

상속을 고려해 설계된 클래스 가운데 Serializable 을 구현한 것으로는 Throwable, Component, HttpServlet 이 존재.
Throwable 이 Serializable 을 구현한 것은 원격에서도 예외를 받기 위해.
HttpServlet 이 Serializable 을 구현한 것은 세션상태를 캐시하기 위해.
