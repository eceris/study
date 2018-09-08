# 규칙 7. finalizer 사용을 피해라.
종료자<sup>finalizer</sup> 는 예측 불가하고 위험하므로 보통은 불필요.

## 단점 1 : 즉시 실행되리라는 보장이 없다.
어떤 객체의 참조가 모두 사라지고 종료자가 실행되기 까지는 시간이 걸릴수 있는데, 이게 Java Specification Doc에도 나와있지 않다. 
그러므로 긴급한<sup>time-ciritical</sup> 작업(파일 스트림을 닫는다던지....) 을 종료자에서 처리하면 안된다.

```
실제로 finalizer의 실행시점은 GC 알고리즘에 의해 결정되는데, JVM마다 구현이 다르고 구현체에 따라 천천히 실행하는 경우도 있기 때문에 사용하지 않는 것이 좋다.
```

## 단점 2 : finalizer 를 사용하면 성능이 심각하게 떨어진다. 
필자의 컴퓨터에서 간단한 객체를 만들고 삭제하는데 5.6ns, finalizer를 붙이자 2400ns.

## 그럼 파일이나 스레드 처럼 명시적으로 반환하거나 삭제해야하는 작업은 어떻게 해?

### 방법 1 : 명시적으로 termination method 를 이용
명시적으로 termination method 를 하나 정의하고 필요하지 않은 객체라면 클라이언트가 호출하도록.(ex : OutputStream, InputStream 의 close())
보통은 객체 종료를 위해 `try-finally` 구문과 함께 사용.

```java
// try-finally 블록을 통해 종료메소드의 실행 보장
Foo foo  = new Foo(...);
try {
	//foo로 해야하는 작업 수행
	...
} finally {
	foo.terminate(); // 명시적 종료 메서드 호출
}
```

## 그럼 진짜 finalize()는 사용할 데가 없나?

### 쓸만한 곳 1 : 명시적 호출을 잊을 때를 대비한 안전망<sup>safety net</sup> 의 역할
클라이언트가 명시적으로 메소드 호출을 잊을때 안전망으로 구현한다. 대신, 그런 자원을 발견할 때는 나중을 위해 꼭 **로깅** 해야 한다.

### 쓸만한 곳 2 : 네이티브 피어<sup>native peer</sup>를 다룰 때 
native peer : 일반 자바 객체가 네이티브 메소드를 통해 기능 수행을 위임하는 네이티브 객체.
일반적인 객체가 아니므로 GC가 알수 없다.

### 주의할 점
다만, finalizer() 를 갖고 있는 클래스를 상속받은 클래스를 구현한다면, finalizer의 chaning 이 자동으로 이뤄지지 않기 때문에, 수동으로 해주어야 한다. 
왜냐면 하위에서 예외가 발생했을때, 상위의 자원도 release 해주어야 하기 때문.
그래서 보통은 익명 클래스를 이용하여 구혆나다. 
```java
//종료 보호자 숙어(Finalizer Guardian idiom)
public class Foo {
	// 이 객체는 바깥 객체(Foo 객체)를 종료 시키는 역할.
	private final Object finalizerGuardian = new Object() { //
		@Override
		protected void finalizer() throws Throwable {
			//바깥 foo를 종료
		}
	};
	... //이하 생략
}
```
익명 클래스로만든 개게는 종료 보호자<sup>finalizer guardian</sup> 이라고 부름. 
종료되어야 하는 객체 안에 하나씩 넣어서 사용하며, 바깥 객체에 대한 모든 참조가 사라지는 시점에 실행.


> \- 자원반환에 대한 최종적 안전장치를 구현하는 것이 아니면 finalizer()는 사용하지 말자. 
>
> \- 굳이 사용해야 한다면 super.finalize() 호출은 잊지말자.
>
> \- 자원의 반환 안전망을 구현하는 경우에 finalizer()가 호출 될 때마다 꼭 로깅하자.
>
> \- 상속이 가능한 public 클래스에 finalizer를 추가하는 상황이면, 꼭 finalizer guardian을 도입할지 고민해보자.
