# 규칙 48. 정확한 답이 필요하다면 float와 double은 피하라.

float와 double은 기본적으로 **과학** 또는 **엔지니어링** 관련 계산에 쓰일 목적으로 넓은 범위의 값에 대해 정확도가 높은 **근사치** 를 위해 설계된 연산이다.

### float와 double 은 특히 돈과 관련된 계산에는 적합하지 않다.
부동소수점 연산 <sup>floating-point arithmetic</sup>을 수행하기 때문에 정확한 값을 얻을 수 없다.

### 돈 계산을 할 때는 BigDecimal, int, long 을 사용한다는 원칙을 지켜야 한다. 
하지만, BigDecimal 을 사용할 때 문제가 있다. 
기본 산술연산 자료형<sup>primitive arithmetic type</sup> 보다 사용이 불편하며, 느리다.

### BigDecimal 의 대안은 int나 long 을 사용하는 것이다.

둘 중 어느 자료형을 쓸것이냐는 **수의 크기**, 그리고 **소수점 이하 몇자리까지 표현** 할 것이냐에 따라 결정된다.
아래와 같이 사용하는 것을 추천한다.
```java
public static void main(String[] args) {
	int itemsBought = 0;
	int funds = 100;
	for (int price = 10; funds >= price; price += 10) {
		funds -= price;
		itemsBought++;
	}
	System.out.println(itemsBought + "itemsBought.");
	System.out.println("Money left over : " + funds + " cents");
}
```

> **정확한 답**을 요구하는 문제를 풀 때는 **float** 나 **double**를 쓰지 말라. 소수점 이하 처리를 알아서 해줬으면 하고, 사용하기가 좀 불편해도 괜찮은데, 성능에도 그렇게 영향을 안받는 다면 **BigDecimal** 을 사용하라.

1. 성능이 중요하고 소수점 아래 수를 직접 관리해도 상관없으며 계산할 수가 심각하지 않을 땐, int 나 long 을 사용할 것.
2. 관계된 수치들이 십진수 9개 이하로 표현 가능할 경우에는 **int** 를 사용하라.
3. 관계된 수치들이 십진수 18개 이하로 표현 가능할 경우에는 **long** 를 사용하라. 
4. 그 이상일 경우에는 **BigDecimal** 을 사용하라. 

