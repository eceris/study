# 3. 안정성 소개

- 엔터프라이즈 소프트웨어는 냉소적<sup>나쁜 일이 생길 거라 예상하고 그런일이 생겼을때 놀라지 않는</sup>이어야 한다. 
- **냉소적** 이어야 하는 이유는 단 기간의 비용 손해때문 만이 아니라 브랜드에 손상이 가기 때문

> 때문에 안정적인 소프트웨어가 중요하다. 놀라운 일은 안정적인 설계나 불안정적인 설계나 구현시에 비용은 거의 동일하다.

## 3.1 안정성이란?
- 일시적인 충격이나 지속적인 스트레스, 정상 처리를 방해하는 부품 고장이 있을 때라도 **트랜잭션**<sup>여기서 트랜잭션은 예를 들면, 옥션에서 `고객이 물건을 구매하는 것.` 이다.</sup>을 계속 처리하는 것.
- 기계 시스템에서 스트레스로 인해 형상의 변화가 생기는 것을 `스트레인`<sup>strain</sup> 이라고 하는데, 소프트웨어에도 마찬가지로 존재한다.
	- 어떤 모듈의 스트레인이 다른 시스템으로 전파 되기도 한다.

### 생명연장하기
- 시스템의 수명을 위협하는 주요 위험요소는 `메모리 누수` 와 `데이터 성장` 이다. 
- 메모리 누수와 같은 버그를 찾기는 굉장히 힘들기 때문에, 제이미터와 같은 부하테스트 도구를 사용하여 테스트하라. 
	- 힘들다면 적어도 중요한 부분은 테스트하라.

## 3.2 고장 유형
- 갑작스런 충격과 스트레인은 **크랙**을 발생시키기 시작하는데, 스트레스를 받을수록 **더 빠른 속도**로 전파.
- 이러한 크랙과 전파되는 방식, 손상된 결과를 **고장 유형** 이라고 한다. 
- 고장이 일어난다는 것을 받아들이고, 완충구간을 만들듯이 시스템의 나머지 부분을 보호하는 **크랙 차단기**<sup>crackstoppers</sup>를 설계 하자.

## 3.3 크랙 전파
- 2장의 CF프로젝트는 고장유형을 설계하지 않았다. 만약 설계 했다면 크랙이 전파되는 것을 막을 수 있었다.
	- pool의 자원이 고갈되었을 때, pool의 구성을 바꿀 수도 있었고,
	- 연결이 체크아웃 되었을때, 요청자를 영원히 블록하는 대신 제한된 시간동안 호출자를 블록하도록 설정할 수도 있었다.
	- RMI 를 사용하는 클라이언트에 **소켓 타임아웃**을 설정할 수도 있었고,
	- RMI 대신 웹서비스로 만들어서 **HTTP 요청에 제한시간**을 설정할 수도 있었다. 
	- 더 큰 의미의 아키텍쳐 이슈로 보면, **메시지 큐**를 사용해서 구현할 수도 있었다.

## 3.4 고장의 연쇄
- **동전 던지기**<sup>이전의 결과가 현재의 결과에 영향을 미치지 않음</sup> 와는 반대로 장애의 원인은 독립적이지 않다.
	- 예를 들면, DB가 느려지면 어플리케이션의 메모리가 바닥날 가능성이 커진다.
- 결합이 긴밀할 수록 **크랙**을 가속한다.
	- 밀접하게 결합되어 있으면, 원격지의 장애가 현재 시스템의 정지 문제로 바뀌게 된다.
- 가능한 모든 고장에 대비하는 한가지 방법은 아래와 같이 모든 예상 결과를 생각해 보는 것이다.
	- 초기 접속을 못하면 어떻게 되는가?
	- 접속하는데 10분이 걸리면?
	- 접속 후 끊어지면?
	- 내 쿼리에 응답하는데 2분 이상 걸리면?
	- 동시에 만 개의 요청이 오게되면?
	- Exception 에 대한 메시지를 남기려하는데 디스크풀 이라면?
