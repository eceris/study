

## Operator
for - > Stream.iterator().limit(10).collect(Collectors.toList())

deletegate subscriber

functional interface

Function<Integer, Integer> f

BiFunction<Integer, Integer, Integer> f


T << generic








Flux <-- Publisher
 


Flux 에 타입을 줄때는 

Flux.<Integer> craete().map().subscribe();....


이후에는 Flux의 operator들이 여러개 있음
ex : map, reduce ... 뭐 등등
map은 말그대로 a -> a1으로 변경
reduce는 a, b, c 라는 elem이 stream으로 들어올때 걔네를 sum한다던가 ..해서
한꺼번에 subscribe..하는 것


각각의 operator가 어떤 기능을 갖고 있는지 diagram으로 보고 싶은 경우
projectreactor.io/core/docs/api에 Flux를 보자



Spring 으로 적용
Publisher<String> 으로 리턴

스프링에서는 Publisher만 만들어서 리턴하면, 스프링이 지가 알아서 Subscriber를 구현한다.
