# 규칙 53. 리플렉션 대신 인터페이스를 이용하라.

java.lang.reflection 의 핵심 리플렉션 기능<sup>core reflection facility</sup>을 이용하면 메모리에 적재된 클래스의 정보를 가져오는 프로그램을 작성할 수 있다.

### Constructor, Method, Field 객체를 이용해 클래스의 멤버이름이나 필드 자료형, 메서드 시그너쳐 등의 정보를 얻을 수 있다. 
Method.invoke() 메소드를 이용하면 어떤 메소드라도 호출 가능
리플렉션을 이용하면, 컴파일 시점에 존재하지 않은 클래스를 사용가능.

#### 다만 리플렉션을 사용하면 .... 
1. 컴파일 시점에 자료형을 검사할수있는 이점을 포기해야 한다. 예외를 포함하여 접근 불가능 한 메소드를 호출 하는 등의 행동은 런타임에 오류를 발생시킨다.
2. 리플렉션 기능을 이용하는 코드는 보기가 좋지 않다. 
3. 성능이 낮다. 일반적인 메소드 호출에 비해 고려해야할 요건이 많다.<sup>필자의 컴퓨터에서 2배 ~ 50배 느려짐</sup>

#### 일반적인 프로그램은 프로그램 실행 중에 리플렉션을 통해 객체를 이용하면 안됨.

- 원래는 컴포넌트 기반 응용프로그램<sup>component-based application builder tool</sup> 을 위해 설계된 기능임.
- 스텁 컴파일러<sup>stub compiler</sup> 가 없는 원격 프로시저 호출<sup>remote procedure call, RPC</sup> 시스템을 구현하는데 적당함

#### 리플렉션을 아주 제한적으로 사용하면 오버헤드는 피하고 리플렉션의 장점을 누릴수 있음.

- 객체 생성은 리플렉션으로 하고 객체의 잠초는 인터페이스나 상위 클래스를 통하면 좋다.
- 호출해야 하는 생성자가 아무런 인자가 없는 경우<sup>NoArgs</sup> Class.newInstance() 메소드를 호출해도 좋다.

```java
public static void main(String [] args) {
	//클래스 이름을 class 객체로 변환
	Class<?> cl = null;
	try {
		cl = Class.forName(args[0]);
	} catch (ClassNotFoundException e) {
		System.err.println("Class not found.");
		System.exit(1);
	}

	//해당 클래스의 객체 생성
	Set<String> s = null;
	try {
		s = (Set<String>) cl.newInstance();
	} catch (IllegalAccessException e) {
		System.err.println("Class not accessible.");
		System.exit(1);
	} catch (InstantiationException e) {
		System.err.println("Class not instantiable.");
		System.exit(1);
	}

	//집합 이용
	s.addAll(Arrays.asList(args).subList(1, args.length));
	System.out.println(s);
}
```
위와 같은 방법은 완벽한 [서비스 제공자 프레임워크](rule1.md) 를 구현할 수 있을 정도로 강력하다. 

### 다만 두가지 단점이 있는데...
1. 세가지 런타임 오류<sup>runtime error</sup>를 발생시키는데, 그것을 처리하기 위해 20줄 가량의 보기 싫은 코드를 작성했다.
2. 컴파일 시점에 이미 처리할 수 있는 에러들을 처리해야 하는 코드가 작성되었다.

#### System.exit()
이 메소드를 호출하는 것은 별로 좋지 않은데. 전체 VM을 그냥 종료시켜버린다. 하지만 **명령줄** 을 처리하는 유틸리티를 비정상 종료시키는데는 **적당**하다.

### 드물기는 하지만, 리플렉션은 런타임에 존재하지 않는 클래스나 메서드, 필드에 대한 종속성을 관리하는데 적합
- 어떤 패키지의 버전이 여러가지이고, 그 전부를 지원하는 또 다른 패키지를 구현할 때 적당.

> 리플렉션은 **특정한 종류** 의 **복잡한 시스템 프로그래밍**에 필요한 강력한 도구. 하지만 단점이 많음. 컴파일 시점에 알수 없는 클래스를 이용하는 프로그램을 작성한다면, 리플렉션을 사용하되 **객체를 만들 때**만 사용하고, 객체를 **참조**할 때는 컴파일시에 알고 있는 인터페이스나 상위 클래스를 이용할 것.