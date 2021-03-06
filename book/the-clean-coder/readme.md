

# 1장. 프로의 마음가짐

## 함부로 바라지 마라
## 책임감을 가져라
## 무엇보다도 해를 끼치지 마라
### 구조에 해를 끼치지 마라.
코드를 조금 바꾸는 일은 언제 해야 할까?
모듈을 살펴볼 때마다 작고 가벼운 변화를 더해 구조를 개선해야 한다. 보통은 이런 일을 위험하다고 생각하는데, 코드를 망칠까봐 두려워하기 때문인데 이건 테스트코드가 없기 때문이다. 결국 제일 걸리는 문제는 테스트다. 

코드 바꾸기가 무섭지 않다는 사실을 어떻게 증명할 수 있을까? 
-> 항상 코드를 바꾸면 된다.

## 직업 윤리

# 2장. 아니라고 말하기
## 반대하는 역할
## 손익관계가 높을 때
## 팀 플레이어
### 노력해보기
- 일정 안에 일을 끝마치지 못하는 상황에 "알았어요. 노력해볼게요." 라고 말하는 것은 `추가로 쏟을 힘이 있다는 것` 이고, 평소에는 `최선을 다하지 않았다는 것` 이다. 예비로 힘을 남겨뒀다는 의미다.

## 예라고 말하는 비용
## 훌륭한 코드는 불가능한가?
## 코드 코드 임파서블

# 3장. 예라고 말하기
## 약속을 뜻하는 말
## 예라고 말하는 법 익히기
## 결론
- 프로는 모든 업무 요청에 예라도 대답할 필요가 없다. 하지만 `예`라고 대답할 수 있는 창의적인 방법을 찾는 데 고심해야 한다. 
- 예라고 대답할 때에는 약속을 뜻하는 언어를 사용해서 내뱉은 말에 모호한 부분이 없도록 해야 한다.

# 4장. 코딩
## 준비된 자세

새벽 3시에 짠 코드 

지쳤을 때는 코드를 만들지 머라. 헌신과 프로다운 모습은 무턱대고 많이 알하는 데서가 아니라 원칙을 지키는 모습에서 나온다. 충분히 자고 건강을 챙기고 건전한 생활습관으로 하루에 8시간씩 "충실히" 일하자.

## 몰입 영역
## 진퇴양난에 빠진 글쟁이
## 디버깅
## 속도 조절
## 일정을 못 지키다
## 도움

# 5장. 테스트 주도 개발

## 배심원 등장
## TDD의 세 가지 법칙
## TDD와 관련 없는 사실

# 6장. 연습

## 연습의 배경지식
## 코딩 도장
## 경험의 폭 넓히기
## 결론

# 7장. 인수 테스트

## 요구사항 관련 의사소통
## 인수 테스트
### 완료에 대한 정의
- '모든 테스트를 생각해서 작성하면 일이 많을 것이다.' 라고 생각하지만 수작업으로 테스트를 하고 또 하는 거에 비하면 아무것도 아니다.
- 인수테스트는 시스템의 명세<sub>spec</sub>를 명확히 하는 작업일 뿐이다.

### GUI 및 다른 문제점
- 테스트들은 GUI의 취약성으로 인해 깨지기 쉽다. 가능한 최소한으로 유지하라.
- GUI 테스트가 늘어날수록 그 테스트를 유지해나갈 가능성은 낮아진다.

## 결론
- 서로에게 말없이 손만 흔든 다음, 상대방이 이해했을 거라 가정해버리면 일은 너무나 쉬워진다. 하지만, 양 당사자들이 완전히 `다른` 개념으로 이해하고 헤어지는 경우가 너무 많다. 그러니 세부사항을 명확하게 하기위해 테스트코드를 작성하자.

# 8장. 테스트 전략

##QA는 오류를 찾지 못해야 한다

##테스트 자동화 피라미드

### 단위테스트
- 프로그래머를 위한 테스트

### 컴포넌트테스트
- 인수테스트의 일종으로, 보통 해당 업무 규칙을 테스트.
- 테스트 대상이 아닌 컴포넌트는 mock으로 분리.

### 통합테스트
- 업무 규칙을 테스트하지 않고, 컴포넌트끼리 얼마나 잘 어울리는지를 테스트.
- 성능테스트나 처리량<sub>throughput</sub>을 테스트하는 단계.

### 시스템테스트
- 시스템 전체를 대상으로 하는 테스트

### 탐색 테스트
- 수동 테스트
- `오류사냥`을 목표로 시스템을 망가뜨리는 것을 목표로 하기도 함.

##결론
- 인수테스트는 요구사항을 표현하고 강화하는 가치 있는 방법.

9장. 시간 관리

## 회의
- 프로는 당장의 이익이나 큰 이득이 없는 회의에는 적극적으로 참석을 거부한다.

### 거부하기
회의 참석을 요청받으면, 자신의 참석이 지금 처리중인 업무에 도움이 되지 않는다고 판단되면 회의 참석을 거부하라.
- 자신의 손실과 타인의 이득을 잘 비교하라

### 빠져나오기
- 쓸모없는 회의에 발이 묶였다면, 예의 바르게 회의에서 빠져나올 방법을 찾을 것.
- 회사의 시간과 자금을 현명하게 사용해야 할 의무가 있으므로 시간을 잘 사용하자

## 집중력 마나
- 회의에서 집중력 마나를 다 써버리면 코딩에 쓸 마나가 부족하게 된다

## 타임박스와 토마토
## 피하기
## 막다른 골목
## 진흙탕, 늪, 수렁, 기타 엉망진창
- 진흙탕은 막다른 길보다 무서운게, 앞으로 갈수는 있지만 뭔가 애매하게 걸리는 것. 설계가 이상하다고 느낄쯔음 앞으로 진행하지말고, 다시한번 돌아보고 고쳐라. 이때가 되돌아가기 가장 쉬운 지점이다.\

## 결론

# 10장. 추정
## 추정이란 무엇인가?
## PERT
## 업무 추정하기
### 광대역 델파이
- 업무에 대해 토론하고, 추정하여 합의에 도달할 때까지 토론과 추정을 반복하는 것.

#### 날아다니는 손가락
- 업무를 추정하는데 손가락을 동시에 내밀어 만장일치가 아닌 경우 왜 그런지에 대해 토론하여 추정하는 방법

## 큰 수의 법칙
## 결론

# 11장. 압박
## 압박 피하기
- 무언가에 의해 압박이 느껴지더라도, 평소에 하던 것 처럼 테스트코드를 짜고 짝프로그래밍으로 의연하게 대처하라.

## 압박 다루기
- 당황하지말고 스트레스를 관리하자. 이런 압박이 왜 생겼는지에 대해 팀동료와 상사에게 알리자.
- 이러한 압박이 오더라도 평소에 하던 것 처럼 대처하자.
## 결론

# 12장. 함께 일하기
## 프로그래머 vs 보통 사람들
## 소뇌
## 결론
- 정말로 프로그래밍으로 일과시간을 보내고 싶다면, 정말로 우리가 마주해야할 것은 사람이다.

# 13장. 팀과 프로젝트
## 갈아서 만들었나요?
## 결론

# 14장. 스승과 제자 그리고 장인 정신
## 실패의 정도
## 스승과 제자
## 수습기간
## 장인 정신
## 결론