- 위와 같은 무지막지한 방법이 항상 살아있어야 하는 화성 탐사기 같은 시스템을 제외하면 **비현실적이라고 생각할 수 있지만, 스트레스를 경감시키는 충격 흡수기는 꼭 필요하다.**

## 3.5 패턴과 안티 패턴
- 대게 같은 트리거, 같은 파손, 전파 등으로 두번 이상 장애를 일으키진 않으나, 시간이 지나면서 패턴이 드러남.
- **안정성 패턴** 은 크랙이 `전파` 되는 것을 막아 주는데, 이것은 시스템이 붕괴되는 대신 손상된 채로 **부분적인 기능을 유지**하는데 도움을 준다.


# 4. 안정성 안티패턴

- 애플리케이션의 범위는 처음에 회사 안에서 시스템을 통합했지만, 근래에 들어서는 회사를 넘나들며 통합하고 있다.
- 이로 인해 다양한 고장유형을 만나게 되고, 작은 시스템보다 더 빨리 고장난다.
- `지나친 결합`은 시스템의 한 부분에 있는 크랙을 계층이나 시스템 경계를 넘어 다른 곳으로 전파시키거나 증가시킨다.

> 그러나 중요한 것은 모든 것이 **고장나기 마련**이라는 사실이고, 항상 **최악을 가정하자는 것**이다.

## 4.1 통합 지점
- `통합지점` 은 시스템을 죽이는 최고의 킬러다.
	- 모든 소켓, 프로세스, 파이프나 원격 프로시져 호출은 행이 걸리거나 걸릴 수 있으므로

### 소켓 기반 프로토콜
- 공유메모리와 named pipe 를 제외한 **많은 것들이 소켓기반**이며, 소켓 계층에서 발생한 고장에 영향을 받기 쉬움.
- Socket.setTimeout() 은 중요하다
- 네트워크 고장은 빠른 고장과 느린 고장이 있는데, **느린 고장은 좋지 않다.**

#### 새벽 5시 문제
- 아무런 문제가 없는데 새벽 5시만 되면 행이 걸림.
- 소켓 연결은 추상화의 한 종류, 단순히 양 끝에 있는 컴퓨터의 메모리에 올라가있는 객체일 뿐.
	- 그래서 물리적인 연결이 끊어지더라도, 다시 연결가능하고
	- 양 끝에 있는 컴퓨터의 `연결`이 존재한다고 가정하면 `연결`은 지속된다. 
- 방화벽은 라우터일 뿐
	- 패킷을 검사해서 접근제어목록에 존재하는지 여부를 가지고, 거부 혹은 무시 한다.
	- 연결될 경우 내부의 연결테이블에 저장.
- **물리적**으로 연결이 끊어진 경우, 양 끝의 컴퓨터에게 연결이 끊어졌다는 것을 알릴 수 없음(**TCP의 스펙의 한계**)
- 양 끝에 있는 컴퓨터는 어떤 패킷도 받지 못한채 무한정 유효하다고 가정.
- JDBC 연결을 체크아웃하기 전에 ping query를 날리는데 이것 때문이었음.
	- 해결방안은 데이터베이스 서버가 제공하는 DCD<sup>dead connection detect</sup> 기능으로 클라이언트가 충돌한 것으로 판단되는 연결의 리소스를 해제한다.

### HTTP 프로토콜
- Apache Jakarta Commons 의 HttpClients 패키지는 연결 제한시간, 읽기 제한시간 등을 세밀하게 제어하는데, 이런 설정들이 **냉소적인 시스템**<sup>나쁜 일이 생길 거라 예상하고 그런일이 생겼을때 놀라지 않는</sup>을 가능하게 한다.

### 벤더 API 라이브러리
- 벤더 API 라이브러리를 믿지 말자. 안전하지 않다. 
	- 보통은 내부 리소스 풀, 소켓 읽기 호출, HTTP 연결 등에 대한 문제가 많이 존재.
