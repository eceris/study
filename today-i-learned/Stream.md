# [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)

## Package java.util.stream Description
이 패키지에 주된 추상화는 바로 Stream 이다. 스트림은 컬렉션과는 몇가지 이유에서 조금 다른데,

1. 저장공간이 없다. 스트림은 요소를 저장하는 **데이터구조**가 없다. 대신에 이것은 Source에서 computational operation의 pipeline을 통해  데이터의 구조, 배열, generator function, I/O채널등등을 전달한다.
2. Functional하다. Stream의 Operation은 **결과**를 **생산할 뿐 Source를 수정하지는 않는다.** 예를 들면 Stream의 filtered는 컬렉션의 요소들을 제거하지 안하고 새로운 Stream을 생성한다.
3. Lazyness를 추구한다. Stream의 많은 operation들은<sup>filter, map, duplicate removal..</sup> 최적화할 수 잇는 기회를 제공하기 위해 lazy하게 구현될 수 있다. 예를 들면, "3개의 연속된 모음을 가진 첫번째 문자열을 찾는 문제"에서 Stream은 모든 입력 문자열을 검사하지 않는다. Stream operation은 항상 
중간<sup>Stream-producing</sup>작업과 종단(value- or side-effect-producing)작업으로 분리되는데, 중간작업은 항상 lazy이다.
4. 무한대일수 있다. 컬렉션의 크기는 한정되어있지만, 스트림은 그렇지 않다. limit()이나 findFirst()와 같은 Short-circuiting operations는 무한 스트림에 대한 계산을 유한 시간으로 완료 할 수 있다.
5. Consumable 하다. 스트림의 각 요소는 Iterator처럼 생성주기 동안 한번만 확인한다. 그래서 재검토를 하기 위해서는 새로운 스트림을 생성해야 할 수도 있다.

스트림은 아래의 몇가지 방법으로 생성되는데,

