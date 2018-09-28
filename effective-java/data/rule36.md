# 규칙 36. Override 어노테이션은 일관되게 사용하라

자바 1.5 이후에 \@Override 어노테이션이 생겼는데, 이것은 메서드의 선언부에만 사용할 수 있고, 상위 자료형<sup>supertype</sup> 에 선언된 메소드를 재정의 한다는 사실을 표현.

아래 예제는 끔직한 버그를 일으킨다.
```java
public class Bigram {
	private final char first;
	private final char second;

	public Bigram(char first, char second) {
		this.first = first;
		this.second = second;
	}

	public boolean equals(Bigram b) {
		return b.first == first && b.second == second;
	}

	public int hashcode() {
		return 31 * first + second;
	}

	public static void main(String[] args) {
		Set<Bigram> s = new HashSet<Bigram>();
		for (int i = 0; i < 10; i++) {
			for (char ch = 'a'; ch <= 'z'; ch++) {
				s.add(new Bigram(ch, ch));
			}
		}
		System.out.println(s.size());
	}
}
```
equals 메소드를 재정의 하려고 하였지만, 메소드의 오버로딩<sup>equals 메소드는 Object를 파라미터로 받는다</sup>으로 인해 size가 26이 찍혀야 하지만 260이 찍힘. 

> 상위 자료형에 선언된 메소드를 재정의 하는 모든 메소드는 Override 어노테이션을 붙이면 많은 오류를 잡을 수 있다. 예외적으로, 비-abstract 클래스에서 상위 클래서의 abstract 메소드를 재정의 할때는 Override 어노테이션을 붙이지 않아도 된다.






### 작명 패턴의 단점 
1. 철자를 틀리면 알아차리기 힘들다
2. 특정한 프로그램 요소에만 적용되도록 하기 힘들다
3. 프로그램 요소에 인자를 전달한 마땅한 방법이 없다<sup>exception test 등등..</sup>

### 어노테이션의 장점
위의 모든 문제를 해결.
테스트  시에 자동으로 실행될 테스트 메서드를 지정. 메서드 내에서 예외가 발생하면 테스트가 실패한 것으로 보자면 아래 예를 보고 Test 자료형을 어떻게 정의하는지 보라.

```java
//마커 어노테이션 자료형(marker annotation type)선언
import java.lang.annotation.*;

// 어노테이션이 붙은 메서드가 테스트 메서드임을 표시.
// 무인자(parameterless) 정적 메서드에만 사용가능.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.Method)
public @interface Test {
}
```

@Retention : 메타 어노테이션으로 Test 가 실행시간에도 유지되어야 하는 어노테이션 임을 알려줌
@Target : 메타 어노테이션으로 Test 가 메서드 선언부에만 적용할 수 있다는 것을 알려줌

#### 마커 어노테이션을 사용하는 예 

```java
public class Sample {
	@Test
	public static void m1() {}	// 이테스트는 성공해야 함
	public static void m2() {}
	@Test
	public static void m3() {	// 이테스트는 실패해야 함
		throw new RuntimeException("Boom!");
	}
	public static void m4() {}
	@Test 
	public void m5() {} // 잘못된 사용:static 메소드가 아님
	public static void m6() {}
	@Test
	public static void m7() {	// 이테스트는 실패해야 함
		throw new RuntimeException("Crash!");
	}
	public static void m8() {}

}
```
어노테이션은 프로그램의 동작에 절대 개입하지 않고 테스트 실행기<sup>test runner</sup> 에게 특별히 다루어야 하는 부분을 알려주는 역할

실제 처리는 아래와 같은 테스트 실행기와 같은 애들이 한다.

```java
public class RunTests {
	public static void main(String []args) throws Exception {
		int tests = 0;
		int passed = 0;
		Class testClass = Class.forName(args[0]);
		for (Method m : testClass.getDeclaredMethods()) {
			if (m.isAnnotationPresent(Test.class)) {
				tests++;
				try {
					m.invoke(null);
					passed++;
				} catch(InvocationTargetException wrappedExc) {
					Throwable exc = wrappedExc.getCause();
					System.out.println(m + " failed: " + exc);
				} catch(Excpetion exc) {
					System.out.println("INVALID @Test : " + exc);
				}
			}
		}
		System.out.printf("Passwd : %d, Failed : %d%n", passed, tests - passed);
	}
}
```