- 이러한 벤더 API에서 블록된다면 애플리케이션이 멈춘다.

### 통합지점 문제에 맞서기
- [**테스트 하니스**](https://www.softwaretestingclass.com/difference-between-test-harness-vs-test-framework/)<sup>test harness</sup>를 통해 시스템과 네트워크 고장을 시뮬레이션 하자.
- JMeter 나 마라톤을 이용해 부하 테스트를 하자.

### 기억할 것
- 통합지점은 어떤식으로 고장나기 마련, 이런 고장에 대비하자.
- 통합지점의 고장에는 여러가지 형태가 존재하며, 이런 고장에 대비하자.
- 통합지점 고장에 경우 디버깅을 위해 추상화 계층을 들여다 보아야 하는데, 와이어샤크와 같은 네트워크 진단 툴이 도움이 되기도 한다.
- 원격지의 고장이 나의 문제가 되므로, 꼭 **방어적**으로 작성하자.
- 통합지점에서의 위험을 피하는데 도움이 되는 차단기, 제한시간, 미들웨어 등을 이용하여 방어적으로 프로그래밍 하자.

## 4.2 연쇄 반응
- 수평적 클러스터에서 단일지점 고장은 드물지만, 부하와 관련된 고장유형을 볼 수 있다.
- 첫번째 애플리케이션의 메모리 누수나 간헐적인 경쟁상태는 살아남은 노드에서도 나타날 가능성이 높다.

> - 알아야 할 것
- 특정 계층에 의존하는 계층은 반드시 스스로를 보호할 것.
- 메모리 누수는 연쇄 반응의 원인이 된다.
- 트래픽도 모호한 경쟁상태를 일으키기도 한다. 
- 연쇄반응을 막기 위해 차단기 패턴을 이용하자.

## 4.3 연속적인 고장
웹사이트와 웹 서비스를 포함하는 **엔터프라이즈 시스템의 표준 아키텍쳐**는 로드 밸런싱 형태로 서로 연결되어있는 별개의 팜이나 클러스트의 집합으로 표현.

- **통합지점** 다음으로 크랙을 가속시키는 인자가 **바로 연속적인 고장** 이다.
- 연속적인 고장을 위한 가장 효과적인 패턴은 **차단기**와 **제한시간**이다. 

### 크랙이 경계를 뛰어넘는 것을 막자.
- 다른 시스템이 다운되더라도 나의 시스템은 살리기 위해, 아래 계층에서 일어난 고장이 번지는 것을 막자.
- 다른 시스템을 호출할 때는 안전하게 호출 하자.

### 리소스 풀을 철저히 검사하자.
- 연속적인 고장은 **리소스 풀**에서 자주 일어난다. 
- 안전한 리소스 풀은 리소스를 얻기 위해 스레드가 기다릴 수 있는 시간을 항상 제한한다.

### 제한시간과 차단기를 이용해 방어하자.
- 차단기는 문제가 생긴 통합지점에 대한 호출을 피하여 시스템을 보호.
- 문제가 생긴 통합 지점을 호출하는 것에서 빠져나올 수 있음.

## 4.4 사용자들
사용자는 최악의 시간에 최악의 것을 정확하게 해내는 재능이 있다.

### 트래픽
- 모든 사용자는 시스템 자원을 소모하며, 시스템 용량에는 한계가 있다. 
- 트래픽이 늘어나면 기본적으로 **많은 세션**으로 인해 **가용 메모리가 줄어**드는데, 세션은 사용자의 마지막 요청 이후에도 일정 시간 남아있어, 다른 사용자가 사용할 수 있는 메모리를 차지하고 있음.
	- 이로 인해, OOM이 발생하거나 최악에는 로깅도 하지 못할 수 있다.
	- 최선의 방법은...
		1. **가능한 세션을 작게** 유지하거나,
		2. 메모리가 풍부할 때는 세션에서 객체를 유지하다가, 메모리가 여유롭지 않을 때는 자동으로 반환하는 방법 : **SoftReference** 

#### SoftReference
아래와 같이 구현 하면, 가비지 컬렉터가 알아서 재활용한다.
```java
MagicBean hugeExpensiveResult =  ... ;
SoftReference ref = new SoftReference(hugeExpensiveResult);
session.setAttribute(EXPENSIVE_BEAN_HODEL, ref);
```
다만, 만약 시스템 리소스의 상태가 좋지 않아, gc가 일어난다면, hugeExpensiveResult이 null 인 상황에 대응해야 한다.

> 크고 비용이 많이 드는 객체를 세션 밖에 둘 수 없을때 **SoftReference** 접근 방식을 이용하라.

### 서비스 비용이 많이 든다. 
- 가장 비용이 많이 드는 트랜잭션이 무엇인지 확인하고, 그것에 2배 혹은 3배에 부하 테스트를 수행하자. 

### 바람직하지 않은 사용자들
- 스크래핑이나 크롤링을 하는 사용자들의 리소스 사용은, 정상적인 사용자들의 리소스 사용을 막는다. 
- 이것을 막기 위해, 네트워크(하나의 아이피에서 특정 간격으로 요청을 할 경우 수상하다.) **방화벽**으로 막아라.
- 법적 조치, 변호사를 고용하여 스크래퍼들을 다루게 하라.

### 악의적인 사용자
- 잘 설정된 현대식 네트워크 장비는 **스크립트 키디**들이 보내오는 부하를 방어할 수 있는데, 세션 관리등은, **차단기**를 사용하여 방어하자.
- 하나의 **소스 IP**에서 오는 **1분당 15개의 연결을 한계값**으로 설정한다.(시스코 문서 참조)

### 기억 할 것.
1. 사용자의 세션마다 메모리를 사용하므로, 가능한 세션 메모리를 최소화 하자.
2. 사용자들은 이상하고도 제멋대로 행동하므로 테스트 하자.
3. 악의적인 사용자의 공격을 막기 위해, 네트워크 설계에 익숙하고, 프레임워크를 최신으로 유지하자. 또한 패치를 쉽게 만들자. 계속 공부하자. 
4. 사용자들은 떼지어 몰려올 것이므로 딥링크나 인기있는 URL 을 공격하는 **특별한 부하테스트**를 수행하자.

## 4.5 블록된 스레드
멀티 스레드 덕분에 엄청난 처리를 가능하게 했지만, 동시성 에러가 발생할 가능성도 그만큼 높아졌다.

잘 만든 증명된 라이브러리를 사용해야 하는 이유

1. 에러상황과 예외는 너무나 많은 상황을 만들기 때문에 테스트가 힘들다. 
2. 예상할 수 없는 상호작용 때문에 예전에는 안전했던 코드에 문제가 생길 수 있다. 
3. 타이밍이 핵심이다. 동시요청의 숫자와 함께 행이 걸릴 확률은 늘어난다. 
4. 개발자들은 자신의 어플리케이션에 만개의 요청을 하지 않는다.

### 블로킹 지점을 찾아라

```java
String key = (String)request.getParameter(PRAM_ITEM_SKU);
Availability avl = globalObjectCache.get(key);
```
위의 코드에서 블로킹 지점을 찾을 수 있는가? 아래와 같이 get 을 구현하였으므로 정상적으로 동작할 것이다.
```java
public synchronized Object get(String id) {
	Object obj = items.get(id);
	if (obj == null) {
		obj = create(id);
		items.put(id, obj);
	}
	return obj;
}
```
하지만 globalObjectCache를 과도하게 사용하면 용량 제한이 되기 쉽다. 이로 인해 문제가 발생하기도 한다. 

- 고객에게 `물건이 있는지 확인하지 못하면 사이트가 망가져도 되나요?` 라는 질문을 받을 수 있다.

### 서드파티 라이브러리
- 스레드를 블로킹 하는 악명 높은 원인.
- 실제로 라이브러리는 **자신만의 리소스 풀링**을 하므로, 무슨 일이 발생하는지 모를때도 있다. 
	- 그러므로 일부러 연결을 정체시키는 **테스트 하니스를 작성하여 테스트**하자.
- 라이브러리가 쉽게 고장난다면, 요청을 처리하는 스레드를 보호 하기 위해, 라이브러리 **외부에서 thread pool을 관리**해주어야 한다.
	- 다만 그전에 시간이 된다면, 더 나은 클라이언트 라이브러리를 찾도록 하라!

### 기억할 것
1. 블록된 스레드 안티패턴은 모든 고장의 직접적인 원인이다.
2. 리소스 풀을 철저히 검사할 것.
3. 입증된 프리미티브를 사용하자.(java의 concurrent유틸 같은...)
4. 제한시간으로 방어하자.(교착상태가 없을 수는 없다. 제한시간을 사용하자.)
5. 확인 할 수 없는 코드를 경계하자.(서드파티 코드를 테스트하자.)

## 4.6 자기부정 공격
자기부정 공격은 **시스템이 스스로에게 부하**를 가져다 주는 상황을 말한다. 예를 들면, 커머스 회사에서 XBOX를 10%의 가격으로 파는 이벤트로 인해 부하를 받는 상황이다.

- 이러한 상황은 수익을 창출하는 것 보다 나쁜 평판을 더 많이 얻게 한다.

### 극복
- **이중화**<sup>redundancy</sup>와 **후면 동기화**<sup>backside synchronization protocol</sup>를 사용하여 공유자원을 수평적으로 확장하라.
- 공유자원이 응답하지 않거나 사용불가일때, **고장대치 모드**<sup>fallback mode</sup>로 설계할 수 있다.
	- 예를 들면, 비관적 잠금<sup>persimistic locking</sup>을 사용하는 락 관리자를 낙관적 잠금<sup>optimistic locking</sup>을 사용하도록 대처할 수 있다.
- 전용서버가 다운될 때는 **빠른 고장**<sup>fast fail</sup>을 적용하여, 쓸데없는 응답을 기다리느라 지체하지 말자.

### 기억할 것
1. 자기부정 공격은 조직안에서 유래하므로, 조직원 간에 대화통로를 유지하자.
2. 공유되는 자원을 보호하자. 앞단에서의 부하를 뒷단에 노출시키지 말자.
3. 이벤트는 사용자들에게 아주 큰 매력이다. 이런 이벤트는 사이트에 부하를 촉진할 것이다.

## 4.7 확장 효과
`다대일` 혹은 `다대소` 관계인 시스템은 한쪽이 커질때 생기는 **확장효과**에 영향을 받는다.

### 일대일 통신
일대일 통신에서 각 인스턴스는 다른 모든 인스턴스와 직접적으로 통신하는데, 연결의 전체 갯수는 **인스턴스 개수의 제곱**으로 늘어난다.

### 공유된 자원
- 공유된 자원은 모든 멤버 시스템들이 사용해야 하는 편의시설 같은 개념.
- 공유된 자원에 부하가 지나치게 걸리면, 용량을 제한하는 병목구간이 됨.
	- 최악의 경우에는 분산된 캐시에 일관성을 제공하는 캐시관리자의 데이터 무결성이 깨지는 경우도 발생
	- 트랜잭션이 실패하여, 어떤 문제도 발생 가능

### 기억할 것
1. 확장효과를 발견하기 위해 QA환경과 운영환경을 잘 점검하자.
2. 일대일 통신을 경계하고, 일대다 통신과 같은 것으로 교체하자. 
3. 공유된 자원의 안정적인 운영을 위해, 부하테스트를 강력하게 실시하자.

## 4.8 불균형 용량
유틸리티 컴퓨팅은 사용량에 따라 비용을 청구하고, 사용량에 따라 자동으로 리소스를 추가하여 리소스 위기로부터 구해준다.

> 하지만 **환상**은 **환상일 뿐**이다.

갑작스런 자기부정 공격을 방어하기 위해 리소스를 크게 늘려서 만든다면 5년 중 하루를 제외하고 99퍼센트 유휴상태일 것이다.

\- 이 문제를 해결하기 위해 앞단은 **차단기**를 사용하여 뒷단의 압력을 줄여주고, 뒷단은 다른 종류의 트랜잭션을 위해 **칸막이**를 고려하자.

### 테스트하여 문제를 찾아내자.
- 테스트 하니스는 부하 상태에서 무기력해지는 뒷단의 시스템을 모방함으로써, 앞단의 시스템이 **우아하게 용량을 줄이는지 검증**하도록 도와줌.

#### 우리가 할 수 있는 일?
- 용량 모델링을 사용.
- 일반적인 부하로 테스트 하지말고, **두 배, 세 배**로 늘려 가장 비싼 트랜잭션에 대해 시스템을 테스트하자.

### 기억할 것
1. 서버와 스레드 갯수를 조사하여, 테스트와 운영을 비교하자.
2. 불균형용량은 확장 효과의 특별한 경우이다. 비슷한 현상을 잘 관찰하자.
3. 뒷단의 시스템으로 인해 앞단 시스템에 무슨 일이 벌어지는지를 확인하자.

## 4.9 느린 응답
느린 응답을 일이키는 것은 연결을 거부하거나 에러를 돌려주는 것보다 더 나쁘다.

#### 느린응답이 발생하는 이유

- 과도한 요청
- LAN 이 아닌 WAN 에서 자주 발생하는데, 복잡한 프로토콜 일때 더 자주 발생
- 직접 작성 가능한 낮은 수준의 소켓 프로토콜에서 전송버퍼가 고갈되고 수신버퍼가 꽉찬 애플리케이션에서 TCP 스톨을 자주 일이키기도 한다.

### 기억 할 것
1. 느린 응답은 연속적인 고장을 일으킨다. 상위 시스템에 영향을 준다는 의미.
2. 웹 사이트의 경우, 느린 응답은 더 많은 트래픽을 일으키는데, 그 이유는 사용자가 계속해서 새로고침 하기 때문
3. 빠른 고장을 고려하자. 평균 응답시간이 시스템에 허용된 시간을 초과할 때는 즉각적으로 에러를 발생시켜 보내자.
4. 메모리 누수나 자원 경쟁을 확인하자.

## 4.10 SLA 역전
**서비스 수준 협약**<sup>SLA</sup>는 회사가 서비스를 얼마나 잘 제공해야 하는가에 대한 계약이다. 이 지표를 만족시키지 못하면 위약금이 발생. 이러한 SLA는 비지니스 요구에 따라 **정량적으로 계약**해야 한다.

- SLA 역전 : 높은 가용성 SLA를 만족시켜야 하는 시스템이 더 낮은 가용성을 지닌 시스템에 의존하는 것.

### 해결 방법

1. 낮은 SLA 시스템을 **분리**<sup>decoupling</sup>한다.
	- 낮은 SLA 시스템이 죽더라도 애플리케이션은 우아하게 동작하도록 만들자.
2. 서비스 수준 협약을 작성 할때 신중하게 하자. 가볍게 99.99 가용성이라고 말하지 말자.

### 4.11 끝이 없는 쿼리 결과

1. 현실적인 데이터 양을 사용하자. 
	- 개발과 운영의 데이터셋은 다르다. 백만개의 쿼리 결과를 한꺼번에 반환하도록 하지말라.
2. **데이터 생산자에 의지하지 말자.**
3. 애플리케이션 수준의 프로토콜에 제한을 두자. 
	- 웹서비스, RMI, XML-RPC 등 의 프로토콜은 **엄청난 양의 객체**을 돌려받는데에 취약하다.





































