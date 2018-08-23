# [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)

## Package java.util.stream Description
이 패키지에 주된 추상화는 바로 Stream 이다. 스트림은 컬렉션과는 몇가지 이유에서 조금 다른데,

1. 저장공간이 없다. 스트림은 요소를 저장하는 데이터구조가 없다. 대신에 이것은 Source에서 computational operation의 pipeline을 통해  데이터의 구조, 배열, generator function, I/O채널등등을 전달한다.
2. Functional하다. Stream의 Operation은 결과를 생산할 뿐 Source를 수정하지는 않는다. 예를 들면 Stream의 filtered는 컬렉션의 요소들을 제거하지 안하고 새로운 Stream을 생성한다.
3. Lazyness를 추구한다. Stream의 많은 operation들은<sup>filter, map, duplicate removal..</sup> 최적화할 수 잇는 기회를 제공하기 위해 lazy하게 구현될 수 있다. 예를들면, "3개의 연속된 모음을 가진 첫번째 문자열을 찾는 문제"에서 Stream은 모든 입력 문자열을 검사하지 않는다. Stream operation은 항상 
중간<sup>Stream-producing</sup>작업과 터미널<sup>value- or side-effect-producing</sup>작업으로 분리되는데, 중간작업은 항상 lazy이다.
4. 무한대일수 있다. 컬렉션의 크기는 한정되어있지만, 스트림은 그렇지 않다. limit()이나 findFirst()와 같은 Short-circuiting operations는 무한 스트림에 대한 계산을 유한 시간으로 완료 할 수 있습니다.
5. Consumable 하다. 스트림의 각 요소는 Iterator처럼 생성주기동안 한번만 확인한다. 그래서 재검토를 하기 위해서는 새로운 스트림을 생성해야 할 수도 있다.

스트림은 아래의 몇가지 방법으로 생성되는데,

