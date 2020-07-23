
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
- Optional 은 자바Bean 혹은 영속화를 위한 property로 사용하기 위한 것이 아님. 심지어 Serializable 도 아님. Optional 을 settter를 통해 구현하는 것은 안티패턴임. 그럼에도 불구하고 도메인모델의 엔티티로 Optional을 사용하는 것은 가능하긴 하다.

```java
// AVOID
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    ...
    @Column(name="customer_zip")
    private Optional<String> postcode; // optional field, thus may be null
     public Optional<String> getPostcode() {
       return postcode;
     }
     public void setPostcode(Optional<String> postcode) {
       this.postcode = postcode;
     }
     ...
}
```

```java
// PREFER
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    ...
    @Column(name="customer_zip")
    private String postcode; // optional field, thus may be null
    public Optional<String> getPostcode() {
      return Optional.ofNullable(postcode);
    }
    public void setPostcode(String postcode) {
       this.postcode = postcode;
    }
    ...
}
```

# 16. 메소드의 인자로 Optional을 사용하지 말라.
- Optionals 를 생성하기위해 call sites를 강제하지 마세요. 그리고 Optional을 setters의 필드나 생성자의 인자로 사용하지 마세요.
Optional을 메소드의 인자로 사용하는것은 자주발생하는 실수중 하나. 필요치 않게 코드를 복잡하게함. 

- Optional 들을 생성하기 위해 강제 콜 사이트 하는것 대신에 파라미터 선택에 대한 책임을 져야 함. 이런것들이 코드를 어지럽히고 의존성을 유발함. 그리고 시간이 지나면 어디서나 사용되고 있음. 
- 기억할 것은 Optional은 또 다른 객체(혹은 컨테이너) 이며, 비싼놈임.(bare ref 메모리의 4배를 소비함.) 그럼에도 경우에 따라 이것을 체크해서 상황에 따라 잘 사용하길 바람.

```java
// AVOID
public void renderCustomer(Cart cart, Optional<Renderer> renderer,
                           Optional<String> name) {     
    if (cart == null) {
        throw new IllegalArgumentException("Cart cannot be null");
    }
    Renderer customerRenderer = renderer.orElseThrow(
        () -> new IllegalArgumentException("Renderer cannot be null")
    );    
    String customerName = name.orElseGet(() -> "anonymous"); 
    ...
}
// call the method - don't do this
renderCustomer(cart, Optional.<Renderer>of(CoolRenderer::new), Optional.empty());
```
```java
// PREFER
public void renderCustomer(Cart cart, Renderer renderer, String name) {
    if (cart == null) {
        throw new IllegalArgumentException("Cart cannot be null");
    }
    if (renderer == null) {
        throw new IllegalArgumentException("Renderer cannot be null");
    }
    String customerName = Objects.requireNonNullElseGet(name, () -> "anonymous");
    ...
}
// call this method
renderCustomer(cart, new CoolRenderer(), null);
```

```java

// PREFER
public void renderCustomer(Cart cart, Renderer renderer, String name) {
    Objects.requireNonNull(cart, "Cart cannot be null");        
    Objects.requireNonNull(renderer, "Renderer cannot be null");        
    String customerName = Objects.requireNonNullElseGet(name, () -> "anonymous");
    ...
}
// call this method
renderCustomer(cart, new CoolRenderer(), null);

```

```java
// PREFER
// write your own helper
public final class MyObjects {
    private MyObjects() {
        throw new AssertionError("Cannot create instances for you!");
    }
    public static <T, X extends Throwable> T requireNotNullOrElseThrow(T obj, 
        Supplier<? extends X> exceptionSupplier) throws X {       
        if (obj != null) {
            return obj;
        } else { 
            throw exceptionSupplier.get();
        }
    }
}
public void renderCustomer(Cart cart, Renderer renderer, String name) {
    MyObjects.requireNotNullOrElseThrow(cart, 
                () -> new IllegalArgumentException("Cart cannot be null"));
    MyObjects.requireNotNullOrElseThrow(renderer, 
                () -> new IllegalArgumentException("Renderer cannot be null"));    
    String customerName = Objects.requireNonNullElseGet(name, () -> "anonymous");
    ...
}
// call this method
renderCustomer(cart, new CoolRenderer(), null);
```


# 17. empty collection 이나 array 를 반환할 때 Optional을 사용하지 말라.
- 코드를 깨끗하고 가볍게 유지하려면 null이나 빈 collections/arrays를 위해 Optional을 리턴하지마세요.
- 그냥 빈 collections/arrays 를 반환하는 것이 좋아요.

