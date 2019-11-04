
[여기 보고 정리 한 것.](https://dzone.com/articles/using-optional-correctly-is-not-optional)

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
- `Optional.orElse()`를 사용하는 것은 `isPresent() - get()`을 대체하는 아주 우아한 방법이다. 중요한 것은 `orElse()`에 담겨있는 파라미터가 Optional이 non-empty 인 경우에도 평가된 다는 것.
- 이게 중요한게 사용하지 않는데도 연산을 수행한다는 것인데 퍼포먼스 측면에서는 마이너스 요소다.
이런 문맥에서 보면 `orElse()`를 이미 선언된<sup>static final</sup> default object 만 사용해야하는 이유이다.

```java
// AVOID
public static final String USER_STATUS = "UNKNOWN";
...
public String findUserStatus(long id) {
    Optional<String> status = ... ; // prone to return an empty Optional
    if (status.isPresent()) {
        return status.get();
    } else {
        return USER_STATUS;
    }
}
```

```java
// PREFER
public static final String USER_STATUS = "UNKNOWN";
...
public String findUserStatus(long id) {
    Optional<String> status = ... ; // prone to return an empty Optional
    return status.orElse(USER_STATUS);
}
```

# 4. 만약 현재 아무 값도 존재하지 않는다면, 이미 Option.orElseGet() 를 통해 존재하지 않는 기본 객체를 set/return 하라.
- `Optional.orElseGet()`를 사용하는 것은 `isPresent() - get()`을 대체하는 아주 우아한 방법이다. 중요한 것은 `orElseGet()`의 파라미터가 `Supplier` 라는 것. 이게 의미하는건 실제로 `Optional` value가 존재하지 않을 때, `Supplier`가 실행된다는 것.
- 사실 3번의 `Optional.orElse()` 보다 성능 측면에서 이득이 있다. 

```java
// AVOID
public String computeStatus() {
    ... // some code used to compute status
}
public String findUserStatus(long id) {
    Optional<String> status = ... ; // prone to return an empty Optional
    if (status.isPresent()) {
        return status.get();
    } else {
        return computeStatus();
    }
}
```
```java
// AVOID
public String computeStatus() {
    ... // some code used to compute status
}
public String findUserStatus(long id) {
    Optional<String> status = ... ; // prone to return an empty Optional
    // computeStatus() is called even if "status" is not empty
    return status.orElse(computeStatus()); 
}
```

```java
// PREFER
public String computeStatus() {
    ... // some code used to compute status
}
public String findUserStatus(long id) {
    Optional<String> status = ... ; // prone to return an empty Optional
    // computeStatus() is called only if "status" is empty
    return status.orElseGet(this::computeStatus);
}
```

# 5. Java10 이후로, 만약 현재 아무값도 존재하지 않는다면, orElseThrow() 를 통해 `java.util.NoSuchElementException` 을 Throw 하라.
- Java 10 이후로 `orElseThrow()` 를 파라미터 없이 사용 가능한데, `NoSuchElementException`이 throw 된다.

```java
// PREFER
public String findUserStatus(long id) {
    Optional<String> status = ... ; // prone to return an empty Optional
    return status.orElseThrow();
}
```

# 6. 만약 현재 아무값도 존재하지 않느다면, 명시적인 Exception을 던지기 위해 `orElseThrow(Supplier<? extends X> exceptionSupplier)` 를 사용하라.
- `Optional.orElseThrow()`를 사용하는 것은 `isPresent() - get()`을 대체하는 아주 우아한 방법이다. 

```java
// AVOID
public String findUserStatus(long id) {
    Optional<String> status = ... ; // prone to return an empty Optional
    if (status.isPresent()) {
        return status.get();
    } else {
        throw new IllegalStateException(); 
    }
}
```

```java
// PREFER
public String findUserStatus(long id) {
    Optional<String> status = ... ; // prone to return an empty Optional
    return status.orElseThrow(IllegalStateException::new);
}
```

# 7. 만약 Null reference가 필요한 Optional 을 사용하려면 `orElse(null)` 을 사용하라.
- 특정한 상황에서 null ref 가 필요하다면 아래와 같이 사용할 것.

```java
// AVOID
Method myMethod = ... ;
...
// contains an instance of MyClass or empty if "myMethod" is static
Optional<MyClass> instanceMyClass = ... ;
...
if (instanceMyClass.isPresent()) {
    myMethod.invoke(instanceMyClass.get(), ...);  
} else {
    myMethod.invoke(null, ...);  
}
```

```java
// PREFER
Method myMethod = ... ;
...
// contains an instance of MyClass or empty if "myMethod" is static
Optional<MyClass> instanceMyClass = ... ;
...
myMethod.invoke(instanceMyClass.orElse(null), ...);
```

# 8. 만약 값이 존재한다면 Optional 을 consume 하라, 만약 값이 존재하지 않는다면 아무것도 하지말고, 이게 Optional.ifPresent()의 역할이다.

```java

// AVOID
Optional<String> status = ... ;
...
if (status.isPresent()) {
    System.out.println("Status: " + status.get());
}
```

```java
// PREFER
Optional<String> status ... ;
...
status.ifPresent(System.out::println);
```

# 9. 만약 값이 존재한다면 Optional 을 consume 하라, 만약 값이 존재하지 않는다면 `Empty-based action` 을 수행하라. 이것이 Optional.ifPresentElse() 의 역할이다.
- Java9 에서 `Optional.ifPresentOrElse()`는 `isPresent() - get()`을 대체하는 아주 우아한 방법이다.
- `ifPresent()`와 비슷한데 `else` 도 커버해준다.

```java
// AVOID
Optional<String> status = ... ;
if(status.isPresent()) {
    System.out.println("Status: " + status.get());
} else {
    System.out.println("Status not found");
}
```

```java
// PREFER
Optional<String> status = ... ;
status.ifPresentOrElse(
    System.out::println, 
    () -> System.out.println("Status not found")
);
```

# 10. 만약에 값이 존재하면, Optional 로 set/return 하라. 값이 없는 경우, other Optional 로 set/return 하라. 이것이 자바9의 Optional.or() 의 역할이다.
- java9에 추가됨.
- 가끔 non-empty `Optional`을 위해, Optional 로 set/return 을 하고 싶다. 그리고 Optional 이 비어있을때, 또 다른 Optional을 실행시키고 싶다. 
- `orElse()` 및 `ElseGet()` 메서드는 Optional로 래핑되지 않은 값을 반환하므로이 작업을 수행 할 수 없습니다.
- Java 9의 `Optional.or()` 메서드는 값을 Optional로 래핑하여 반환할 수 있다. 그렇지 않으면 supplier에 의해 생성된 Optional을 반환한다.

```java
// AVOID
public Optional<String> fetchStatus() {
    Optional<String> status = ... ;
    Optional<String> defaultStatus = Optional.of("PENDING");
    if (status.isPresent()) {
        return status;
    } else {
        return defaultStatus;
    }  
}
```

```java

// AVOID
public Optional<String> fetchStatus() {
    Optional<String> status = ... ;
    return status.orElseGet(() -> Optional.<String>of("PENDING"));
}
```

```java
// PREFER
public Optional<String> fetchStatus() {
    Optional<String> status = ... ;
    Optional<String> defaultStatus = Optional.of("PENDING");
    return status.or(() -> defaultStatus);
    // or, without defining "defaultStatus"
    return status.or(() -> Optional.of("PENDING"));
}
```

# 11. Optional.orElse / orElseXXX 는 람다식에서 isPresent()-get() 코드의 완벽한 대체제이다. 
- 스트림에서 `Optional`(e.g.,findFirst(), findAny(), reduce(),...) 객체를 반환하는 연산자들이 있다. 이것을 isPresent()-get() 연산자로 접근하는 것은 스트림의 체이닝을 깨뜨리고, if-else 문을 추가하여 보기에 좋지 않다. 이러한 경우에는 `orElse()` 혹은 `orElseGet()`을 사용하여 체이닝을 이어가자.


```java
// AVOID
List<Product> products = ... ;
Optional<Product> product = products.stream()
    .filter(p -> p.getPrice() < price)
    .findFirst();
if (product.isPresent()) {
    return product.get().getName();
} else {
    return "NOT FOUND";
}
```

```java
// AVOID
List<Product> products = ... ;
Optional<Product> product = products.stream()
    .filter(p -> p.getPrice() < price)
    .findFirst();
return product.map(Product::getName)
    .orElse("NOT FOUND");
```

```java
// PREFER
List<Product> products = ... ;
return products.stream()
    .filter(p -> p.getPrice() < price)
    .findFirst()
    .map(Product::getName)
    .orElse("NOT FOUND");

```

```java
// AVOID
Optional<Cart> cart = ... ;
Product product = ... ;
...
if(!cart.isPresent() || 
   !cart.get().getItems().contains(product)) {
    throw new NoSuchElementException();
}
```

```java

// PREFER
Optional<Cart> cart = ... ;
Product product = ... ;
...
cart.filter(c -> c.getItems().contains(product)).orElseThrow();

```



# 12. 하나의 값을 얻기 위해 과도하게 Optional 메소드를 체이닝 하는 것을 피해라.
- 단순한 값을 얻기 위해 Optional을 사용하지 마라.
```java
// AVOID
public String fetchStatus() {
    String status = ... ;
    return Optional.ofNullable(status).orElse("PENDING");
}

```

```java
// PREFER
public String fetchStatus() {
    String status = ... ;
    return status == null ? "PENDING" : status;
}
```

# 13. Optional 타입으로 필드 선언은 하지 말라.
- Optional 을 setter나 생성자의 인자로 사용하지 마라. Optional 은 필드에 사용하기 위한 것이 아니고, Serializable을 구현하지도 않는다.
- 또한, Optional 클래스는 Java Bean의 property로 사용하기 위한 것이 아니다.

```java
// AVOID
public class Customer {
    [access_modifier] [static] [final] Optional<String> zip;
    [access_modifier] [static] [final] Optional<String> zip = Optional.empty();
    ...
}
```
```java
// PREFER
public class Customer {
    [access_modifier] [static] [final] String zip;
    [access_modifier] [static] [final] String zip = "";
    ...
```


# 14. 생성자의 파라미터로 Optional 을 사용하지말라
- Optional을 필드 또는 메소드의 인자로 사용하지 마라.

```java
// AVOID
public class Customer {
    private final String name;               // cannot be null
    private final Optional<String> postcode; // optional field, thus may be null
    public Customer(String name, Optional<String> postcode) {
        this.name = Objects.requireNonNull(name, () -> "Name cannot be null");
        this.postcode = postcode;
    }
    public Optional<String> getPostcode() {
        return postcode;
    }
    ...
}
```

```java
// PREFER
public class Customer {
    private final String name;     // cannot be null
    private final String postcode; // optional field, thus may be null
    public Cart(String name, String postcode) {
        this.name = Objects.requireNonNull(name, () -> "Name cannot be null");
        this.postcode = postcode;
    }
    public Optional<String> getPostcode() {
        return Optional.ofNullable(postcode);
    }
    ...
}
```
- 근데 prefer 라고 해서 이 방법으로 모두 하는 것은 좋지 않음. 
- 대부분의 경우 getter는 컬렉션 또는 배열을 반환하며,이 경우에는 Optional 대신 빈 컬렉션 / 배열을 반환하는 것을 더 나음.





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

