# 한계돌파를 위한 노력, Mock을 이용한 TDD

## Mock 객체
- 모듈의 겉모양이 실제 모듈과 비슷하게 보이도록 만든 **가짜 객체**로 실제 객체를 만들기엔 **비용과 시간**이 많이 들거나 **의존성**이 길게 걸쳐져 있어 제대로 구현하기 어려울 경우, 이런 가짜 객체를 만들어 사용.

### 언제 Mock 객체를 만들 것인가?

대부분의 경우 모듈이 가진 `의존성`이 테스트 케이스 작성을 어렵게하는 원인인데, 이게 사실 테스트를 힘들게 함. 그래서 `의존성`을 해결하기 위해 Mock 객체를 사용.

#### 1. 테스트 작성을 위한 환경 구축이 어려워서
- 환경 구축을 위한 작업 시간이 많이 필요한 경우
- 특정 모듈을 갖고 있지 않은 경우
- 타부서의 협의나 정책이 필요한 경우

#### 2. 테스트가 특정 경우나 순간에 의존적이라서
- 연결 접속시간 제한<sup>timeout</sup>을 테스트할 경우.

#### 3. 테스트 시간이 오래 걸려서
- 테스트 실행시간 단축을 위해 


## Mock에 대한 기본적인 분류 개념, 테스트 더블

### 테스트 더블 
오리지널 객체를 사용해서 테스트를 진행하기가 어려울 경우 이를 대신해서 테스트를 진행할 수 있도록 만들어주는 객체를 지칭한다

#### 더미 객체<sup>Dummy Object</sup>
더미 객체는 말 그대로 멍청한 모조품, 단순한 껍데기에 해당한다. 오로지 인스턴스화될 수준
```java
public class DummyCoupon implements ICoupon {
	@Override public int getDiscountPercent() {
		return 0;
	}
	@Override public String getName() {
		return null;
	}
	@Override public boolean isAppliable(Item item) {
		return false;
	}
	@Override public boolean isValid() {
		return false;
	}
	@Override public void doExpire() {

	}
}
```
더미 객체는, 단지 **인스턴스화**된 객체가 필요할 뿐 해당 객체의 **기능까지는 필요하지 않은** 경우에 사용. 더미 객체의 메소드가 호출됐을 때, 정상동작은 보장되지 않음.

#### 테스트 스텁<sup>Test Stub</sup>
테스트 스텁은 더미 객체가 마치 실제로 동작하는 것처럼 보이게 만들어놓은 객체. 더미객체와 다른 점은 객체의 **특정 상태를 가정**해서 만들었다는 점.
```java
public class StubCoupon implements ICoupon {
	@Override public int getDiscountPercent() {
		return 7;
	}
	@Override public String getName() {
		return "VIP 고객 한가위 감사쿠폰";
	}
	@Override public boolean isAppliable(Item item) { return true; }
	@Override public boolean isValid() { return true; }
	@Override public void doExpire() { }
}
```
- 단지 인스턴스화될 수 있는 객체 수준이면 **더미**
- 인스턴스화된 객체가 특정 상태나 모습를 대표하면 **스텁**

스텁은 특정 객체가 상태를 대신해주고 있긴 하지만, 하드 코딩된 형태.

#### 페이크 객체<sup>Fake Object</sup>
모양만으로는 더미와 스텁의 경계가 모호할 수 있던 것처럼, 스텁과 페이크의 경계도 사실 딱 구분 짓기는 어려운데, 페이크는 여러 개의 **인스턴스를 대표**할 수 있는 경우이거나, 좀 더 복잡한 구현이 들어가 있는 객체를 지칭한다.
```java
public class FakeCoupon implements ICoupon {
	List<String> categoryList = new ArrayList(); // 내부용으로 사용할 목록
	public FakeCoupon (){
		categoryList.add("부엌칼");
		categoryList.add("아동 장난감");
		categoryList.add("조리기구");
	}
	@Override public boolean isAppliable(Item item) {
		if( this.categoryList.contains( item.getCategory() )) {
			return true;
		}
		return false;
	}
	…
	…
```
페이크 객체는 복잡한 로직이나, 객체 내부에서 필요로 하는 다른 외부 객체들의 동작을, 비교적 단순화하여 구현한 객체.