```java
// AVOID
public Optional<List<String>> fetchCartItems(long id) {
    Cart cart = ... ;    
    List<String> items = cart.getItems(); // this may return null
    return Optional.ofNullable(items);
}
```

```java
// PREFER
public List<String> fetchCartItems(long id) {
    Cart cart = ... ;    
    List<String> items = cart.getItems(); // this may return null
    return items == null ? Collections.emptyList() : items;
}

```

# 18. collections에서 Optional을 사용하지 말라.
- 컬렉션에서 Optional을 사용하는 건 악취를 풍기기도 한다. Optional Map을 만든다고 생각해보자. 그것은 Optional 객체를 담고 있지 않고 보통, null 혹은 이상한 값을 담고 있다. 또한 Optional은 공짜가 아니다. 메모리를 더 소비할 것이다.
```java

// AVOID
Map<String, Optional<String>> items = new HashMap<>();
items.put("I1", Optional.ofNullable(...));
items.put("I2", Optional.ofNullable(...));
...
Optional<String> item = items.get("I1");
if (item == null) {
    System.out.println("This key cannot be found");
} else {
    String unwrappedItem = item.orElse("NOT FOUND");
    System.out.println("Key found, Item: " + unwrappedItem);
}
```


```java

//PREFER
Map<String, String> items = new HashMap<>();
items.put("I1", "Shoes");
items.put("I2", null);
...
// get an item
String item = get(items, "I1");  // Shoes
String item = get(items, "I2");  // null
String item = get(items, "I3");  // NOT FOUND
private static String get(Map<String, String> map, String key) {
  return map.getOrDefault(key, "NOT FOUND");
}
```

```java
//WORSE
Map<Optional<String>, String> items = new HashMap<>();
Map<Optional<String>, Optional<String>> items = new HashMap<>();
```

# 19. Optional.of() 와 Optional.ofNullable() 을 헷갈리지 말자.
- 키포인트는 `Optional.of(null)`은 NPE 를 반환할 수 있고, `Optional.ofNullalbe(null)` 은 `Optional.empty`를 반환한다.

# 20. Optional<T> 제네릭을 사용하지말고, non-generic 타입인, OptionalInt, OptionalLong, OptionalDouble 를 사용하자.
- 특별한 경우 boxed primitives타입이 필요하더라도,  Optional<  T  > 는 피하고 generic 이 아닌 `OptionalInt`, `OptionalLong`, 또는 `OptionalDouble`을 사용하자.
- boxing과 unboxing 은 성능저하의 우려가 있는 비싼 작업이다. 위의 것들은 primitive 타입들의 래퍼 클래스이다.

```java
// AVOID
Optional<Integer> price = Optional.of(50);
Optional<Long> price = Optional.of(50L);
Optional<Double> price = Optional.of(50.43d);
```

```java
// PREFER
OptionalInt price = OptionalInt.of(50);           // unwrap via getAsInt()
OptionalLong price = OptionalLong.of(50L);        // unwrap via getAsLong()
OptionalDouble price = OptionalDouble.of(50.43d); // unwrap via getAsDouble()
```

# 21. Equality를 단언하기 위해 Optional 을 unwrap 할 필요가 없다.
- 두 개의 Optional을 비교하기 위해 Optional을 까볼 필요가 없다. 왜냐면 `equals` 함수가 래핑되어있는 값을 비교하기 때문이다. 
```java
@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (!(obj instanceof Optional)) {
        return false;
    }
    Optional<?> other = (Optional<?>) obj;
    return Objects.equals(value, other.value);
}
```
- 이미 이렇게 구현되어있다.

```java
// AVOID
Optional<String> actualItem = Optional.of("Shoes");
Optional<String> expectedItem = Optional.of("Shoes");        
assertEquals(expectedItem.get(), actualItem.get());

```

```java
// PREFER
Optional<String> actualItem = Optional.of("Shoes");
Optional<String> expectedItem = Optional.of("Shoes");        
assertEquals(expectedItem, actualItem);
```

# 22. Optional이 제공하는 map(), flatMap() 을 사용하여 변환하자.
- 두개의 함수는 convinient 함수다.
- `map()` 함수는 `Optional` 로 래핑된 값을, `flatMap()`은 value를 리턴한다. 

```java
// AVOID
Optional<String> lowername ...; // may be empty
// transform name to upper case
Optional<String> uppername;
if (lowername.isPresent()) {
    uppername = Optional.of(lowername.get().toUpperCase());
} else {
    uppername = Optional.empty();
}
```

