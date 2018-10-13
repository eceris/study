# [자바 11] Files 변경 사항

[동영상 링크](https://www.youtube.com/watch?v=NoWlvaUbyI0)


### 파일에 들어있는 문자열 읽어오기
아주 간단한데 Files 에 아래 api가 추가되었다.
단순하게 제공되는 path에서 문자열을 읽어옴.
```java
public static String readString(Path path) throws IOException
```

### 파일에 문자열 쓰기
제공되는 path에 문자열을 쓴다.
```java
public static String writeString(Path path, CharSequence csq, OpenOption... options) throws IOException
```