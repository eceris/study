# 규칙 60. 표준 예외를 사용하라
코드를 재사용하면 좋다는 일반 규칙은 **예외** 에도 어김없이 적용. 자바 플랫폼 라이브러리에는 대부분의 API가 필요로 하는 **기본적인 무점검 예외** 들이 갖추어져 있다.

### 표준 예외의 장점 1
배우기 쉽고 사용하기 편리한 API를 만들수 있다. 다른 개발자들도 친숙한, 널리 퍼진 관습을 따르기 때문

### 표준 예외의 장점 2
표준 예외로 개발된 API는 가독성이 높다.

### 표준 예외의 장점 3
예외 클래스 갯수를 줄이면 프로그램의 메모리 요구량이 줄고, 클래스 로딩 시간도 줄어든다.

### 가장 널리 사용되는 예외 IllegalArgumentException
잘못된 값을 인자로 전달했을 때 일반적으로 발생하는 예외.

### 널리 쓰이는 또다른 예외는 IllegalStateException
현재 객체 상태로는 호출할 수 없는 메서드를 호출했을 때 일반적으로 발생하는 예외.

### 일반적 관습
1. null 인자를 받으면 안되는 메서드에 null을 받았을 경우, IllegalArgumentException 보다는 NullPointerException이 발생해야 함.
2. 어떤 순서열의 첨자를 나타내는 인자에 참조 가능 범위를 벗어날 경우 IndexOutOfBoundsException이 발생해야 한다.
3. 호출된 메서드를 지원하지 않을 경우 UnsupportedOperationException 이 발생해야 함.

가장 널리 재사용되는 예외를 아래 표에 요약하였다.
| 예외 | 용례 |
|:--------|:--------|
| IllegalArgumentException | null이 아닌 인자의 값이 잘못되었을 때 |
| IllegalStatementException | 객체 상태가 메서드 호출을 처리하기에 적절치 않을 때 |
| NullPointerException | null 값을 받으면 안 되는 인자에 null이 전달되었을 때 |
| IndexOutOfBoundsException | 인자로 주어진 첨자가 허용 범위를 벗어났을 때 |
| ConcurrentModificationException | 병렬적 사용이 금지된 객체에 대한 병렬 접근이 탐지 되었을 때 |
| UnsupportedOperationException | 객체가 해당 메소드를 지원하지 않을 때 |

상황에 따라 다른 예외도 사용할 수 있는데,
복소수나 실수를 나타내는 수학적 연산 객체를 구현하고 있다면, `ArithmeticException`이나 `NumberFormatException` 도 재사용할 수 있다.

### 어떤 예외를 재사용하면 좋을지 결정하는 것은 엄밀한 과학적 절차를 따르지 않는다.
- 위의 표에 나열한 예외들도 `상호배제적`이지 않다.
- 명확한 규칙은 없으니 상황에 잘 맞는 예외를 잘 사용하자.
- [예외가 발생한 상황을 좀더 상세히 표현하기 위해 기존 예외에 하위 클래스를 자유롭게 만들어서 사용해도 좋다.](rule63.md)
