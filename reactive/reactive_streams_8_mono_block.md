#Mono와 block()
Mono.just는 block이고
Mono.fromSupplier는 nonblock


OperatorFusion....Operator의 성능을 높히기 위해 Fusion을 하거나 뭐 하는데.. 굉장히 난이도가 높다 ...



모노나 플럭스 같은 퍼블리셔는 
섭스크라이버를 여러개 가질수 있다.

퍼블리셔가 데이터를 공급하는 타입은 두가지 
HOT, COLD
어느 섭스크라이버가 호출하던 간에 동일한 데이터가 나는 거는 COLD SOURCE타입

실시간으로 일어나는 것들은 hot source type
 

 Mono.block() 하는 순간 Mono에 들어있는 value를 얻을수는 있지만 블락이된다 .... 논블라킹이 아님 ;;
 결과적으로 subscribe()와 거의 동일한방식 ..