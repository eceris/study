# 규칙 33. ordinal을 배열 첨자로 사용하는 대신 EnumMap을 이용하라

때때로, ordinal 메소드가 반환하는 값을 배열 첨자로 이용하는 코드를 만나는데, 이것은 잘못 된 것이다.
1. 배열은 제네릭과 호환되지 않아 형변환이 필요하다.
2. 정확한 int 값을 사용해야 하는데, 오동작할 가능성이 크다.


상태전이와 같은 나타내는 상수를 만들고 싶다면 아래와 같이 하는것이 우아하다.
```java
public enum Phase {
	SOLID, LIQUID, GAS;

	public enum Transition {
		MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID), BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID), SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID);

		private final Phase src;
		private final Phase dst;

		Transition(Phase src, Phase dst) {
			this.src = src;
			this.dst = dst;
		}

		//상태전이 맵 초기화
		private static final Map<Phase, Map<Phase, Transition>> m = new EnumMap<Phase, Map<Phase, Transition>>(Phase.class);
		static {
			for (Phase p : Phase.values()) {
				m.put(p, new EnumMap<Phase, Transition>(Phase.class));
			}
			for (Transition trans : Transition.values()) {
				m.get(trans.src).put(trans.dst, trans);
			}
		}

		public static Transition from(Phase src, Phase dst) {
			return m.get(src).get(dst);
		}
	}
}
```
초기화 과정이 조금 복잡하지만, 우아하다. 
EnumMap으로 만든 맵의 맵은 내부적으로는 배열의 배열이므로 메모리 요구량이나 수행 성능 측면에서 손해보는 일은 없다.

> ordinal 값을 배열 첨자로 사용하기보다는 EnumMap을 사용하라. 만약 다차원을 표현해야 한다면 EnumMap<..., EnumMap<...>> 과 같이 사용하라.
