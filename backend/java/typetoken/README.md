# Typetoken

## TypeErasure
**타입 소거**<sup>TypeErasure</sup> 라고 불리는 이 작업은 결과적으로 자바의 하위호환성을 보장하기 위해 컴파일 시점에 동작한다.

자바 1.5 이전에는 Generic이 존재하지 않았기 때문에 아래와 같이 아무 객체나 넣을 수 있었다.
```java
List numbers = new ArrayList();
numbers.add(1);
numbers.add("2");
numbers.add(3l);
```
위와 같이 작성해도 컴파일과 실행에 문제가 없다. 다만, 내부적으로 많은 오류를 낼 수 있는 여지를 갖고 있다.<sup>TypeSafe 하지 않다</sup>

자바 1.5 이후에 처음 **Generic**이 도입되면서 타입 파라미터를 지원하기 시작했다.
```java
List<String> numbers = new ArrayList<String>();
numbers.add("1");
numbers.add("2");
numbers.add("3");
```
이렇게 제네릭을 사용하면 **TypeSafe** 하다.

어쨋든 컴파일 시점에 **TypeErasure**가 일어나는 이유를 살펴 보기 위해 아래 코드를 보자.
```java
public class MyList<E> {
  private static final int DEFAULT_CAPACITY = 10;
  private Object element[]; // 
  private int index;

  public MyList() {
    element = new Object[DEFAULT_CAPACITY];
  }

  public void add(E e) {
    this.element[index++] = e;
  }

  public E get(int index){
    return (E) element[index];
  }
}
```
위의 코드를 사용하면
```java
MyList<String> myList = new MyList<>();
myList.add("wonwoo");
myList.add("seungwoo");
myList.add(1); //컴파일 에러
System.out.println(myList.get(0));
System.out.println(myList.get(1));
```
이렇게 컴파일 시점에 Type에 대한 안전성을 보장해준다. 

근데 좀 이상하다. get()메소드에서 명시적으로 **E 타입**으로 **형변환** 하고 있다. 이걸 바꿔보자.
```java
public class MyList<E> {
  private static final int DEFAULT_CAPACITY = 10;
  private E element[];
  private int index;

  public MyList() {
    element = new E[DEFAULT_CAPACITY]; //컴파일 에러
  }

  public void add(E e) {
    this.element[index++] = e;
  }

  public E get(int index){
    return element[index];
  }
}
```
이렇게 하면 컴파일 시점에 생성자에서 에러가 발생하는데 살펴보면 `Type Parameter 'E' cannot be instantiated directly` 에러이다. `타입파라미터 E는 직접적으로 인스턴화 할 수 없다` 는 내용인데, 왜 타입파라미터는 인스턴스화 할 수 없을까? 

바로 `TypeErasure` 때문이다. 자바는 **하위 호환**을 위해 **컴파일** 시점에 **타입 파라미터**를 제거하고 사용한다. 

이렇게 하면 Java 1.5 하위에서 작성된 `List numbers = new ArrayList();` 와 같은 코드를 **JRE 1.5** 이상에서도 무리없이 실행 할 수 있다. 

> 결과적으로 자바의 Generic은 **타입파라미터**를 컴파일 시점에 **타입체킹용**으로만 사용하고, 컴파일된 결과물<sup>bytecodes</sup>에는 **타입파라미터**를 **제거**한다. 실제로 런타임 시점에는 타입파라미터가 제거된 형태로<sup>raw type 형태</sup> 동작한다. 


그러면 **RawType** 인 List의 잃어버린 **타입 파라미터 정보**를 얻을 수는 없을까? 아래와 같이 **ParameterizedType**을 이용한 방법으로 타입 파라미터의 정보를 알아낼 수는 있다.
```java
List<String> s = new ArrayList<String>();
Type t =  s.getClass().getGenericSuperClass();
ParameterizedType p = (ParameterizedType) t;
p.getActuralTypeArguments()[0]
```


