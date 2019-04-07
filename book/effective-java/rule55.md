# 규칙 55. 신중하게 최적화하라
프로그래머가 알아둬야 하는 최적화 관련 격언 세가지.

> 맹목적인 어리석음<sup>blind stupidity</sup>을 비롯한 다른 어떤 이유보다도, **효율성**이라는 이름으로 **저질러지는 죄악**이 더 많다.(효율성을 반드시 성취하는 것도 아니면서 말이다.)

> \- 윌리엄 울프(William A. Wulf)[Wulf 72]

> 작은 효율성<sup>small efficiency</sup>에 대해서는, 말하자면 97% 정도에 대해서는, 잊어버려라. 섣부른 최적화<sup>premature optimization</sup>는 모든 악의 근원이다. 

> \- 도널드 커누스(Donald E. Knuth)[Knuth 74]

> 최적화를 할 때는 아래의 두 규칙을 따르라. 

> 1. 하지마라.

> 2. (전문가들만 따를 것) 아직은 하지마라 - 완벽히 명료한, 최적화되지 않는 해답을 얻을때까지는...

> \- M. A. 잭슨(M. A. Jackson)[Jackson 75]

최적화는 좋을 때보다 나쁠 때가 더 많으며, 섣불리 시도하면 더더욱 나쁘다.

### 성능 때문에 구조적인 원칙<sup>architectural principle</sup> 을 희생하지 마라.

- 빠른 프로그램이 아닌, 좋은 프로그램을 만들려 노력하라.
- 좋은 프로그램인데 충분히 빠르지 않다면, 좋은 구조를 갖추었기 때문에 최적화의 여지도 충분할 것이다.

### 설계할 때는 성능을 제약할 가능성이 있는 결정들은 피하라.
성능문제가 나왔을 때, 가장 고치기 힘든 부분은 모듈 간 상호작용이나 외부와의 상호작용을 명시하는 부분이다.<sup>API, 통신 프로토콜, 지속성 데이터 형식</sup> 이런 부분은 성능 문제가 발견된 후에는 수정하기 어렵거나 수정이 불가능하다. 

### API를 설계할 때 내리는 결정들이 성능에 어떤 영향을 끼칠지를 생각하라.
- public 자료형을 변경 가능하게 만들면 쓸데없이 [방어적 복사](rule39.md)를 많이 해야 할 수도 있다. 
- 구성<sup>composition</sup>기법을 사용하지 않고 계승<sup>inheritance</sup>기법을 사용하면 영원히 상위클래스에 묶이게 된다.
- 인터페이스가 적당할 API에 구현 자료형<sup>implementation type</sup>을 사용해버리면 특정한 구현에 종속되므로, [나중에 더 빠른 구현체가 나와도 적용할 수 없다.](rule52.md)

### 좋은 성능을 내기 위해 API를 급진적으로바꾸는 것은 바람직하지 않다.

### 최적화를 시도할 때마다, 최적화 전 후의 성능을 측정하고 비료하라.
- 프로파일링 도구를 사용하면 어디를 최적화할지 판단하는데 도움이 된다. 
- 보통은 알고리즘이 최적화의 대상이 된다.

> **빠른 프로그램**을 만들고자 **애쓰지 말라.** 대신 **좋은 프로그램**을 짜기 위해 애써라. 성능은 따라올 것이다. 시스템 구현을 마쳤다면 **프로파일링 도구**를 이용하여 **성능을 측정하고 최적화**하라.