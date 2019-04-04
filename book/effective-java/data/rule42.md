# 규칙 42. varargs는 신중히 사용하라

가변 인자 메서드<sup>variable arity method</sup> 라고 부르는 `varargs` 는 지정된 자료형의 인자를 0개 이상 받을 수 있다.

아래와 같이 사용하면 클라이언트에서 전달한 인자 수에 맞는 **배열**이 자동 생성되고, 모든 인자가 배열에 대입되어 메소드에 인자로 전달된다.

```java
//varargs의 간단한 사용 예
static int sum(int... args) {
	int sum = 0;
	for(int arg : args) {
		sum += arg;
	}
	return sum;
}
```

때때로 인자의 갯수가 0개 이상이 아니라, 하나 이상 필요할 때가 있는데 아래와 같이 구현하는 것은 좋지 않다.
```java
static int sum(int... args) {
	if (args.length == 0) {
		throw new IllegalArgumentException("Too few arguments");
	}
	int min = args[0];
	for(int i = 1; i< args.length; i++) {
		if (args[i] < min)
			min = args[i];
	}
	return min;
}
```
왜냐하면 
1. 클라이언트가 인자 없이 호출하는 것이 가능.
2. 컴파일 시점이 아니라 런타임에 오류가 발생
3. 보기가 흉하다!!!!

이것을 극복한 좋은 방법이 인자를 두개 받는 것
```java
static int min(int firstArg, int... remainingArgs) {
	int min = firstArg;
	for (int arg : remainingArgs) {
		if (arg < min)
			min = arg;
	}
	return min;
}
```
위와 같은 방법으로 구현된 것이 자바 1.5에 printf와 리플렉션이다. 

### Array를 출력하는 좋은 방법
```java
System.out.println(Arrays.toString(myArray));
```

#### 성능이 중요한 환경이라면 varargs 사용에 신중해라.
varargs 는 호출 할때마다, 그에 맞는 배열을 만들고 초기화 한다. 만약 성능이 중요한 환경이라면, 아래와 같이 하자.
```java
public void foo() {}
public void foo(int a1) {}
public void foo(int a1, int a2) {}
public void foo(int a1, int a2, int a3) {}
public void foo(int a1, int a2, int a3, int... rest) {}
```
위와 같이 하는 방법이 보통의 상황에서는 적절치 않지만 대부분의 성능 최적화 방법이 그렇듯 필요할땐 유용하다.

**EnumSet** 클래스도 위와 같은 기법을 사용한다.

> varargs 메소드는 인자 개수가 가변적인 메소드를 정의할 때 편리하지만, 남용되면 좋지 않다. 