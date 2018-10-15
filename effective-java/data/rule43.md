# 규칙 43. null 대신 빈 배열이나 컬렉션을 반환하라.

빈 배열 이나 컬렉션을 반환하지 않고 null을 반환할 경우 클라이언트에 아래와 같은 null 체크 로직이 매번 들어가야한다.
```java
Cheese[] cheeses = shop.getCheeses();
if(cheeses != null && Arrays.asList(cheeses).contains(Cheese.STILTON)) {
	System.out.println("Jolly good, just the thing");
}

//만약 null을 반환하지 않는 다면 간단하게 ...
if(Arrays.asList(cheeses).contains(Cheese.STILTON)) {
	System.out.println("Jolly good, just the thing");
}
```

문제는 클라이언트가 null처리를 잊어버릴 경우, 이 문제는 오래도록 잠복한다.

#### 배열의 할당 비용을 피할수 있으니 더 좋은 성능을 위해 차라리 null을 반환하는게 맞는것 아니냐??
두가지 측면에서 이 주장은 틀렸다.
1. 프로파일링의 결과로 해당 메서드가 성능 저하의 주범으로 밝혀지지 않는 이상은 그 수준까지 성능 걱정을 하는 것은 바람직 하지 않다.
2. 길이가 0 인 배열은 [변경이 불가능<sup>immutable</sup> 하므로 아무 재약 없이 재사용할 수 있다.](data/rule15.md)


```java
public List<Cheese> getCheeseList() {
	if (cheeseInStock.isEmpty()) {
		return Collections.emptyList();
	} else {
		return new ArrayList<Cheese>(cheeseInStock);
	}
}
```

> null 대신에 빈 배열이나 빈 컬렉션을 반환하자. null을 반환하는 것은 C 언어로 부터 전해진 관습인데, C언어 에서는 길이가 0인 배열을 반환해도 이득이 없기 때문에 이런 방식을 사용했던 것이다.
