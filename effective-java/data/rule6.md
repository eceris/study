# 규칙 6. 유효기간이 지난 객체 참조는 폐기하라.
C, C++ 에 비해 개발자가 메모리 관리를 하지 않는 Java 개발자가 메모리 관리에 대한 사실을 망각하면 안된다.
메모리 누수로 인해, 가비지 컬렉터가 해야하는 일이 많아져 성능이 느려지거나, 메모리의 요구량이 늘어간다. 극단적으로 디스크 페이징(swap memory)이 발생하고, 마지막에는 OOM<sup>out of memory</sup> 가 발생하기도 한다. 

쓸일이 없는 객체 참조는 무조건 null로 만들어야 한다. 아래의 경우 stack에서 pop 된 객체에 대한 참조는 그 즉시 null로 만들어 gc의 대상이 되도록 하자.
```java
public Object pop() {
	if (size == 0) 
		throw new EmptyStackException();
	Object result = elements[--size];
	elements[size] = null; // obsolete ref(쓸모없는 참조) 제거
	return results;
}

```

위와 같은 경우를 한번 겪고 나서 참조를 null로 처리해야한다는 강박관념을 갖지마라. 소스코드만 난잡해진다.
객체의 참조를 null 로 하는 것은 규범이라기 보다는 예외적인 조치어야 한다.

예방하는 가장 좋은 방법은 변수의 유효범위(scope)를 [최대한 좁게 만들면 자연스레 해결된다.](rule45.md)

## 캐시
캐시도 메모리 누수가 흔히 발생하는 장소인데, WeakHashMap 으로 구현하여 GC에 대상이 되도록 하자.

## Callback
클라이언트가 콜백을 명시적으로 제거하지 않는 이상 메모리에 점유된 상태로 존재한다. 
WeakHashMap을 키로 저장하여 자연스레 GC의 대상이 되도록 할 것.

> 보통 메모리 누수는 뚜렷한 오류가 아니므로 찾기 어려운데, 주의깊게 코드를 보거나 `Heap profiler` 같은 도구를 통해 검증하다가 발견한다.

