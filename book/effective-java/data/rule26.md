# 규칙 26. 가능하면 제네릭 자료형으로 만들것
```java
public class Stack {
	private Object[] elements;
	private int size = 0;
	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	public Stack() {
		elements = new Object[DEFAULT_INITIAL_CAPACITY];
	}

	public void push(Object e) {
		ensureCapacity();
		elements[size++] = e;
	}

	public Object pop() {
		if (size == 0) 
			throw new EmptyStackException();
		Object result = elements[--size];
		elements[size] = null; //만기 참조 제거
		return result;
	}

	public boolean isEmpty() {
		return size == 0;
	}
	private void ensureCapacity() {
		if (elements.lengt == size)
			elements = Arrays.copyOf(elements, 2 * size - 1);
	}
}
```
위의 클래스는 제네릭화<sup>generification</sup> 하면 딱 좋은 후보. 



```java
public class Stack<E> {
	private E[] elements;
	private int size = 0;
	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	@SuppressWarnings("unchecked")
	public Stack() {
		elements = (E[])new Object[DEFAULT_INITIAL_CAPACITY]; // elements 배열에는 push(E)를 통해 전달된 E의 객체만 저장.
	}

	public void push(E e) {
		ensureCapacity();
		elements[size++] = e;
	}

	public E pop() {
		if (size == 0) 
			throw new EmptyStackException();
		@SuppressWarnings("unchecked")
		E result = (E) elements[--size]; //자료형이 E인 원소만 PUSH하므로, 형변환은 안전하다.
		elements[size] = null; //만기 참조 제거
		return result;
	}

	public boolean isEmpty() {
		return size == 0;
	}
	private void ensureCapacity() {
		if (elements.lengt == size)
			elements = Arrays.copyOf(elements, 2 * size - 1);
	}
}
```
위와 같이 사용하면, 명시적으로 형변환도 필요 없다. 

형인자를 제거하는 제네릭 자료형도 있는데, 
```
class DelayQueue<E extends Delayed> implements BlockingQueue<E> ;
```
인자목록 <E extends Delayed\> 을 보면 실 형인자 E는 반드시 Delayed의 하위 자료형이 어야 한다. 이렇게 한정하는 것을 한정적 형인자<sup>bounded type parameter</sup> 라고 한다.


> 제네릭 자료형은 클라이언트가 형변환을 해야하는 자료형보다 안전하다. 시간이 있을 때마다, 기존 자료형을 제네릭 자료형으로 변환하라.

