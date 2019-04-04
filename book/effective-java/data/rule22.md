# 규칙 22. 멤버 클래스는 가능하면 static으로 선언 할 것
중첩 클래스<sup>nested class</sup> 는 다른 클래스 안에 정의된 클래스.

총 네가지가 있는데

1. 정적 멤버 클래스<sup>static member class</sup>
2. 비-정적 멤버 클래스<sup>nonstatic member class</sup>
3. 익명 클래스<sup>anonymous class</sup>
4. 지역 클래스<sup>local class</sup>

정적 멤버 클래스<sup>static member class</sup>는 보통 바깥 클래스<sup>Outer class</sup>의 유용한 도움 클래스<sup>Helper class</sup>로 사용한다.(바깥클래스와 독립적으로 존재)

비정적 멤버 클래스는 바깥 클래스의 메소드를 호출할 수 있고, this 한정자<sup>qualified this</sup>를 통해 바깥 객체에 대한 참조도 획득 가능.
Map 인터페이스를 구현한 애들에서 자주 볼수 있다.<sup>예를 들면 HashMap의 final class Values extends AbstractCollection<V> 와 같은... 것들</sup>

```java
class Envelop {
	void(x) { System.out.println("hello world"); }
	class Enclosure {
		void(x) { Envelop.this.x(); } // qualified this 사용.
	}
}
```

바깥 클래스 객체에 접근할 필요가 없는 멤버 클래스는 항상 정적 멤버 클래스로 만들자.

1. 익명클래스는 표현식 중간에 들어가므로 가독성을 위해 10줄 이하로 짧게 작성할 것
2. 익명클래스는 사용되는 즉시 새로운 객체로 생성되므로 자주 사용될 경우 메모리를 차지

> 바깥 객체에 대한 참조 여부를 확인 후, 비정적 vs 정적 멤버 클래스로 만들어라. 그리고 만약 중첩 클래스가 특정 메소드에만 속해야 하고, 한곳에서만 객체를 생성한다면 익명클래스도 괜찮다.
