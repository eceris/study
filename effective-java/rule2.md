# 규칙 2. 생성자 인자가 많을 때(4개 이상..)는 Builder 패턴 적용을 고려할 것

객체를 생성하는데 **선택인자가 필수인자보다 많은** 경우 빌더 패턴을 사용하자.

## 점층적 생성자 패턴

아래와 같은 `점층적 생성자 패턴`<sup>telescoping constructor pattern</sup>을 적용할 수 있다.

```java
public class NutritionFacts {
	private final int servingSize;	//필수
	private final int servings;		//필수
	private final int calories;		//선택
	private final int fat;			//선택
	private final int sodium;		//선택
	private final int carbohydrate;	//선택

	public NutritionFacts(int servingSize, int servings) {
		this(servingSize, servings, 0);
	}
	public NutritionFacts(int servingSize, int servings, int calories) {
		this(servingSize, servings, calories, 0);
	}
	public NutritionFacts(int servingSize, int servings, int calories, int fat) {
		this(servingSize, servings, calories, fat, 0);
	}
	public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium) {
		this(servingSize, servings, calories, fat, sodium, 0);
	}
	public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
		this.servingSize = servingSize;
		this.servings = servings;
		this.calories = calories;
		this.fat = fat;
		this.sodium = sodium;
		this.carbohydrate = carbohydrate;
	}
}
```

점층적 생성자 패턴은 **설정할 필요가 없는 인자**에도 값을 전달해야하는데, 위의 예에서 fat에 0을 전달한 것.

### 단점 
- 점층적 생성자 패턴은 유용하지만, 인자수가 늘어나면 복잡하고 작성하기가 어려움.
- 자료형이 같은 인자가 많아지면, 실수할 가능성이 높아짐.

## 자바빈 패턴
인자없는 생성자로 객체를 생성한 다음, 설정메소드<sup>setter</sup>를 통해 값을 채우는 방법을 적용할 수 있다.

```java
public class NutritionFacts {
    private final int servingSize = -1;	//필수 : 기본값 없음
	private final int servings = -1;	//필수 : 기본값 없음
	private final int calories = 0;		//선택
	private final int fat = 0;			//선택
	private final int sodium = 0;		//선택
	private final int carbohydrate = 0;	//선택

	public NutritionFacts() {}

	public void setServingSize(int val) { servingSize = val; }
	public void setServings(int val) { servings = val; }
	public void setCalories(int val) { calories = val; }
	public void setFat(int val) { fat = val; }
	public void setSodium(int val) { sodium = val; }
	public void setCarbohydrate(int val) { carbohydrate = val; }
} 
```
작성해야 하는 코드가 좀 늘어나긴 하지만, 객체를 생성하기도 쉽고 읽기도 편하다.

### 단점
- 1회의 호출로 객체의 생성을 끝낼수 없어 **객체의 일관성**<sup>consistency</sup> 이 깨질 수 있다.
- **변경불가능**<sup>immutable</sup>객체를 생성하지 못한다.

## 빌더 패턴
점층적 생성자 패턴의 안정성 + 자바빈 패턴의 가독성 = 빌더 패턴

```java
public class NutritionFacts {
	private final int servingSize;	//필수
	private final int servings;		//필수
	private final int calories;		//선택
	private final int fat;			//선택
	private final int sodium;		//선택
	private final int carbohydrate;	//선택

	public static class Builder {
		private final int ServingSize;
		private final int Servings;
		private final int calories;		//선택
		private final int fat;			//선택
		private final int sodium;		//선택
		private final int carbohydrate;	//선택

		public Builder(int servingSize, int servings) {
			this.servingSize = servingSize;
			this.servings = servings;
		}
		public Builder calories(int calories) {
			this.calories = calories;
			return this;
		}
		public Builder fat(int fat) {
			this.fat = fat;
			return this;
		}
		public Builder sodium(int sodium) {
			this.sodium = sodium;
			return this;
		}
		public Builder carbohydrate(int carbohydrate) {
			this.carbohydrate = carbohydrate;
			return this;
		}

		public NutritionFacts build() {
			return new NutritionFacts(this);
		}
	}
	private NutritionFacts(Builder builder) {
		servingSize = builder.servingSize;
		servings = builder.servings;
		calories = builder.calories;
		fat = builder.fat;
		sodium = builder.sodium;
		carbohydrate = builder.carbohydrate;
	}
}
```
NutritionFacts의 객체가 변경이 불가능하고, 모든 인자의 기본값이 한군데에 모여있다는 것에 유의할 것
사용은 아래와 같이 쉽다. 
```java
NutritionFacts cocaCola = new NutritionFacts.builder(240, 80).calories(100).sodium(35).carbohydrate(27).build();
```

### 장점 1 : 생성자와 마찬가지로 인자에 **불변식**을 적용할 수 있다.
**build()**메소드 안에서 **불변식이 위반**되었는지 검사할 수 있다. 불변식을 위반한 경우 build()메소드는 [IllegalArgumentException을 던져야한다](rule60.md)
불변식을 강제하는 또한가지 방법은, 불변식이 적용될 값 전부를 인자로 받는 setter를 지정하고 위반할 경우 IllegalArgumentException. 이렇게 하면 build()메소드가 호출되기 전에 불변식 검사를 할 수 있다.

### 장점 2 : 빌더 객체가 여러개의 varargs 인자를 받을 수 있다.
인자마다 별도의 setter를 사용하므로, 필요한 갯수만큼 늘릴수 있다.

### 장점 3 : 유연하다.
하나의 빌더로 여러 객체를 만들수 있고, 빌더객체의 어떤 값은 자동으로 채울수 있다.<sup>객체가 만들어질때마다 자동으로 증가되는 일련번호 같은</sup>

인자가 설정된 빌더는 훌륭한 `추상적 팩토리` 이다.
```java
public interface Builder<T> {
	T build();
}
```
위와 같은 인터페이스가 있다면, NutritionFacts.Builder 클래스는 Builder<NutritionFacts> 를 implement 하도록 선언할 수 있다.

보통 빌더객체를 인자로 받는 메소드는 `한정적 와일드카드 자료형`<sup>bounded wildcard type</sup>을 사용하여 [인자의 자료형을 제한한다.](rule28.md)
아래는 클라이언트가 전달한 Builder 객체를 사용하여 트리노드를 만든다.
```java
Tree buildTree(Builder<? extends Node> nodeBuilder) {...}
```
### 단점 1 : 객체를 생성하려면 우선 빌더 객체를 생성해야 해서 코드량이 조금 많아진다. 

> 빌더패턴은 인자가 많은 생성자나 static 팩토리가 필요한 클래스를 설계할 때, 특히 대부분의 인자가 선택적 인자인 경우에 유용하다.