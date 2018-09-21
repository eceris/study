# 규칙 11. clone()을 재정의할 때는 신중할 것

Clonable 은 객체가 복제<sup>clone</sup>을 허용한다는 사실을 알리는데 쓰려고 고안된 [믹스인 인터페이스<sup>mixin interface</sup> 이다.](rule18.md) 조금 특이한 형태인데, interface에는 clone() 메서드가 존재하지도 않는다. 

#### final이 아닌 클래스에 clone()을 재정의 할때는 반드시 super.clone()을 호출해서 얻은 객체를 반환할 것.

#### Clonable 인터페이스를 구현하는 클래스는 제대로 동작하는 public clone 메소드를 제공할 것.
PhoneNumber 클래스에 clone을 재정의 한다고 하면, Clonable 인터페이스를 구현한다고 선언후, Object 클래스의 protected clone() 메소드를 접근할 수 잇도록 하는 public 메소드를 구현하면 된다.
```java
@Override
public PhoneNumber clone() {
	try {
		return (PhoneNumber) super.clone();
	} catch (CloneNotSupportedException ex) {
		throw new AssertionError(); // 수행될리 없음.
	}
}
```
라이브러리가 할 수 있는 일을 클라이언트에게 미루지 말라.(형변환 해서 줘라)

#### 사실상 clone()메소드는 또 다른 형태의 생성자다.
복사의 대상이 되는 객체에 영향이 없어야 하고, 복사본의 불변식도 제대로 만족시켜야 한다. 기 구현 했던 Stack의 clone() 메소드가 정상 동작하게 하려면, 스택 내부 구조도 복사해야 한다.
```java
@Override
public stack clone() {
	try {
		Stack result = (Stack) super.clone();
		result.elements = elements.clone();
		return result;
	} catch (CloneNotSupportedException e) {
		throw new AssertionError();
	}
}
```
위와 같이 구현할 경우 elements 배열도 clone을 재귀적으로 호출 했기 때문에 정상이라고 생각할 수 있으나, element가 final로 선언되어 있으면 동작하지 않는다. clone() 의 아키텍쳐는 변경가능한 객체를 참조하는 final 필드의 일반적 용법과 호환되지 않는다.

어쨌든 동작하게 하기 위해서는 필드의 final 선언을 지워야 할 수도 있다.


HashTable을 clone()하기 위해서는 **deepcopy()** 를 지원해야 하는데, 자기 자신을 재귀적으로 호출하며 해당 원소를 복사하는데, 사이즈가 커지면 stack overflow가 난다.

> 객체 복제를 지원하는 좋은 방법은, 복사 생성자<sup>copy constructor</sup> 나 복사 팩터리<sup>copy factory</sup>를 이용하는 것. 결과적으로 Cloneable을 상속받는 인터페이스는 만들면 안된다.