아주 심플한 테스트 실행기<sup>test runner</sup> 이다. 만약 **특정한 예외**가 발생했을 때 성공하는 테스트를 만들고 싶다면 아래와 같이 하자.

#### 특정 예외가 발생했을 때 성공하는 테스트

```java
// 인자를 취하는 어노테이션 자료형
import java.lang.annotation.*;

// 이 어노테이션이 붙은 메서드는 테스트 메서드이며,
// 테스트에 성공하려면 지정된 예외를 발생시켜야 한다.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
	Class<? extends Exception> value();
}
```
Exception 을 상속받은 클래스를 파라미터로 받을 수 있다. 이렇게 표현한 것은 [한정적 자료형 토큰](data/rule29.md)<sup>bounded type token</sup> 이라고 한다. 

```java
public class Sample {
	@ExceptionTest(ArithmeticException.class)
	public static void m1() { //이 테스트는 성공
		int i =0;
		i = i / i;
	}

	@ExceptionTest(ArithmeticException.class)
	public static void m2() { // 이 테스트는 실패(엉뚱한 예외 발생)
		int[] a = new int[0];
		int i = a[1];
	}
} 
```
테스트 실행기<sup>test runner</sup> 를 수정하자.

```java
public class RunTests {
	public static void main(String []args) throws Exception {
		int tests = 0;
		int passed = 0;
		Class testClass = Class.forName(args[0]);
		for (Method m : testClass.getDeclaredMethods()) {
			if (m.isAnnotationPresent(Test.class)) {
				tests++;
				try {
					m.invoke(null);
					System.out.printf("Tests %s failed : no exception%n", m);
				} catch(InvocationTargetException wrappedExc) {
					Throwable exc = wrappedExc.getCause();
					Class<? extends Exception> excType = m.getAnnotation(ExceptionTest.class).value();
					if (excType.instanceof(exc)) {
						passed++;
					}
					System.out.println(m + " failed: " + exc);
				} catch(Excpetion exc) {
					System.out.println("INVALID @Test : " + exc);
				}
			}
		}
		System.out.printf("Passed : %d, Failed : %d%n", passed, tests - passed);
	}
}
```

#### 배열을 인자로 받는 어노테이션 자료형

```java
// 배열을 인자로 취하는 어노테이션 자료형
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
	Class<? extends Exception>[] value();
}
```

사용하는 방법
```java
@ExceptionTest({IndexOutOfBoundsException.class, NullPointerException.class})
public static void doublyBad() {
	List<String> list = new ArrayList<>();

	//자바 명세에는 아래와 같이 호출하면 IndexOutOfBoundsException 이나 NullPointerException 발생한다고 되어 있다
	list.addAll(5, null);
}
```

테스트 실행기<sup>test runner</sup> 를 변경하면

```java
public class RunTests {
	public static void main(String []args) throws Exception {
		int tests = 0;
		int passed = 0;
		Class testClass = Class.forName(args[0]);
		for (Method m : testClass.getDeclaredMethods()) {
			if (m.isAnnotationPresent(ExceptionTest.class)) {
				tests++;
				try {
					m.invoke(null);
					System.out.printf("Tests %s failed : no exception%n", m);
				} catch(Throwable wrappedExc) {
					Throwable exc = wrappedExc.getCause();
					Class<? extends Exception>[] excTypes = m.getAnnotation(ExceptionTest.class).value();
					int oldPassed = passed;
					for (Class<? extends Exception> excType : excTypes) {
						if (excType.instanceof(exc)) {
							passed++;
							break;
						}	
					}

					if (passed == oldPassed)	
						System.out.printf("Test %s failed : %s %n", m, exc);
				}
			}
		}
		System.out.printf("Passed : %d, Failed : %d%n", passed, tests - passed);
	}
}
```
어노테이션을 사용하고 작명 패턴<sup>naming pattern</sup> 을 사용하지 말자

> 대부분의 개발자는 어노테이션 자료형을 정의할 필요가 없지만, 그래도 알아둘 필요는 있다. 