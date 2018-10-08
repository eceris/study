# [자바 11] Collection에 추가된 메소드

[동영상 링크](https://www.youtube.com/watch?v=RZB8_ABoo98)

JDK11의 collection interface에 새로운 default 메소드가 추가되었다. Stream interface의 [Stream.toArray(IntFcuntion)](https://docs.oracle.com/javase/10/docs/api/java/util/stream/Stream.html#toArray(java.util.function.IntFunction)) 과 비슷하게 동작하는 [Collection.toArray(IntFcuntion)](https://download.java.net/java/early_access/jdk11/docs/api/java.base/java/util/Collection.html#toArray(java.util.function.IntFunction)) 가 추가 되었다.

```java
final Set<String> names = Set.of("Fred", "Wilma", "Barney", "Betty");  
out.println(Arrays.toString(names.toArray(String[]::new)));
```


```java
publci void toArray() {
	List<String> hello = List.of("하이", "안녕", "헬로");

	String[] helloArray = hello.toArray(String[]::new);
	System.out.println(Arrays.toString(helloArray));
}
```

