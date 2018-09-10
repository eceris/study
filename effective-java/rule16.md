# 규칙 16. 계승(상속)하는 대신 구성하라

### 이유
1. 계승<sup>상속</sup>은 코드를 재사용하는 강력한 도구지만, 항상 그렇지는 않다. 적절히 사용하지 못하면 깨지기쉬운<sup>fragile</sup> 클래스를 만든다.
2. 메소드 호출과 달리 계승은 캡슐화<sup>encapsulation</sup>원칙을 위반한다.
```
- 하위클래스가 동작하기 위해서는 상위클래스에 의존할 수 밖에 없음.(상위 클래스에 발맞춰 변화해야함.)
- 작성자가 계승을 고려해 설계하고 문서까지 만들어 놓지 않으면 깨지기 쉽다.
```
아래 코드를 참고하자 
```java
// 계승을 잘못 사용한 사례
public class InstrumentedHashSet<E> extends HashSet<E> {
	// 요소를 삽입하려한 횟수
	private int addCount = 0;
	public InstrumentedHashSet() {	}
	public InstrumentedHashSet(int initCap, float loadFactor) {
		super(initCap, loadFactor);
	}
	@Override
	public boolean add(E e) {
		addCount++;
		return super.add(e);
	}
	@Override
	public boolean addAll(Collecetion<? extends E> c) {
		addCount += c.size();		<-- 여기가 문제다
		return super.addAll(c);		<-- 여기가 문제다
	}
	public int getAddCount() {
		return addCount;
	}
}
```
괜찮아 보이지만 정상동작하지 않는다. 예를 들면 아래 코드를 실행하면 **getAddCount() 가 3**을 반환할 것이라고 생각하지만 **실제는 6**이다. 

```java
InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
s.addAll(Arrays.asList("snap", "crackle", "pop"));
```
따라서 InstrumentedHashSet 클래스는 깨지기 쉬운 <sup>fragile</sup>클래스 이다.

사실 이런 문제들은 메서드 재정의로 인해 발생한 것이고, 기존 클래스를 **계승**하는 대신 새로운 클래스의 필드로 **구성**<sup>composition</sup>하여 문제를 해결 할 수 있다.

이런 기법을 **전달**<sup>forwarding</sup> 이라고 부른다. 
```java
//계승 대신 구성을 사용하는 포장(wrapper)클래스
public class InstrumentedHashSet<E> extends ForwardingSet<E> {
	private int addCount = 0;
	public InstrumentedHashSet() {}
	@Override
	public boolean add(E e) {
		addCount++;
		return super.add(e);
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		addCount += c.size();
		return super.addAll(c);
	}
	public int getAddCount() {
		return addCount;
	}
}
//재사용가능한 전달(forwarding)클래스
public class ForwardingSet<E> implements Set<E> {
	private Set<E> s;
	public ForwardingSet(Set<E> s) {
		this.s = s;
	}
	public void clear() { s.clear(); }
	public boolean contains(Object o) { return s.contains(o); }
	public boolean isEmpty() { return s.isEmpty(); }
	public int size() { return s.size(); }
	public Iterator<E> iterator() { return s.Iterator(); }
	public boolean add(E e) { return s.add(e); }
	public boolean remove(Object o) { return s.remove(o); }
	public boolean containsAll(Collection<? extends E> c) { return s.containsAll(c); }
	public boolean addAll(Collection<? extends E> c) { return s.addAll(c); }
	public boolean removeAll(Collection<? extends E> c) { return s.removeAll(c); }
	public boolean retainAll(Collection<? extends E> c) { return s.retainAll(c); }
	public Object[] toArray() { return s.toArray(); }
	public <T> T[] toArray(T[] a) { return s.toArray(a); }
	@Override
	public boolean equals(Object o) { return s.equals(o); }
	@Override
	public int hashcode() { return s.hashcode(); }
	@Override
	public String toString() { return s.toString(); }
}
```
위와 같이 **포장 클래스**<sup>wrapper class</sup>를 설계하면 안정적일뿐아니라 유연성도 높다. 이런 패턴을 **데코레이터**<sup>decorator</sup> 패턴이라고도 부른다. 

단점이 별로 없으나 callback 프레임워크와 함께 사용하기에는 적합하지 않음.

> 클래스 B는 클래스 A와 IS-A 관계가 성립할때만 계승해야 하기에 **"B(원숭이)는 확실히 A(동물)인가?"** 라는 질문에 YES 일때만 계승하라. 만약 계승이 갖고 있는 문제를 피하려면 **구성과 전달 기법**을 사용하여 설계하라.