```java
// PREFER
Optional<String> lowername ...; // may be empty
// transform name to upper case
Optional<String> uppername = lowername.map(String::toUpperCase);
```
```java

// AVOID
List<Product> products = ... ;
Optional<Product> product = products.stream()
    .filter(p -> p.getPrice() < 50)
    .findFirst();
String name;
if (product.isPresent()) {
    name = product.get().getName().toUpperCase();
} else {
    name = "NOT FOUND";
}
// getName() return a non-null String
public String getName() {
    return name;
}
```


```java
// PREFER
List<Product> products = ... ;
String name = products.stream()
    .filter(p -> p.getPrice() < 50)
    .findFirst()
    .map(Product::getName)
    .map(String::toUpperCase)
    .orElse("NOT FOUND");
// getName() return a String
public String getName() {
    return name;
}
```
```java

// AVOID
List<Product> products = ... ;
Optional<Product> product = products.stream()
    .filter(p -> p.getPrice() < 50)
    .findFirst();
String name = null;
if (product.isPresent()) {
    name = product.get().getName().orElse("NOT FOUND").toUpperCase();
}
// getName() return an Optional
public Optional<String> getName() {
    return Optional.ofNullable(name);
}
```

```java
// PREFER
List<Product> products = ... ;
String name = products.stream()
    .filter(p -> p.getPrice() < 50)
    .findFirst()
    .flatMap(Product::getName)
    .map(String::toUpperCase)
    .orElse("NOT FOUND");
// getName() return an Optional
public Optional<String> getName() {
    return Optional.ofNullable(name);
}
```

# 23. 미리 정의된 룰을 기반으로 하는 값을 사용하지말고, filter()를 사용하자.
- `filter()` 함수를 이용하여 래핑된 value를 처리하자.
```java
// AVOID
public boolean validatePasswordLength(User userId) {
    Optional<String> password = ...; // User password
    if (password.isPresent()) {
        return password.get().length() > 5;
    }
    return false;
}
```


```java
// PREFER
public boolean validatePasswordLength(User userId) {
    Optional<String> password = ...; // User password
    return password.filter((p) -> p.length() > 5).isPresent();
}
```

# 24. Optional api를 Stream Api 와 체이닝해서 쓸 필요가 있을까?
- 그러면 Optional.stream()을 사용하자.
- 자바9 부터 는 `Optional.stream()`를 이용하여 Optional 인스턴스를 Stream처럼 사용할 수 있다.
- Optional API를 Stream API 와 체이닝 할때 유용한데, 하나의 element를 가진 Stream 혹은 empty Stream(만약 Optional이 not present라면)를 생성한다.
```java
// AVOID
public List<Product> getProductList(List<String> productId) {
    return productId.stream()
        .map(this::fetchProductById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());
}
public Optional<Product> fetchProductById(String id) {
    return Optional.ofNullable(...);
}
```

```java
// PREFER
public List<Product> getProductList(List<String> productId) {
    return productId.stream()
        .map(this::fetchProductById)
        .flatMap(Optional::stream)
        .collect(toList());
}
public Optional<Product> fetchProductById(String id) {
    return Optional.ofNullable(...);
}
```


# 25. Optional에서 Identity-Sensitive 한 작업을 피하자.
- 레퍼런스 equality, hash-based 또는 synchronization 은 피하자.
- `LocalDateTime` 클래스와 같이 `Optional` 클래스는 value-based 클래스이다.
```java

// AVOID
Product product = new Product();
Optional<Product> op1 = Optional.of(product);
Optional<Product> op2 = Optional.of(product);
// op1 == op2 => false, expected true
if (op1 == op2) { ...
```
```java
// PREFER
Product product = new Product();
Optional<Product> op1 = Optional.of(product);
Optional<Product> op2 = Optional.of(product);
// op1.equals(op2) => true,expected true
if (op1.equals(op2)) { ...
```

```java
// NEVER DO
Optional<Product> product = Optional.of(new Product());
synchronized(product) {
    ...
}
```

# 26. 비어있는 Optional을 체크하여 boolean 을 반환할 때는, java11의 Optional.isEmpty() 를 사용하자.
- 자바11 부터 `Optional`객체가 비어있는지 쉽게 체크하는 `isEmpty()`함수가 추가되었다.
```java
// AVOID (Java 11+)
public Optional<String> fetchCartItems(long id) {
    Cart cart = ... ; // this may be null
    ...    
    return Optional.ofNullable(cart);
}
public boolean cartIsEmpty(long id) {
    Optional<String> cart = fetchCartItems(id);
    return !cart.isPresent();
}
```

```java
// PREFER (Java 11+)
public Optional<String> fetchCartItems(long id) {
    Cart cart = ... ; // this may be null
    ...    
    return Optional.ofNullable(cart);
}
public boolean cartIsEmpty(long id) {
    Optional<String> cart = fetchCartItems(id);
    return cart.isEmpty();
}
```