1. [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html) 으로부터 stream()이나 parallelStream으로 생성.
2. 배열로부터 [Arrays.stream(Object[])](https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#stream-T:A-)을 통해 생성.
3. stream 클래스의 static factory method를 통해([Stream.of(Object[])](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#of-T...-), [IntStream.range(int, int)](https://docs.oracle.com/javase/8/docs/api/java/util/stream/IntStream.html#range-int-int-), [Stream.iterate(Object, UnaryOperator)](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#iterate-T-java.util.function.UnaryOperator-))
4. 파일의 라인을 얻기위해 [BufferedReader.lines()](https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html#lines--)를 통해
5. 파일의 paths를 얻기위해 [Files](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html)를 통해
6. 임의의 숫자를 얻기 위해 [Random.ints()](https://docs.oracle.com/javase/8/docs/api/java/util/Random.html#ints--)
7. JDK의 수많은 다른 스트림 지원 메소드를 통해 [BitSet.stream()](https://docs.oracle.com/javase/8/docs/api/java/util/BitSet.html#stream--), [Pattern.splitAsStream(java.lang.CharSequence)](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#splitAsStream-java.lang.CharSequence-), [JarFile.stream()](https://docs.oracle.com/javase/8/docs/api/java/util/jar/JarFile.html#stream--).

## Stream operations and pipelines
스트림의 operation들은 중간작업과 터미널작업으로 나뉘어지고, 결합되어 스트림 파이프라인을 형성한다. Source(Collection, 배열, generator function, I/O채널)로 구성된 스트림의 파이프라인은 Stream.filter 또는 Stream.map과 같은 0 개 이상의 중간 연산과 Stream.forEach 또는 Stream.reduce와 같은 터미널 작업이 포함된다.

중간 Operation들은 새로운 스트림을 return하는데, 항상 `lazy`다. 사실 filter() 와 같은 중간 연산자는 실제로 어떤 필터링도 수행하지 않고, 요소를 순회<sup>traverse</sup>할 때 주어진 predicate와 일치하는 요소를 포함하는 새로운 stream을 생성한다. 파이프 라인 소스의 순회<sup>traversed</sup>는 파이프 라인의 터미널 연산이 실행될때까지 시작하지 않는다.

Stream.forEach 또는 IntStream.sum과 같은 터미널 연산은 결과나 어떤 side-effect를 생산하기 위해 스트림을 traverse 한다.

스트림을 순회하여 결과 또는 부작용을 일으키는데, 터미널 연산이 수행 된 후에는 스트림 파이프 라인은 소비 된 것으로 간주되어 더 이상 사용할 수 없다. 동일한 데이터 소스를 다시 순회<sup>traverse</sup>해야하는 경우 데이터 소스로 돌아와서 새 스트림을 가져와야 한다. 거의 모든 경우에, 터미널 연산은 `eager`로 작동하며, 리턴하기 전에 파이프 라인의 처리와 데이터 소스의 순회를 완료한다. 

그러나 터미널 연산중에 iterator()와 spliterator()는 조금 다른 "escape hatch"로서 제공된다.(기존 연산이 task에 충분하지 않은 이벤트에서 임의의 client-controlled 파이프라인 순회를 가능하게 하기 위해)

스트림에 대한 Lazy 처리가 상당히 효율적인데, 스트림 파이프라인에서 filtering, mapping, reducing 이 최소의 중간 상태에서 하나의 pass로 융합될수 있다. 지연처리는 필요하지 않은 데이터를 모두 검사하는 것도 피할 수 있다.(이런 동작은 input stream이 무한대이거나 그렇게 크지 않을때 더욱 중요하다.)

중간작업은 stateless 와 stateful  연산으로 나뉜다. Stateless<sup>filter, map</sup> 연산은 새로운 요소를 처리할 때, 이전 요소를 stateless로 유지한다.(각 요소는 연산과 독립적) Stateful<sup>distinct, sorted</sup> 연산은 새로운 요소를 처리할 때, 이전 요소의 상태를 통합한다.

Stateful 연산은 전체 요소를 처리해야할 수도 있다. 예를들면 sorted 함수는 스트림의 모든 요소를 확인하기 전엔 정렬하여 결과를 생성할 수 없다. 결과적으로 parallel 연산에서 stateful 중간 연산을 포함하는 일부 파이프 라인은 데이터를 여러 번 통과해야하거나 중요한 데이터를 버퍼해야 할 수도 있다. Stateless 중간 연산만 포함하는 파이프라인은 데이터 버퍼링을 최소화 하면서(parallel or sequential) 단일 패스로 처리할 수 있다.

게다가, 일부 연산은 short-circuiting operations으로 처리한다. 중간 연산은 short-circuiting 이다. 만약 무한입력으로 표현될 때, 결과적으로 유한한 stream을 생성할 수 있다. 단자 연산은 무한 입력으로 제시 될 때 유한 한 시간 내에 종료 될 수있는 경우 단락됩니다. 파이프 라인에서 단락 연산을 수행하는 것은 필요하지만 충분하지는 않습니다. 무한 스트림이 유한 시간에 정상적으로 종료되도록 처리하는 조건입니다.

## Parallelism
명시적으로 for-loop가 있는 요소의 처리는 직렬이다. 스트림은 집계연산<sup>aggregate operation</sup>의 파이프 라인으로 계산을 다시 매핑하여 병렬실행을 가능하게 한다.(각 요소에 명령형이 아니다.) 모든 스트림의 연산은 serial 혹은 parallel로 실행할수 있는데, 기본은 serial 이다. 예를들면 컬렉션의 Collection.stream()과 IntStream.range(int, int)의 경우 순차적인 스트림이지만, BaseStream.parallel()를 활용해 효과적으로 병렬화 할 수 있다. 앞의 예를 병렬로 실행하면,

```java
int sumOfWeights = widgets.parallelStream()
						.filter(b -> b.getColor() == RED)
						.mapToInt(b -> b.getWeight())
						.sum();
```

Serial과 Parallel의 유일한 차이점은 initial stream의 생성을 parallelStream()으로 하느냐 stream()으로 하느냐 이다. 터미널 연산이 시작되면, 스트림 파이프라인은 호출되는 스트림의 방향에 따라 순차적으로 또는 병렬로 실행된다. 스트림을 직렬 또는 병렬로 실행할지 여부는 isParallel 메소드를 사용하여 결정할 수 있고, 스트림의 방향은 BaseStream.sequential() 과 BaseStream.parallel을 사용하여 수정할 수 있다. 터미널 작업이 시작되면 스트림 파이프 라인은 호출되는 스트림의 모드에 따라 순차적으로 또는 병렬로 실행된다. 

findAny()와 같이 명시적으로 비결정적인 것으로 식별되는 연산을 제외하고는 스트림이 순차적 or 병렬적으로 실행되는지 여부가 계산의 결과를 변경해서는 안된다. 

대부분의 스트림 연산은 람다식으로도 파라미터를 받을수 있는데 올바른 동작을 유지하려면, 이러한 파라미터들이 반드시 비간섭적이어야 하고 stateless 여야만 한다. 이런 파라미터는 항상 [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)과 같은 기능의 인터페이스 인스턴스이며, 람다 혹은 메서드 참조 이다.

## Non-interference
작성중...