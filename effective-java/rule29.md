# 규칙 29. 형 안전 다형성 컨테이너를 쓰면 어떨지 따져보라
제네릭은 Set 이나 Map 같은 컬렉션과 ThreadLocal, AtomicReference 처럼 하나의 원소만 담는 컨테이너 처럼 사용
형 인자를 받는 것은 컨테이너 역할을 하는 부분, 컨테이너 별로 형인자의 수는 고정

컨테이너 대신 키에 형인자를 지정하는 것이 기본 아이디어, 컨테이너 에 값을 넣거나 뻴 때마다 키를 제공.

예제를 보자 
```java
// 형 안전 다형성(heterogenenous) 컨테이너 패턴 - API
public class Favorites {
	public <T> void putFavorites(Class<T> type, T instance);
	public <T> T getFavorites(Class<T> type);
}
```
위의 클래스를 사용해보자
```java
//형 안전 다형성(heterogeneous) 컨테이너 패턴 - 클라이언트
public static void main(String[] args) {
	Favorites f = new Favorites();
	f.putFavorites(String.class, "java");
	f.putFavorites(Integer.class, 0xcafebabe);
	f.putFavorites(Class.class, Favorites.class);
	String favoriteString = f.getFavorites(String.class);
	int favoriteInteger = f.getFavorites(Integer.class);
	Class<?> favoriteClass = f.getFavorites(Favorites.class);

	System.out.printf("%s %d %s%n", favoriteString, favoriteInteger, favoriteClass.getName());
}
```
실행해보면 java cafebabe Favorites 가 출력. 
Favorites 객체는 형 안정성<sup>typesafe heterogenenous container</sup>을 보장.
Favorites 클래스의 구현을 보자
```java
public class Favorites {
	private Map<Class<?>, Object> favorites = new HashMap<>();

	public <T> void putFavorites(Class<T> type, T instance) {
		if (type == null ) {
			throw new NullPointerException("Type is null");
		}
		favorites.put(type, instance);
	}

	public <T> T getFavorites(Class<T> type) {
		return type.cast(favorites.get(type));
	}
}
```
위와 같이 동적 형변환을 통해 T 형으로 무점검 형변환<sup>unchecked cast</sup>하는 코드가 없는, 형 안전성이 보장된 Favorites 클래스를 만들수 있다.

> 컬렉션 API를 통해서도 확인할 수 있듯이 제네릭의 일반적인 용법대로 컨테이너 별로 형인자 갯수는 정해져 있는데, 컨테이너 대신 키를 제네릭으로 만들면 제약이 없는 형안전 다형성 컨테이너를 만들 수 있다. 보통 Class 객체를 키로 쓰는데, 그런 Class 객체를 자료형 토큰<sup>type token</sup>이라고 부른다.

