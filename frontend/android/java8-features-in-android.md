# Java 8 feature in Android

## INTRODUCE
Java 8에서 제공하는 여러 feature들을 Android 앱에서도 사용하고 싶다. 안드로이드에서는 Java8에서 제공하는 여러가지 feature를 ApiVersion 24에서 사용하도록 [가이드](https://developer.android.com/guide/platform/j8-jack?hl=ko) 하고 있다. 하지만, 현재 유지보수하는 어플리케이션은 ApiVersion 19 부터 지원하고 있다...... ㅠㅠㅠ
어쨌는 검색 결과, 하위 버전의 ApiVersion에서도 사용할 수 있도록 하는 오픈소스 라이브러리를 발견했다.

## RetroLambda
[RetroLambda](https://github.com/luontola/retrolambda)는 Java8의 lambda expression을 Java1.7, 1.6 에서 사용할 수 있도록 해주는 [Backporting](https://m.blog.naver.com/mug896/140186450421) 라이브러리이다. 

## Lightweight-Stream-API
[Lightweight-Stream-API](https://github.com/aNNiMON/Lightweight-Stream-API) 는 Java8의 StreamAPI를 rewritten하여 Java 1.7 혹은 그 하위 버전에서 사용할 수 있도록 해주는 라이브러리이다. 

## 사용방법

 - () -> lambda expression

   ```java
   findViewById(R.id.go).setOnClickListener(v -> {
      final int index = mActionSpinner.getSelectedItemPosition();
      if (index != Spinner.INVALID_POSITION) {
          action(actions[index]);
      }
   });
   ```


 - Method::references
 
   ```java
   int cmp = Objects.compare(word, other.word, String::compareToIgnoreCase);
   ```


 - Stream.API()
 
   ```java
   return Stream.of(lines)
        .map(str -> str.split("\t"))
        .filter(arr -> arr.length == 2)
        .map(arr -> new Word(arr[0], arr[1]))
        .collect(Collectors.toList());
   ```


 - switch for "string"
 
   ```java
   switch (action) {
       case "filter 1":
           // Filter one word
           stream = stream.filter(p -> p.getWord().split(" ").length == 1);
           break;
       case "filter 2+":
           // Filter two and more words
           stream = stream.filter(p -> p.getWord().split(" ").length >= 2);
           break;
       // ...
   }
   ```

- try(with-resources) {}

  ```java
  final List<String> lines = new ArrayList<>();
  try (final InputStream is = context.getAssets().open("words.txt");
       final InputStreamReader isr = new InputStreamReader(is, "UTF-8");
       final BufferedReader reader = new BufferedReader(isr)) {
      String line;
      while ( (line = reader.readLine()) != null ) {
          lines.add(line);
      }
  }
  ```

- Objects (from Java 7)

  ```java
  @Override
  public boolean equals(Object o) {
      // ...
      final Word other = (Word) o;
      return Objects.equals(translate, other.translate) &&
             Objects.equals(word, other.word);
  }

  @Override
  public int hashCode() {
      return Objects.hash(word, translate);
  }
  ```
  
- ~~try {~~ Exceptional ~~} catch~~ (functional try/catch)

  ```java
  return Exceptional.of(() -> {
      final InputStream is = context.getAssets().open("words.txt");
      // ... operations which throws Exception ...
      return lines;
  }).ifException(e -> Log.e("Java 8 Example", "Utils.readWords", e))
    .getOrElse(new ArrayList<>());
  ```
