# generic 간편정리
```
static <T> void method(List<T> list) {
}

static void method2(List<?> list) {
}
```

타입 파라미터 T를 사용하는 이유는 List 안의 T에 관심이 있다는 것
와일드 카드는 T가 아닌 List에 관심이 있다는 것 ..

? 와 T 를 작성하는데 있어서 ....
T 타입파라미터를 사용하면 이 메소드에서 T를 사용하여 뭔가를 하겠다.
라는 의미임 ..

메소드 내부에서 사용할 경우 upper bounded를 사용 (input)
<? extends T\>

메소드 외부에서 사용할 경우 lower bounded를 사용 (output)
<? super T\>


wild card capture

컴파일러가 타입이 뭔지 추론해야되는 상황이 오는데 ..

1. generic T로 바꾸는 방법이 있고
2. 일종의 helper 메소드를 만들어서 ...처리
3. raw type으로 하는 방법도 있다 ...

```
Intersection type

(Function) s -> s

(Function & Serializable) s -> s
Intersaction type
function과 serializable 타입을 조합.

marker interface ----> Serializable

```
실제로 Functional Interface는 익명 클래스인데 위와 같이 IntersactionType을 주면...
그 익명 클래스가 Serializable을 구현 한 것 처럼 동작한다.
Serializable과 같이 메소드가 하나도 존재하지 않는 인터페이스는 marker interface라고 하는데..
이런 종류는 Serializable, Clonable....등등이 있다..

근데 왜 굳이 이런걸 만들었냐면 ... Lambda도 Serializable을 구현하고 싶을때도 있다 .

암튼...이게 어떤 의미가 있냐면 ....






