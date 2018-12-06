
[여기 보고 정리 한 것.] (https://dzone.com/articles/using-optional-correctly-is-not-optional)

# 작성중

# 1. Optional 변수에 Null을 할당하지 마라

```java
// AVOID
public Optional<Cart> fetchCart() {
    Optional<Cart> emptyCart = null;
    ...
}
```

```java
// PREFER
public Optional<Cart> fetchCart() {
    Optional<Cart> emptyCart = Optional.empty();
    ...
}
```
Optional 객체를 초기화 하는데 null 보다는 Optional.empty() 를 더 선호하는데, Optional은 단순한 컨테이너 혹은 박스 이다.

# 2. Optional.get() 을 호출하기 전에 Optional 에 값이 존재하는지 확실히 해라.

```java
// AVOID
Optional<Cart> cart = ... ; // this is prone to be empty
...
// if "cart"is empty then this code will throw a java.util.NoSuchElementException
Cart myCart = cart.get();
```

```java
// PREFER
if (cart.isPresent()) {
    Cart myCart = cart.get();
    ... // do something with "myCart"
} else {
    ... // do something that doesn't call cart.get()
}
```


# 3. 만약 현재 아무 값도 존재하지 않는다면, 이미 Option.orElse() 를 통해 만들어진 기본 객체를 set/return 하라.

# 4. 만약 현재 아무 값도 존재하지 않는다면, 이미 Option.orElseGet() 를 통해 존재하지 않는 기본 객체를 set/return 하라.

# 5. 만약 현재 아무값도 존재하지 않는다면, orElseThrow() 를 통해 `java.util.NoSuchElementException` 을 Throw 하라.
# 6. 만약 현재 아무값도 존재하지 않느다면, 명시적인 Exception을 던지기 위해 `orElseThrow(Supplier<? extends X> exceptionSupplier) 를 사용하라.
# 7. 만약 Null reference가 필요한 Optional 을 사용하려면 `orElse(null) 을 사용하라.
# 8. 만약 값이 존재한다면 Optional 을 consume 하라, 만약 값이 존재하지 않는다면 아무것도 하지말고, 이게 Optional.ifPresent()의 역할이다.
# 9. 만약 값이 존재한다면 Optional 을 consume 하라, 만약 값이 존재하지 않는다면 `Empty-based action` 을 수행하라. 이것이 Optional.ifPresentElse() 의 역할이다.
# 10. 만약에 값이 존재하면, Optional 로 set/return 하라. 값이 없는 경우, other Optional 로 set/return 하라. 이것이 자바9의 Optional.or() 의 역할이다.
# 11. Optional.orElse / orElseXXX 는 람다식에서 isPresent()-get() 코드의 완벽한 대체제이다. 
# 12. 하나의 목적을 달성하기 위해 Optional 메소드를 체이닝 하는 것을 피해라.
# 13. Optional 타입으로 필드 선언은 하지 말라.
# 14. 생성자의 파라미터로 Optional 을 사용하지말라
# 15. setter의 인자로 Optional을 사용하지 말라.
# 16. 메소드의 인자로 Optional을 사용하지 말라.
# 17. empty collection 이나 array 를 반환할 때 Optional을 사용하지 말라.
# 18. collections에서 Optional을 사용하지 말라.
# 19. Optional.of() 와 Optional.ofNullable() 을 헷갈리지 말자.
# 20. Optional<T> 제네릭을 사용하지말고, non-generic 타입인, OptionalInt, OptionalLong, OptionalDouble 를 사용하자.
# 21. Equality를 asserting 하기 위해 Optional 을 unwrap 할 필요가 없다.
# 22. Optional이 제공하는 map(), flatMap() 을 사용하여 변환하자.
# 23. 미리 정의된 룰을 기반으로 하는 값을 사용하지말고, filter()를 사용하자.
# 24. Stream api 에 Optional api 를 함께 사용할 수 있다.
# 25. Optional에서 Identity-Sensitive 한 작업을 피하자.
# 26. 비어있는 Optional을 체크하여 반환할 때는, Optional.isEmpty() 를 사용하자.

