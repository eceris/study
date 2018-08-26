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
스트림은 다양한 데이터 소스(ArrayList와 같은 non-thread-safe를 포함)에 대해 병렬 처리를 수행할 수 있게 해준다. 이것은 스트림 파이프라인의 실행 중에 데이터 소스의 간섭을 방지 할 수 있는 경우에만 가능하다. 그러나 escape-hatch 연산인 iterator()와 spliterator()를 제외하고 실제 실행은 터미널 연산이 호출될 때 시작되고 터미널 연산이 완료될때 끝난다. 대부문의 데이터 소스에서 간섭의 방지는 스트림 파이프라인의 실행중 데이터소스가 전혀 수정되지 않도록 보장한다. 살펴볼 만한 예외는 데이터 소스가 동시 수집인 동시에 동시 수정을 처리하도록 설계된 스트림이다. Concurrent 스트림 소스는 Spliterator가 CONCURRENT 특성을 보고한다.

게다가, 동시성을 보장하지 않는 데이터들이 스트림의 파이프라인에 속할 경우 스트림 파이프라인의 매개변수들은 스트림의 데이터 소스를 수정해서는 안된다. 매개변수가 스트림의 데이터 소스를 수정하거나 수정의 원인이 되면, non-concurrent 데이터 소스를 방해한다고 한다. 병렬 파이프라인 뿐 아니라, 모든 파이프라인에는 간섭하지 않는 것이 가이드입니다. 스트림의 소스가 동시성을 보장하는 한, 스트림 파이프라인의 실행 동안 스트림의 데이터소스를 변경하는 것은 예외와 잘못된 결과를 야기하는 원인이 됩니다. 잘 동작하는 스트림의 소스의 경우, 소스는 터미널 연산 시작 전에 소스를 수정할 수 있다. 그리고 그러한 작업들은 모든 요소에 영향을 미친다. 아래의 코드가 그 예이다.
```java
List<String> l = new ArrayList(Arrays.asList("one", "two"));
Stream<String> sl = l.stream();
l.add("three");
String s = sl.collect(joining(" "));
```

먼저, 두개의 string이 포함된 리스트를 생성한 후, 스트림을 생성하였다. 그리고나서 리스트에 새로운 요소가 추가되었다. 결과적으로 스트림의 요소들은 collected되고 함계 joined 된다. 위와 같이 코드를 실행 할 경우, 결과는 `one two three`가 나온다. 마지막으로 list에 요소를 수정한 이후 마지막에 collect 라는 터미널 연산이 시작되었기 때문이다. 모든 스트림은 jdk 컬렉션으로부터 리턴되고 대부분의 JDK 클래스들은 잘 동작하도록 보장해준다.

## Stateless behaviors
매개변수가 상태를 갖고 있는 Stateful 인 경우, 스트림 파이프라인의 결과는 비결정적이거나 올바르지 않을 수 있다. stateful 람다식(또는 fn interface의 구현체)은 스트림의 파이프라인 실행동안 변경될 수 있는 상태에 따라 결과가 달라진다. stateful 람다식식이 map함수의 매개변수로 들어있는 예제 이다. 

```java
Set<Integer> seen = Collections.synchronizedSet(new HashSet<>());
stream.parallel().map(e -> { if (seen.add(e)) return 0; else return e; })...

```
위의 예제에서 만약 mapping 연산이 병렬로 동작한다면, 동일한 입력에 대한 결과는 스레드 스케줄링의 차이로 인해 매 실행마다 다를수 있지만, stateless 람다식에서는 항상 결과가 같다.

매개변수에서 mutable 상태에 접근하려고 하면 안전과 성능면에서 좋지 않은 선택을 하게 된다. 만약 상태에 대한 접근을 synchronize 하지 않으면, 데이터는 경쟁상태에 놓이게 되며, code가 깨지게 된다. 그러나 만약 상태에 대한 접근을 synchronize 할 경우, 이익을 얻고자 했던 병렬성을 약화 시킨다.

가장 좋은 방법은 stateful 매개변수를 피함으로 스트림연산을 완벽하게 하는것이다.

## Side-Effects
스트림 연산으로 향하는 매개변수 안에 사이드 이팩트는 보통 권장하지 않는데, 그것들이 자주 statelessness 요구사항의 알수없는 위협 혹은 thread-safety에 대한 위협이 되기 때문이다.


만약 매개변수들이 사이드 이펙트를 갖고 있고, 명시적으로 stated 하지 않는다면, 다른 스레드를 향한 사이드 이펙트에 대한 가시성을 보장하지 않고, 같은 스레드의 스트림 파이프라인에 같은 요소에 대한 연산을 보장하지 않는다. 게다가, 이러한 효과의 순서는 놀라운 일일 수 있다. 파이프라인이 스트림 소스의 방문 순서와 일치하는 결과를 생성하도록 제약을 받는 경우에도, 주어진 요소를 위해 실행되는 어떠한 매개변수나 개별 요소에 적용되는 매퍼함수의 순서를 보장하지 않는다. 
```java
IntStream.range(0,5).parallel().map(x -> x*2).toArray() 

[0, 2, 4, 6, 8] // must produce
```
사이드이팩트를 사용하려고 유혹되는 많은 계산들은 더욱 안전하고 효과적으로 표현될 수 있다. 

mutable<sup>변하기 쉬운</sup> accumulators 대신에 [reduction](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html#Reduction)과 같은 것을 사용하여 사이드이펙트를 갖고 있는 많은 계산들이 더욱 안전하고 효과적으로 표현될 수 있다. 그러나 디버깅의 목적으로사용되는 println()과 같은 사이드 이팩트들은 보통 문제가 없다. forEach() 나 peek() 와 같은 몇개의 스트림 연산은 side-effects를 통해 연산하게 되는데 주의해서 사용해야 한다.

사이드 이펙트를 부적절하게 사용하는 스트림 파이프 라인을 변환하지 않는 방법으로, 아래의 코드는 정규식과 매칭되는 string을 스트림에서 찾아서 list에 넣는 예이다.
```java
ArrayList<String> results = new ArrayList<>();
     stream.filter(s -> pattern.matcher(s).matches())
           .forEach(s -> results.add(s));  // Unnecessary use of side-effects!
```
위의 코드는 불필요하게 side-effects를 사용한다. 만약 이것을 병렬로 실행했다면, thread-safe하지 않은 ArrayList가 오류를 일으키고, 그것을 보완하기 위해 동기화를 추가하려면 경합이 발생하여 병렬처리의 이득을 약화 시킨다. 게다가 여기서 사용한 side-effects는 완벽하게 불필요하다. forEach ()는보다 안전하고 효율적이며 병렬 처리가 더 용이 한 축소 작업으로 간단히 대체 될 수 있습니다.
```java
List<String>results =
         stream.filter(s -> pattern.matcher(s).matches())
               .collect(Collectors.toList());  // No side-effects!
```

## Ordering