#### 테스트 스파이<sup>Test Spy</sup>
보통은 호출 여부를 몰래 감시해서 기록했다가, 나중에 요청이 들어오면 해당 기록 정보를 전달해주는 목적으로 만들어진 테스트 더블을 테스트 스파이라고 함.
```java
public class SpyCoupon implements ICoupon {
	List<String> categoryList = new ArrayList();
	private int isAppliableCallCount;
	
	@Override public boolean isAppliable(Item item) {
		isAppliableCallCount++; // 호출되면 증가
		if(this.categoryList.contains(item.getCategory())) {
			return true;
		}
		return false;
	}
	public int getIsAppliableCallCount(){ // 몇 번 호출됐나?
		return this.isAppliableCallCount;
	}
…
```

일반적으로 테스트 스파이는 **아주 특수한 경우**를 제외하고 잘 쓰이지 않는데, 테스트 스파이가 필요한 경우에도 Mock 프레임워크를 이용하는 것이 더 편하기 때문


### 상태 기반 테스트 vs 행위 기반 테스트

#### 상태 기반 테스트

하나는 객체가 **특정 시점에 자신 만의 상태**를 갖는다는 특징을 이용한 테스트

테스트 대상 메소드를 호출하고, 그 결과값과 예상값을 비교하는 방식.

#### 행위 기반 테스트
- 올바른 로직 수행에 대한 판단의 근거로 특정한 동작의 수행 여부를 이용.
- 메소드의 리턴값이 없거나 리턴값을 확인하는 것 만으로 예상대로 동작했음을 보증하기 어려운 경우에 사용.
- 행위 기반 테스트를 수행할 때는 예상하는 행위들을 미리 시나리오로 만들어놓고 해당 시나리오대로 동작이 발생했는지 여부를 확인하는 것이 핵심


#### Mock 객체
- 일반적인 테스트 더블은 상태(state)를 기반으로 테스트 케이스를 작성
- Mock 객체는 행위(behavior)를 기반으로 테스트 케이스를 작성

상태 기반 테스트의 예제
```java
ICoupon coupon = new FakeCoupon();
user.addCoupon(coupon);
assertEquals (1, user.getTotalCouponCount());
```

행위 기반 테스트의 예제
```java
ICoupon coupon = mock(ICoupon.class);
coupon.expects(once()).method("isValid") // ➊
.withAnyArguments() // ➋
.will(returnValue(true)); // ➌
user.addCoupon(coupon);
assertEquals(1, user.getTotalCouponCount());
```
➊ Mock으로 만들어진 coupon 객체의 isValid 메소드가 한 번 호출될 것을 예상함
➋ isValid에서 사용할 인자는 무엇이 됐든 상관 안 함
➌ 호출 시에 리턴값은 true를 돌려주게 될 것임

#### Mock 객체라는 용어가 주는 혼란스러움 정리
사실 `Mock 객체` 는 `행위 기반 테스트를 위해 사용되는 객체` 의 의미 보다는 더 넓은 일반적인 `가상 임시 구현체` 의 의미로 사용되는 경우가 더 많음.

> 위에서는 테스트 더블을 일반 interface를 구현하는 클래스로 만들었지만 테스트를 위한 클래스가 늘어나는 것이 좋은 현상은 아니기에 **익명 클래스**<sup>annoymous class</sup> 로 만들어서 작성하는 것도 생각해볼만하다(뒤에서는 이것도 좋은 방법이 아니라고 한다).


## Mock 프레임워크
Mock 프레임워크(혹은 라이브러리)는 동적으로 **Mock 객체**를 만들어주는 **프레임워크**

- Mock 객체를 직접 작성해서 **명시적인 클래스**로 만들지 않아도 된다.
- Mock 객체에 대해서 **행위**까지도 테스트 케이스에 포함시킬 수 있다.

### Mockito
TDD 개발자들이 선호하는 상태 기반 테스트를 지향하기에 인기가 많아짐.

