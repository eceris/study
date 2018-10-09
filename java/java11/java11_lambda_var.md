# [자바 11] 람다에서 var 사용하기

[동영상 링크](https://www.youtube.com/watch?v=tjj-XLk4CSA)


### lambda 에서 var 사용 가능.
Java 10 이하에서 사용하는 방식이 편한데 왜 ??? `var` 를 람다식에서 쓰려고 하는거지???
아래 샘플 코드처럼 어노테이션을 사용할 수 있음.

```java
public static void main(String[] args) {
    Flux.just(1, 2, 3, 4, 5)
            .map((@NonNull var i) -> i * 2)
//          .map(i -> i * 2) // java 10 이하에서 사용하는 방식
            .subscribe(System.out::println);
}
```

타입에 붙일 수 있는 어노테이션들을 사용할 수 있다.

Java 10부터는 로컬 variable에는 var를 사용할 수 있도록 바뀌었는데, 사실 람다식에 있는 variable 도 로컬 variable 이니까...
확장판이지 않나? ㅋ