1. [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html) 으로부터 stream()이나 parallelStream으로 생성.
2. 배열로부터 [Arrays.stream(Object[])](https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#stream-T:A-)을 통해 생성.
3. stream 클래스의 static factory method를 통해([Stream.of(Object[])](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#of-T...-), [IntStream.range(int, int)](https://docs.oracle.com/javase/8/docs/api/java/util/stream/IntStream.html#range-int-int-), [Stream.iterate(Object, UnaryOperator)](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#iterate-T-java.util.function.UnaryOperator-))
4. 파일의 라인을 얻기위해 [BufferedReader.lines()](https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html#lines--)를 통해
5. 파일의 paths를 얻기위해 [Files](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html)를 통해
6. 임의의 숫자를 얻기 위해 [Random.ints()](https://docs.oracle.com/javase/8/docs/api/java/util/Random.html#ints--)
7. JDK의 수많은 다른 스트림 지원 메소드를 통해 [BitSet.stream()](https://docs.oracle.com/javase/8/docs/api/java/util/BitSet.html#stream--), [Pattern.splitAsStream(java.lang.CharSequence)](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#splitAsStream-java.lang.CharSequence-), [JarFile.stream()](https://docs.oracle.com/javase/8/docs/api/java/util/jar/JarFile.html#stream--).

## Stream operations and pipelines
스트림의 operation들은 중간<sup>intermediate</sup>작업과 종단<sup>terminal</sup>작업으로 나뉘어지고, 결합되어 스트림 파이프라인을 형성한다. Source(Collection, 배열, generator function, I/O채널)로 구성된 스트림의 파이프라인은 Stream.filter 또는 Stream.map과 같은 0개 이상의 중간 연산과 Stream.forEach 또는 Stream.reduce와 같은 종단 작업이 포함된다.

중간 Operation들은 새로운 스트림을 return하는데, 항상 `lazy`다. 사실 filter() 와 같은 중간 연산자는 실제로 어떤 필터링도 수행하지 않고, 요소를 순회<sup>traverse</sup>할 때 주어진 predicate와 일치하는 요소를 포함하는 새로운 stream을 생성한다. 파이프 라인 소스의 순회<sup>traversed</sup>는 파이프 라인의 종단 연산이 실행될때까지 시작하지 않는다.

Stream.forEach 또는 IntStream.sum과 같은 종단<sup>terminal</sup> 연산은 결과나 어떤 side-effect를 생산하기 위해 스트림을 traverse 한다.

스트림을 순회하여 결과 또는 부작용을 일으키는데, **종단 연산**이 수행 된 후에는 스트림 파이프 라인은 소비 된 것으로 간주되어 더 이상 **사용불가**다. 동일한 데이터 소스를 다시 순회<sup>traverse</sup>해야하는 경우 데이터 소스로 돌아와서 새 스트림을 가져와야 한다. 거의 모든 경우에, 종단 연산은 `eager`로 작동하며, 리턴하기 전에 파이프 라인의 처리와 데이터 소스의 순회를 완료한다. 

그러나 종단 연산중에 iterator()와 spliterator()는 조금 다른 `escape hatch`<sup>(비행기따위의)피난용비상구</sup>로서 제공된다.(기존 연산이 task에 충분하지 않은 이벤트에서 임의의 client-controlled 파이프라인 순회를 가능하게 하기 위해)

스트림에 대한 Lazy 처리가 상당히 효율적인데, 스트림 파이프라인에서 filtering, mapping, reducing 이 최소의 중간 상태에서 하나의 pass로 융합될수 있다. Lazy 처리는 필요하지 않은 데이터를 모두 검사하는 것도 피할 수 있다.(이런 동작은 input stream이 무한대이거나 그렇게 크지 않을때 더욱 중요하다.)

중간작업은 **stateless** 와 **stateful** 연산으로 나뉜다. Stateless<sup>filter, map</sup> 연산은 새로운 요소를 처리할 때, 이전 요소를 stateless로 유지한다.(각 요소는 연산과 독립적) Stateful<sup>distinct, sorted</sup> 연산은 새로운 요소를 처리할 때, 이전 요소의 상태를 통합한다.

Stateful 연산은 전체 요소를 처리해야할 수도 있다. 예를들면 sorted 함수는 스트림의 모든 요소를 확인하기 전엔 정렬하여 결과를 생성할 수 없다. 결과적으로 parallel 연산에서 stateful 중간 연산을 포함하는 일부 파이프 라인은 데이터를 여러 번 통과해야하거나 중요한 데이터를 버퍼해야 할 수도 있다. Stateless 중간 연산만 포함하는 파이프라인은 데이터 버퍼링을 최소화 하면서(parallel or sequential) 단일 패스로 처리할 수 있다.

게다가, 일부 연산은 short-circuiting operations으로 처리한다. 중간 연산은 short-circuiting 이다. 만약 무한입력으로 표현될 때, 결과적으로 유한한 stream을 생성할 수 있다. 단지 연산은 무한 입력으로 제시 될 때 유한 한 시간 내에 종료 될 수있는 경우 종료된다. 파이프 라인에서 단락 연산을 수행하는 것은 필요하지만 충분하지는 않습니다. 무한 스트림이 유한 시간에 정상적으로 종료되도록 처리하는 조건입니다.

## Parallelism
명시적으로 for-loop가 있는 요소의 처리는 직렬이다. 스트림은 집계연산<sup>aggregate operation</sup>의 파이프 라인으로 계산을 다시 매핑하여 **병렬실행**을 가능하게 한다.(각 요소에 명령형이 아니다.) 모든 스트림의 연산은 serial 혹은 parallel로 실행할수 있는데, 기본은 serial 이다. 예를들면 컬렉션의 Collection.stream()과 IntStream.range(int, int)의 경우 순차적인 스트림이지만, BaseStream.parallel()를 활용해 효과적으로 병렬화 할 수 있다. 앞의 예를 병렬로 실행하면,

```java
int sumOfWeights = widgets.parallelStream()
  .filter(b -> b.getColor() == RED)
  .mapToInt(b -> b.getWeight())
  .sum();
```

Serial과 Parallel의 유일한 차이점은 initial stream의 생성을 parallelStream()으로 하느냐 stream()으로 하느냐 이다. 종단 연산이 시작되면, 스트림 파이프라인은 호출되는 스트림의 방향에 따라 순차적으로 또는 병렬로 실행된다. 스트림을 직렬 또는 병렬로 실행할지 여부는 isParallel 메소드를 사용하여 결정할 수 있고, 스트림의 방향은 BaseStream.sequential() 과 BaseStream.parallel을 사용하여 수정할 수 있다. 종단 작업이 시작되면 스트림 파이프 라인은 호출되는 스트림의 모드에 따라 순차적으로 또는 병렬로 실행된다. 

findAny()와 같이 명시적으로 **비결정적인 것**으로 식별되는 연산을 제외하고는 스트림이 순차적 or 병렬적으로 실행되는지 여부가 계산의 결과를 변경해서는 안된다. 

대부분의 스트림 연산은 람다식으로도 파라미터를 받을수 있는데 올바른 동작을 유지하려면, 이러한 파라미터들이 반드시 **비간섭적**이어야 하고 **stateless** 여야만 한다. 이런 파라미터는 항상 [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)과 같은 기능의 인터페이스 인스턴스이며, 람다 혹은 메서드 참조 이다.

## Non-interference
스트림은 다양한 데이터 소스(ArrayList와 같은 non-thread-safe를 포함)에 대해 병렬 처리를 수행할 수 있게 해준다. 이것은 스트림 파이프라인의 실행 중에 데이터 소스의 간섭을 방지 할 수 있는 경우에만 가능하다. 그러나 `escape-hatch` 연산인 iterator()와 spliterator()를 제외하고 실제 실행은 종단 연산이 호출될 때 시작되고 종단 연산이 완료될때 끝난다. 대부문의 데이터 소스에서 간섭의 방지는 스트림 파이프라인의 실행 중 **데이터소스**가 전혀 **수정되지 않도록** 보장한다. 살펴볼 만한 예외는 데이터 소스가 동시 수집인 동시에 동시 수정을 처리하도록 설계된 스트림이다. Concurrent 스트림 소스는 Spliterator가 CONCURRENT 특성을 보고한다.

게다가, 동시성을 보장하지 않는 데이터들이 스트림의 파이프라인에 속할 경우 스트림 파이프라인의 매개변수들은 스트림의 데이터 소스를 수정해서는 안된다. 매개변수가 스트림의 데이터 소스를 수정하거나 수정의 원인이 되면, non-concurrent 데이터 소스를 방해한다고 한다. 병렬 파이프라인 뿐 아니라, 모든 파이프라인에는 간섭하지 않는 것이 가이드이다. 스트림의 소스가 동시성을 보장하는 한, **스트림 파이프라인의 실행 동안** 스트림의 **데이터소스**를 변경하는 것은 **예외**와 잘못된 결과를 야기하는 **원인**이 된다. 잘 동작하는 스트림의 소스의 경우, 소스는 종단<sup>terminal</sup> 연산 시작 전에 소스를 수정할 수 있다. 그리고 그러한 작업들은 모든 요소에 영향을 미친다. 아래의 코드가 그 예이다.
```java
List<String> l = new ArrayList(Arrays.asList("one", "two"));
Stream<String> sl = l.stream();
l.add("three");
String s = sl.collect(joining(" "));
```

먼저, 두개의 string이 포함된 리스트를 생성한 후, 스트림을 생성하였다. 그리고나서 리스트에 새로운 요소가 추가되었다. 결과적으로 스트림의 요소들은 collected되고 함계 joined 된다. 위와 같이 코드를 실행 할 경우, 결과는 `one two three`가 나온다. 마지막으로 list에 요소를 수정한 이후 마지막에 **collect** 라는 **종단 연산**이 시작되었기 때문이다. 모든 스트림은 jdk 컬렉션으로부터 리턴되고 대부분의 JDK 클래스들은 잘 동작하도록 **보장**해준다.

## Stateless behaviors
매개변수가 상태를 갖고 있는 **Stateful** 인 경우, 스트림 파이프라인의 결과는 **비결정적이거나 올바르지 않을** 수 있다. stateful 람다식(또는 fn interface의 구현체)은 스트림의 파이프라인 실행동안 변경될 수 있는 상태에 따라 결과가 달라진다. stateful 람다식이 map함수의 매개변수로 들어있는 예제 이다. 

```java
Set<Integer> seen = Collections.synchronizedSet(new HashSet<>());
stream.parallel()
      .map(e -> { if (seen.add(e)) return 0; else return e; })...
```
위의 예제에서 만약 mapping 연산이 병렬로 동작한다면, 동일한 입력에 대한 결과는 스레드 스케줄링의 차이로 인해 매 실행마다 다를수 있지만, stateless 람다식에서는 항상 결과가 같다.

매개변수에서 mutable<sup>변하기 쉬운</sup> 상태에 접근하려고 하면 안전과 성능면에서 좋지 않은 선택을 하게 된다. 만약 상태에 대한 접근을 **synchronize** 하지 않으면, 데이터는 경쟁상태에 놓이게 되며, code가 깨지게 된다. 그러나 만약 상태에 대한 접근을 synchronize 할 경우, 이익을 얻고자 했던 **병렬성을 약화** 시킨다.

가장 좋은 방법은 **stateful** 매개변수를 **피함**으로 스트림연산을 완벽하게 하는것이다.

## Side-Effects
스트림 연산으로 향하는 매개변수 안에 사이드 이팩트는 보통 권장하지 않는데, 그것들이 자주 statelessness 요구사항의 알수없는 위협 혹은 thread-safety에 대한 위협이 되기 때문이다.

만약 매개변수들이 사이드 이펙트를 갖고 있고, 명시적으로 stated 하지 않는다면, 다른 스레드를 향한 사이드 이펙트에 대한 가시성을 보장하지 않고, 같은 스레드의 스트림 파이프라인에 같은 요소에 대한 연산을 보장하지 않는다. 게다가, 이러한 효과의 순서는 놀라운 일일 수 있다. 파이프라인이 스트림 소스의 방문 순서와 일치하는 결과를 생성하도록 제약을 받는 경우에도, 주어진 요소를 위해 실행되는 어떠한 매개변수나 개별 요소에 적용되는 매퍼함수의 순서를 보장하지 않는다. 
```java
IntStream.range(0,5).parallel().map(x -> x*2).toArray() 

[0, 2, 4, 6, 8] // must produce
```
사이드이팩트를 사용하려고 유혹되는 많은 계산들은 더욱 안전하고 효과적으로 표현될 수 있다. 

mutable<sup>변하기 쉬운</sup> accumulators 대신에 [reduction](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html#Reduction)과 같은 것을 사용하여 사이드이펙트를 갖고 있는 많은 계산들이 더욱 안전하고 효과적으로 표현될 수 있다. 그러나 디버깅의 목적으로사용되는 println()과 같은 사이드 이팩트들은 보통 문제가 없다. **forEach()** 나 **peek()** 와 같은 몇 개의 스트림 연산은 side-effects를 통해 연산하게 되는데 **주의**해서 사용해야 한다.

사이드 이펙트를 부적절하게 사용하는 스트림 파이프 라인을 변환하지 않는 방법으로, 아래의 코드는 정규식과 매칭되는 string을 스트림에서 찾아서 list에 넣는 예이다.
```java
ArrayList<String> results = new ArrayList<>();
     stream.filter(s -> pattern.matcher(s).matches())
           .forEach(s -> results.add(s));  // Unnecessary use of side-effects!
```
위의 코드는 불필요하게 side-effects를 사용한다. 만약 이것을 병렬로 실행했다면, thread-safe하지 않은 ArrayList가 오류를 일으키고, 그것을 보완하기 위해 동기화를 추가하려면 경합이 발생하여 병렬처리의 이득을 약화 시킨다. 게다가 여기서 사용한 side-effects는 완벽하게 불필요하다. forEach()는 보다 안전하고 효율적이며 병렬 처리가 더 용이 한 축소 작업으로 아래와 같이 간단히 대체 될 수 있다.

```java
List<String>results =
         stream.filter(s -> pattern.matcher(s).matches())
               .collect(Collectors.toList());  // No side-effects!
```

## Ordering
스트림은 마주치는<sup>encounter</sup> 순서가 있을 수도, 없을 수도 있다. 스트림에 마주치는<sup>encounter</sup> 순서가 있는지 여부는 데이터 소스와 중간 연산자<sup>intermediate operations</sup>에 따라 다르다. 특정 스트림 소스(List나 Array 같은)는 본질적으로 순서가 지정되며 HashSet과 같은 애들은 순서가 없다. **sorted()** 와 같은 몇몇 중간 연산자는 순서가 지정되지 않는 스트림에 순서를 지정할 수 있으며, 다른 연산자들은 순서가 지정된 스트림을 **BaseStream.unordered()** 와 같이 순서를 지정하지 않은 상태로 렌더링할 수 있다. 게다가 forEach()와 같은 몇몇 terminal 연산자는 순서를 무시할 수 있다. 
만약 스트림이 정렬되었다면, 대부분의 연산자자들은 encounter 순서대로 연산한다. 만약 스트림의 소스가 [1, 2, 3]을 포함한 list라면 map(x -> x\*2)의 결과는 [2, 4, 6]이 될것이다. 그러나 소스의 encounter 순서가 정의되어있지 않으면 [2, 4, 6] 값의 순열이 유효한 결과가 된다.

순차적인 스트림의 경우, encounter 순서의 유무는 성능에 영향을 주지 않으며, 결과에만 영향을 준다. 만약 스트림이 정렬되었다면, identical한 소스의 스트림 파이프라인이 반복적으로 실행되는 경우, 결과도 identical하다. 만약 정렬되어 있지 않다면, 다른 결과를 생성한다. 

병렬 스트림의 경우, **순서를 보장하는 조건**을 **완화**하면 더 **효율적**으로 실행한다. 요소의 순서가 적절하지 않은 경우 distinct()나 Collectors.groupingBy()와 같은 aggregate 연산자를 이용하여 연산을 효율적으로 구현할 수 있다. 비슷하게 **limit()**와 같은 **순서를 보장하기 위한 연산자**는 병렬처리의 이점을 **약화** 시킨다. 스트림에 encounter 순서가 있지만, 중요하지 않은 경우에 unsorted() 로 스트림을 명시적으로 정렬해제 하면, 일부 stateful한 연산이나 terminal 연산의 병렬 성능이 향상될 수도 있다. 그러나, 대부분의 스트림 파이프라인은 순서의 제약조건에서도 효율적으로 병렬처리 한다.

## Reduction operations
Reduction 연산<sup>fold 라고도 함</sup>은 입력을 취합하여 숫자의 합 혹은 최대값을 찾거나 요소를 목록에 누적하는 등의 결합작업을 반복적으로 적용하여 단일 요약 결과로 결합한다. 스트림 클래스에는 reduce(), collect(), sum(), max() 또는 count()와 같은 특별한 함수들이 있다. 물론 이러한 작업은 아래와 같은 간단한 순차루프로 쉽게 구현할 수 있다.
```java
int sum = 0;
for (int x : numbers) {
	sum += x;
}
```
그러나, 위의 예제와 같은 mutative 연산보다 reduction 연산을 선호하는 여러 이유가 있다. 

reduction 연산이 "좀 더 추상적" 이라는 것 뿐 아니라 요소를 처리하는 데 사용되는 함수가 연관적이고 stateless 인 경우, 본질적으로 병렬처리가 가능하다는 것이다. 예를 들어 합계를 찾고자하는 숫자의 흐름이 주어지면 아래와 같이 쓸 수 있다.
```java
int sum = numbers.stream().reduce(0, (x,y) -> x+y);
```
or
```java
int sum = numbers.stream().reduce(0, Integer::sum);
```

이러한 reduction 연산들은 별다른 수정없이 병렬로 안전하게 수행된다.
```java
int sum = numbers.parallelStream().reduce(0, Integer::sum);
```

병렬로 Reduction 하는 작업은 잘 수행된다. Language가 parallel for-each 구조를 갖고 있다고 하더라도, mutative 연산에 대한 접근은 thread-safe한 업데이트를 제공하기 위해 개발자가 신경써야 하는데, 이러한 작업은 병렬처리의 이점을 살리지 못한다. 대신 reduce()함수를 사용하면, reduction 작업을 병렬처리하는 부담이 제거되므로 라이브러리는 **추가 동기화**가 필요 없는 **효율적인 병렬 구현**을 제공할 수 있다. 

위에서 보여진 위젯 예제는 루프가 대량 작업으로 대체되는 다른 작업과 축소가 어떻게 결합되는지 보여준다. 위젯이 getWidget() 메소드가 있는 위젯 객체의 집합인 경우, 아래와 같이 가장 큰 위젯을 찾을 수 있다.
```java
OptionalInt heaviest = widgets.parallelStream()
                           .mapToInt(Widget::getWeight)
                           .max();
```
좀 더 일반적인 형태로, <U> Type의 결과를 생성하는 <T> Type의 요소에 대한 reduce 연산은 세 개의 매개 변수를 필요로 하는데,

```java
 <U> U reduce(U identity,
              BiFunction<U, ? super T, U> accumulator,
              BinaryOperator<U> combiner);
```
identity 요소는 reduction을 위한 초기 seed value 이며, 입력이 없을 경우 default value 이다. accumulator 함수는 partial result와 다음 요소를 가져와 새로운 partial result를 생성한다. combiner Operator 는 두 개의 partial result를 결합하여 새로운 partial result를 생성한다. 입력이 나뉘어져 있고 부분 accumulation이 각각의 입력을 계산하여 최종 결과를 만들기 위해 combiner는 병렬 reductions에 필수이다.

더욱 formal하게, identity 값은 combiner 함수에서 유일해야 한다. 이것은 모든 u에 대해  combiner.apply(identity, u) 는 u와 같다. 게다가 combiner 함수는 **연관적**이어야하며, accumulator와 호환 가능해야 한다. 모든 u와 t에 대해 combiner.apply (u, accumulator.apply (identity, t))는 accumulator.apply (u, t)와 같아야한다. 

세개의 매겨변수 형식은 두개의 매개 변수 형식에 accumulation 스텝을 매핑 단계에 통합해서 일반화 한 것이다. 보다 일반적인 방식을 사용하여 위 예제를 다시 작성하면.

```java
int sumOfWeights = widgets.stream()
                           .reduce(0,
                                   (sum, b) -> sum + b.getWeight())
                                   Integer::sum);
```
명시적으로 map-reduce를 선언 하는 것이 보다 읽기 쉽다. 일반화된 form은 매핑과 단일 함수로 결합하여 최적화 될 수 있는 중요한 작업을 위해 제공된다.

## Mutable reduction
변경 가능한 reduction 작업은 스트림의 요소를 처리할 때, 입력된 요소들을 변경가능한 결과 container<sup>Collection or StringBuilder 와 같은...</sup> 에 결과를 모은다.
만약 하나의 긴 string을 stream으로 받거나 concatenate 하기를 바란다면, 우리는 아래와 같은 reduction 연산으로 처리할 수 있다.

```java
String concatenated = strings.reduce("", String::concat);
```
위의 코드는 병렬로 동작할 수 있는데, 생각 처럼 performance에 이득을 얻지 못한다. 이러한 구현은 많은 문자열 copying을 수행하며 실행 시간은 **O(N^2)**이 걸린다. 조금 더 성능관점에서 접근한다면 StringBuilder<sup>mutable container</sup>를 사용하여 문자들을 accumulate 할 수 있다. 위의 코드를 일반적인 reduction과 마찬가지로 병렬로 수행할 수 있는 방법이 있다.

mutable reduction 연산은 collect()라는 것인데 이것은 Collection과 같은 container에 결과를 원하는데로 모아준다. collect() 연산은 3가지 함수가 필요한데,

1. supplier : 결과 container의 새로운 인스턴스 생성을 위해
2. accumulator : 입력된 요소를 결과 conatiner에 모으기 위해
3. combining : 결과 container를 다른 container에 merge 하기 위해. 

이러한 form은 일반적인 reduction 연산과 매우 비슷하다. 

```java
<R> R collect(Supplier<R> supplier,
       BiConsumer<R, ? super T> accumulator,
       BiConsumer<R, R> combiner);
```

reduce()연산과 마찬가지로 collect()라는 추상적인 방법을 사용하면 병렬처리가 직접적으로 가능하다. accumulate와 combine이 적절한 요구사항 대로 구현되면, partial 결과들을 병렬로 축적하여 결합 할 수 있다. 예를 들면, 스트림에 있는 요소의 String 을 ArrayList에 모으기 위해 for-each를 사용해서 작성할 수 있다.

```java
ArrayList<String> strings = new ArrayList<>();
for (T element : stream) {
 strings.add(element.toString());
}
```

또는 이렇게 병렬화 할수 있다.
```java
ArrayList<String> strings = stream.collect(() -> new ArrayList<>(),
                                    (c, e) -> c.add(e.toString()),
                                    (c1, c2) -> c1.addAll(c2));
```

또는, accumulator함수에서 mapping 연산을 땡겨오면 더 간결하게 표현 할 수 있다.
```java
List<String> strings = stream.map(Object::toString)
                              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
```

여기에 supplier는 ArrayList의 생성자인데, accumulator는 요소를 stringify 해서 ArrayList에 모으고, combiner는 addAll 을 이용해 다른 container로 복사했다. 

collect에 세가지 --supplier, accumulator, combiner-- 는 타이트하게 응집되어있다. 우리는 Collector에서 추상화된 연산을 사용할 수 있다. 

```java
List<String> strings = stream.map(Object::toString)
                          .collect(Collectors.toList());
```

mutable reduction들을 Collector에 패키징하는 것은 다른 이점이 있다. 
- composability : Collector 클래스는 collectors 를 위한 predefind 된 여러 팩토리를 포함한다.(하나의 collector를 다른 것으로 변환하는 combinator를 포함.) 예를 들면, 아래와 같이 직원 스트림의 급여합계를 계산하는 collector이다.

```java
Collector<Employee, ?, Integer> summingSalaries
     = Collectors.summingInt(Employee::getSalary);
```

groupingBy를 사용하여 부서 별로 합을 생성할 수 있다.
```java
Map<Department, Integer> salariesByDept
     = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment,
                                                        summingSalaries));
```
regular reduction 연산과 마찬가지로 collect() 연산은 병렬화 할 수 있다. 부분적으로 accumulated 된 결과의 경우 빈 결과 container와 결합해도 동일한 결과가 생성된다. 즉, 부분적으로 accumulate된 결과값 p는 combiner.apply(p, supplier.get()) 과 동일해야 한다.

또한, 계산이 분할되지만 동일한 결과를 산출해야 한다. 어떠한 입력 요소 t1, t2와 결과 값 r1, r2는 계산의 결과로 반드시 같아야 한다.

```java
A a1 = supplier.get();
accumulator.accept(a1, t1);
accumulator.accept(a1, t2);
R r1 = finisher.apply(a1);  // result without splitting

A a2 = supplier.get();
accumulator.accept(a2, t1);
A a3 = supplier.get();
accumulator.accept(a3, t2);
R r2 = finisher.apply(combiner.apply(a2, a3));  // result with splitting
```

이것은 일반적으로 Object.equals(Object)와 동일하며, 몇몇 경우에는 순서의 차이를 위해 동등성이 완화 될 수 있다.

## Reduction, concurrency, and ordering

Map을 생성하는 collect()와 같은 복잡한 reduction 연산이 있는 경우
```java
Map<Buyer, List<Transaction>> salesByBuyer
      = txns.parallelStream()
              .collect(Collectors.groupingBy(Transaction::getBuyer));
```

**병렬**로 위의 연산을 실행하는 것은 **비생산적**이다. combining 하는 연산이 실제로 몇몇 Map 구현체에서 비용이 많이 들기 때문이다. 

그러나 [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html) 과 같은 동시에 수정할 수 있는 결과 container가 reduction 연산에 사용되었다고 가정하자. 이 경우에, accumulator의 병렬 호출은 같은 공유된 결과 container에 동시에 보관할 수 있으므로 combiner가 병합을 위해 별도의 result container를 갖을 필요가 없다. 이것은 잠재적으로 병렬 연산의 성능을 향상시킨다. 이것을 **concurrent reduction**이라고 부른다.

**concurrent reduction**을 지원하는 Collector는 **Collector.Characteristics.CONCURRENT** 특성으로 마킹되어있다. 그러나 concurrent collection도 좋지 않은 단점을 갖고 있다. 만약 여러개의 쓰레드가 공유된 container에 동시에 결과를 저장한다면, 결과가 저장되는 순서는 비 결정적이다. 결과적으로 concurrent reduction은 **처리되는 순서가 중요하지 않은** 스트림에서만 사용할 수 있다. Stream.collect(Collector); 는 concurrent reduction을 아래와 같은 경우에만 구현할 수 있다.

1. stream이 병렬일 때
2. collector가 Collector.Characteristics.CONCURRENT 특성을 가질때
3. 스트림이 정렬되어있지 않거나, collector가 Collector.Characteristics.UNORDERED 특성을 가질때

BaseStream.unordered() 메소드로 스트림이 정렬되지 않음을 보장할 수 있다. 예를 들면...

```java
Map<Buyer, List<Transaction>> salesByBuyer
   = txns.parallelStream()
         .unordered()
         .collect(groupingByConcurrent(Transaction::getBuyer));
```
(Collectors.groupingByConcurrrent()는 groupingBy()의 동시접근 가능한 버전이다.)

## Associativity
연산자나 함수 op 는 다음과 같은 경우 상호 연관된다.

  (a op b) op c == a op (b op c)

이것을 네가지 용어로 확장하면 병렬 evaluation의 중요성을 볼수 있게 된다.

     a op b op c op d == (a op b) op (c op d)


또한 병렬로 (a op b) 와 (c op d)를 평가할 수 있고, 결과 위에서 op를 다시 invoke 한다. 
**연관 연산**의 예로 numeric addition, min, max 그리고 string의 concatenation 이 있다.


## Low-level stream construction

지금까지 스트림을 얻기 위해 Collection.stream() 혹은 Arrays.stream(Object [])와 같은 메서드를 사용했다. 이러한 Stream-bearing 함수들은 어떻게 구현될까?

[StreamSupport](https://docs.oracle.com/javase/8/docs/api/java/util/stream/StreamSupport.html)클래스는 스트림의 생성을 위해 많은 low-level 함수를 제공하는데 이러한 것들은 모두 **Spliterator**의 몇몇 형식을 이용한다. Spliterator는 Iterator와 비슷하지만 **병렬**로 동작합니다. 이것은 요소의 컬렉션(아마도 무한대)을 표현하고, 대규모의 데이터 순회, 그리고 입력의 일부를 병렬로 처리할 수 있는 다른 spliterator로 분할 하는 기능을 지원한다. 가장 lowest 레벨에서 모든 stream은 spliterator로 동작한다. 
spliterator의 구현에는 여러 선택사항이 있는데, 거의 모든것이 구현의 단순함과 해당 spliterator를 사용하는 스트림의 성능을 tradeoff 한 것이다. 간단하지만 적어도 성능을 생각한 spliterator를 생성하는 방법은 [Spliterators.spliteratorUnknownSize(java.util.Iterator, int)](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterators.html#spliteratorUnknownSize-java.util.Iterator-int-)를 사용한 iterator를 사용하는 것이다. 만약 이러한 spliterator가 동작하면, 데이터셋이 얼마나 큰지를 알지 못하고 단순한 분할 알고리즘에 영향으로 **병렬**에서는 **좋지 못한 성능**을 낼것이다. 

high-quality spliterator는 균형잡히고, 알려진 split 크기와 정확한 사이즈 정보를 토대로 실행을 최적화 하기 위해 구현에서 사용할 수 이쓴 여러가지 특성들을 제공한다. 

변경 가능한 데이터 소스를 위한 Spliterators 에는 추가적인 과제가 있다. spliterator의 생성시간과 stream의 파이프라인이 실행되는 시간 사이에 데이터가 변경될 수 있으므로, 이상적으로는 스트림의 spliterator는 **IMMUTABLE**이나 **CONCURRENT 특성**을 보고 해야 하거나, 그렇지 않으면 late-binding 이어야 한다. 만약 소스가 추천되는 spliterator를 직접적으로 제공할 수 없는 경우, **Supplier**를 사용하여 spliterator를 간접적으로 제공하고 Supplier-accepting 버전의 stream()을 통해 스트림을 구성할 수 있다. Spliterator는 스트림 파이프라인의 terminal 연산이 시작된 후에만 Supplier로 부터 얻는다. 
이러한 요구사항은 스트림 source의 이상동작과 스트림 파이프라인 실행의 **잠재적인 간섭 범위**를 크게 줄인다. 