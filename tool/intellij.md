# IntelliJ Tips & Tricks

## UI 관련 소소한

- nyan plugin
- use dark window headers

## Live Templates

- https://www.jetbrains.com/help/idea/2017.3/creating-code-constructs-by-live-templates.html
- psvm
- ifn
- inn
- todo, fix, const, ifn, inn, thr, sbc, psvm, ...
- 혹은 `⌘j`로 검색도 가능. (suggestion list)
- gwt, ase, asnn, ...

## [Search Everywhere](https://www.jetbrains.com/help/idea/searching-everywhere.html)

- 탭 기능이 추가됨. (Actions가 매번 밑에 있어서 불편했는데 좋다)
  - 그 중에서 Actions에 바로 접근하고 싶다면 `⌘^a`
- 자주 사용하지 않는 리팩토링 액션이나, 깃 명령어(annotate, git compare, revert, push, ...) 등에 활용.
- abbreviation 추가도 가능 (rv, an)

## Run Anything (Debug with `^`)

- gradle clean build
- application
- 전체 패키지 테스트 (기존에는 `⌥f1`, 좌측 화살표 키, `⌘r`)
- action으로 실행 중지도 가능

## [complete statement](https://www.jetbrains.com/help/idea/auto-completing-code.html#statements_completion)

- `⌘^⏎` (클래스, 메서드, 조건문, case문, ...)
- 그 외 smart completion, hippie completion

## [language injections](https://www.jetbrains.com/help/idea/using-language-injections.html)

- " + `⌥⏎` 
- json, html, sql 등의 문법 체크
- <u>`⌥⏎` 한 번 더 입력하면 edit <language ID> fragment</u>!
- language injection comment를 이용하면 highlighting도 가능.

## [acejump](https://github.com/acejump/AceJump#acejump) plugin

- vim 모드에서 부러운 것 중에 하나는 특정 단어로 바로 가기
- `⌃;` 단어 찾아가기
- `⌃⇧;` 라인 찾아가기

## [inserting a live template](https://www.jetbrains.com/help/idea/2017.3/creating-code-constructs-by-live-templates.html)

- todo, fix, const, ifn, inn, thr, sbc, psvm, ...
- 혹은 `⌘j`로 검색도 가능. (suggestion list)
- gwt, ase, asnn, ...

## [analyze data flow](https://www.jetbrains.com/help/idea/analyzing-data-flow.html)
- 특정 필드 커서 위치 후 data flow 액션 찾기
- `⌥F7`과 비교하면 장점이 확연히 드러남

## debug

- `⌥F8`: evaluate code fragment
- [Analyze Java Stream operations](https://www.jetbrains.com/help/idea/analyze-java-stream-operations.html)
- [디버그 도중 코드를 고치지 않고 예외 던지기가 가능](https://www.jetbrains.com/idea/whatsnew/#v2018-1-jvm-debugger)

## 유용한 단축키: https://www.jetbrains.com/help/idea/mastering-keyboard-shortcuts.html

- `Ctrl` *2
- `⇧` *2
- `⌘O`, `⇧⌘O`, `⌥⌘O`, `⇧⌘A`
- `⌘E`
- `⌥⏎`
- `⌃Space`, `⌃⏎Space`, `⇧⌘⏎`
- `⌥↑`, `⌥↓`

## Http Client는 여전히 좋음. [이것도 덤으로](https://www.jetbrains.com/idea/whatsnew/#v2018-1-spring-boot)
## 탭 없애기(검색과 최근 파일 뷰 사용)

## 기타

- `⌘7`
- `⌥F1`
- `⌘⇧8`
- `^↑`, `^↓`
- Refactor
  - `⌥⌘N`
  - `⌥⌘V`
  - `⌥⌘C`
  - `⌥⌘M`
  - `⇧F6`
  - `F6`
  - 모르겠으면 `^T`
