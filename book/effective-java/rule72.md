# 규칙 72. 스레드 스케쥴러에 의존하지 마라
실행할 스레드가 많을때, 어떤 스레드를 얼마나 오랫동안 사용할 지 결정하는 것은 스레드 스케쥴러. 제대로 된 운영체제라면 공평한 결정을 내리지만, 그 정책은 바뀔수 있음.

## 좋은 프로그램이라면 스케줄링 정책에는 의존 하지 말 것.
- 정확성을 보장하거나 성능을 높이기 위해 스레드 스케줄러에 의존하는 프로그램은 이식성이 떨어짐.<sup>non-portable</sup>
- 좋은 방법은 실행 가능 스레드의 평균적 수가 프로세서 수보다 너무 많이지지 않도록 하는 것.

## 스레드는 필요한 일을 하고 있지 않을 때는 실행 중이어서는 안됨.
- 실행 가능<sup>runnable</sup>한 스레드와 스레드의 총 갯수는 다름.
- [실행자 프레임워크 관점에서](rule68.md) 스레드 풀의 크기는 **적절히** 정하고 태스크의 크기는 **적당히 작게**, **독립적으로** 
- 아래처럼 바쁘게 대기해서는 안된다
```java
public class SlowCountDownLatch {
    ...
    
    public void await() {
        while (true) {
            synchronized(this) {
                if (count == 0) return;
            }
        }
    }
    ...
}
```

## 다른 스레드에 비해 충분 CPU 시간을 받지 못하는 스레드가 있더라도, Thread.yield 를 호출해서 문제를 해결하려고 하지마라.
- 임시방편은 될 수 있음. 하지만 이식성이 없음.
- 비슷한 기법으로 스레드의 우선순위를 조정할 수 있는데 이것을 합리적이지 않음.

> 프로그램의 정확성을 스레드 스케줄러에 의존하지 말자. **안정적이지도 않고**, **이식성이 보장되지도 않음.**