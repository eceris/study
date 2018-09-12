# 규칙 20. 태그 달린 클래스 대신 클래스 계층을 활용할 것

클래스를 만들때 어떤 기능을 제공하는지 표시(enum 따위로...) 하는 **태그**<sup>tag</sup>가 달린 클래스를 만들면 **안된다.**

예를 들면 아래와 같이 조잡하게 만들수 있다.

```java
//태그가 달린 클래스,,,, 이렇게 만들면 안된다!!!
public class Figure {
	enum Shape { RECTRANGLE, CIRCLE };

	//어떤 모양인지 나타내는 태그 필드
	final Shape shape;

	//태그가 RECTANGLE 일때만 사용하는 필드
	double length;
	double width;

	//태그가 CIRCLE 일때만 사용하는 필드
	double radius;

	// 원을 만드는 생성자
	Figure(double radius) {
		shape = Shape.CIRCLE;
		this.radius = radius;
	}

	// 사각형을 만드는 생성자
	Figure(double length, double width) {
		shape = Shape.RECTRANGLE;
		this.length = length;
		this.width = width;
	}

	double area() {
		switch(shape) {
			case RECTRANGLE : 
				return length * width;
			case CIRCLE : 
				return Math.PI * (radius * radius);
			default:
				throw new AssertionError();
		}
	}
}
```
태그를 포함하는 클래스를 만들면, 

1. enum선언, 태그필드, switch 문 같은 상투적인 코드가 들어감.
2. 서로 다른 기능들이 한 클래스에 모여 있어 **조잡함.**
3. 객체를 만들때마다 사용하지 않는 필드가 생성되므로 **heap에 메모리 요구량**이 늘어남.
4. 생성자에서는 관련 없는 필드를 초기화하지 않는 한, 필드들을 final로 선언 할수 없어 상투적인 <sup>boilerplate</sup> 코드가 늘어남.
5. 모든 로직에 **switch 문**이 들어가게 됨.

#### 태그기반 클래스는 클래스 계층을 얼기설기 흉내낸 것일 뿐.

너저분하고 오류 발생 가능성이 높고 효율적이지도 않다. 

#### 그럼 어떻게 해?

**하위 자료형 정의**<sup>subtyping</sup> 를 이용.

**추상 클래스**를 정의하고 태그값에 따라 달라지는 값을 추상메소드로 분리

```java
abstract class Figure {
	abstract double area();
}

class Circle extends Figure {
	final double radius;

	Circle(double radius) { this.radius = radius; }

	double area() { return Math.PI * (radius * radius); }
}

class Rectangle extends Figure {
	final double length;
	final double width;
	
	Rectangle(double length, double width) { 
		this.length = length;
		this.width = width;
	}

	double area() { return length * width; }
}
```

얼마나 단순하고 명료한가?

난잡하고 상투적인<sup>bolierplate</sup> 코드도 없다.

심지어 모든 필드가 **final** 이다!!!

#### 자료형 간의 자연스러운 계층 관계를 반영할 수 있다.

정사각형을 만들면....

```java
class Square extends Rectangle {
	Square(double side) {
		super(side, side);
	}
}

```

정사각형이 사각형이라는 클래스의 특별한 유형임을 알려준다.

> 태그 기반 클래스 사용은 피하라. 그리고 만약 만난다면, 꼭 **리팩토링** 해라!!!