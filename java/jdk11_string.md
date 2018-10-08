# [자바 11] String에 추가된 메소드를 살펴보자

[동영상 링크](https://www.youtube.com/watch?v=WX_MFJk9H3E&t=17s)


### strip() 추가
unicode도 대응 되는 기존 trim()의 발전된 형태, 기존에는 유니코드에서 공백을 뜻하는 `\u205F` 를 trim()으로도 대응할 수 없었으나 strip()으로 대응가능하다.

```java
public void strip() {
	String test = " test \u205F";
	System.out.println(test.trim().length());
	System.out.println(test.strip().length());
}
```

### isBlank() 추가
strip()과 비슷한 이슈인데, 유니코드의 공백을 대응하는 isEmpty()의 발전된 형태, 유니코드의 공백 문자도 공백으로 보고 true를 반환.
```java
public void isBlank() {
	String test = " test \u205F";
	System.out.println(test.isEmpty());
	System.out.println(test.isBlank());
}
```

### lines() 추가
String 문자열을 line 별로 streaming 해서 출력함.
```java
public void lines() {
	String name = "안녕하세요.\n 만나서 반갑습니다. \n 나중에 다시만나요.";
	names.lines().foreach(System.out::println);
}
```

### repeat() 추가
문자열을 반복해서 출력.
```java
public void repeat() {
	String name = "안녕하세요.";
	System.out.pringlne(name.repeat(10));
}
```