#### 기본 사용법
Mockito는 Stub 작성과 Verify 가 중심을 이루며 다음과 같은 순서로 진행
| 이름 | 역할 |
|:--------|:--------|
| CreateMock | 인터페이스에 해당하는 Mock 객체를 만든다 |
| Stub | 테스트에 필요한 Mock 객체의 동작을 지정한다(단, 필요시에만) |
| Exercise | 테스트 메소드 내에서 Mock 객체를 사용한다 |
| Verify | 메소드가 예상대로 호출됐는지 검증한다 |

1. Mock 객체 만들기
```java
Mockito.mock (타깃 인터페이스);
```
2. 테스트에 사용할 스텁 만들기(필요할 때만 만든다)
```java
when(Mock_객체의_메소드).thenReturn(리턴값);
when(Mock_객체의_메소드).thenThrow(예외);
```
3. 검증
```java
verify (Mock_객체).Mock_객체의_메소드;
verify (Mock_객체, 호출횟수지정_메소드).Mock_객체의_메소드;
```

호출 횟수도 지정해서 **검증**할 수가 있다.

| 이름 | 역할 |
|:--------|:--------|
| times(n) | n번 호출됐는지 확인, n=0은 times를 지정하지 않았을 때와 동일 |
| never | 호출되지 않았어야 함 |
| atLeastOnce | 최소 한 번은 호출됐어야 함 |
| atLeast(n) | 적어도 n번은 호출됐어야 함 |
| atMost(n) | 최대 n번 이상 호출되면 안됨 |

예를 들면,
```java
verify (mockedList).add("item");
verify (mockedList, times (1)).add("item");
verify (mockedList, times (2)).add(box);
verify (mockedList, never ()).add(car);
verify (mockedList, atLeastOnce()).removeAll();
verify (mockedList, atLeast (2)).size();
verify (mockedList, atMost (5)).add(box);

```

```java
import static org.mockito.Mockito.*;
public class SimpleMockTest {
	@Test
	public void testMockList() throws Exception {
		List mockedList = mock(List.class);
		verify (mockedList).size();
	}
}
```
```
-----실행 결과-----
Wanted but not invoked:
list.size();
-> at SimpleMockTest.testMockList(SimpleMockTest.Java:19)
Actually, there were zero interactions with this mock.
```

사용법
```java
import static org.mockito.Mockito.*;
…
List mockedList = mock (List.class);

// Stub 만들기
when (mockedList.get(0)).thenReturn("item");
when (mockedList.size()).thenReturn(1);
when (mockedList.get(1)).thenThrow(new RuntimeException());

```

## Mock 프레임워크 마무리

### 유의 사항

#### Mock 프레임워크가 정말 필요한지 잘 따져본다
- Mock을 사용하는 것 자체가 **목적**이 돼버리는 경우, Mock 객체가 필요한 부분을 찾는 것이 아니라, Mock 객체가 적용될 수 있는 부분을 찾으려 애쓰는 역전 현상이 발생한다. **Mock은 의존성 문제**로 인해 어쩔수 없을 때 사용하자.

#### 투자 대비 수익(ROI)이 확실할 때만 사용한다
- 테스트용 DB를 설치하는데 반나절이 걸린다고 하자. 그렇다면 Mock 객체를 사용하는 것이 맞을까? **Mock을 사용할 때는 좀 더 길게 볼 필요가 있다.** 쉽게 사용한 Mock 객체들이 시간이 지날수록 깨지기 쉬워 지고 개발 진척의 발목을 잡는 족쇄가 될 수 있다.

#### 어떤 Mock 프레임워크를 사용하느냐는 핵심적인 문제가 아니다
- 대부분의 경우 간단한 Mock 객체만으로도 충분하다. **유행을 따르지 말자.**

#### Mock은 Mock일 뿐이다.
- Mock 객체를 사용해 아무리 잘 작동하는 코드를 만들었다 하더라도, 실제 객체(real object)가 끼어들어 왔을 때도 잘 동작하리라는 보장은 없다.