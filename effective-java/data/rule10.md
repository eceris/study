# 규칙 10. toString()은 항상 재정의 하라
Object 클래스가 toString() 메소드를 제공하지만 @다음에 해시코드<sup>PhoneNumber@11cd9s</sup>가 붙은 형태이다. equals()와 hashcode() 보다는 덜 중요하지만 클래스를 좀 더 쾌적하게 사용하기 위해 구현하라.
```
{Jenny=PhoneNumber@11cd9s}
보다는 
{Jenny=(707) 867-5309)}
가 낫다
```
가능하다면 toString() 메소드는 객체 내의 중요정보를 전부 담아 반환할 것.

toString()이 반환하는 문자열의 형식을 명시하건 그렇지 않건 간에 어떤 의도 인지는 문서에 분명하게 남길 것.
```java
/**
*	전화번호를 문자열로 변환해서 반환한다.
*	문자열은 "(XXX) YYY-ZZZZ" 형식으로 14개 문자로 구성.
*	XXX 는 지역번호, YYY 는 국번, ZZZZ 는 회선번호다 
*	전화번호의 각 필드가 주어진 자리를 다 채우지 못할 경우 필드 앞에는 
*	0이 붙는다. 예를 들어, 회선 번호가 123일 경우, 위의 문자열 마지막 필드에
*	채워지는 문자열은 "0123"이다.
*
*	지역번호를 닫는 괄호와 국번 사이에는 공백이 온다는 것에 주의하자.
*/
@Override
public String toString() {
	return String.format("(%03d) %03d-%04d", areaCode, prefix, lineNumber);
}
```
만약 형식을 명시하지 않기로 했다면 아래와 같이 작성하자.

```java
/**
*	물약(potion)의 내용을 간단히 설명하는 문자열을 반환한다. 
*	정해진 문자열 형식은 없으며, 바뀔 수 있다.
*	하지만 대체로 아래와 같은 문자열이 반환된다.
*	
*	"[Potion #9: type=love, smell=turpentine, look=inida ink]"
*/
@Override
public String toString() { ... }
```
이렇게 만들 경우, client 개발을 하는 스스로가 문제에 대한 책임을 져야 한다.

toString() 이 반환하는 문자열에 포함되는 정보는 모두 getter를 통해 가져올 수 잇도록 하자.(다른 개발자들을 위해)
