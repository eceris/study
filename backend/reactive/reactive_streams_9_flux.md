#Flux
curl 과 tar 툴이 윈도우 10에 포함되었다 !!!(2017년 12월 기준) 
놀람;;


Mono의 리스트와 Flux와 결과는 같음 .....

1. 그러나 Mono 리스트를 사용할 경우, Flux가 제공하는 fluent한 operator를 사용할 수 없음...
2. HttpStream을 사용할 경우에는 Flux가 더 편함.
@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
Flux<Event> events();
위의 경우 chunked로 나누어서 전송된다고 하더라 ...

Flux.delayElements();로 duration을 주면 백그라운드 스레드가 블록킹이 되면서 duration을 ...갖는다 ...

이건 SSE의 개념과 비슷...


Flux.zip()

비지니스 로직과 인프라에 관한 FLUX가 동일한 Chain에 있을 경우, 더러워짐 ..
이걸 ... 해주는 것...지퍼 와 비슷한 개념

Flux.zip(flux, flux).map(tu -> tu.getT1());
zip하면 튜플이라는 하나의 데이터 집합이 나오고 위의 예는 그중 앞에 데이터를 보낸다는 것. ..

zip은 모든 flux를 묶을수 있다.

보통은 데이터를 zipping하는데 많이 사용함 .....




