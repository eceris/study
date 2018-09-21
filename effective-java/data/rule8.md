# 규칙 8. equals() 를 재정의할 때는 일반 규약을 따르라.
equals() 메소드는 되도록이면 재정의 하지 않는 것이 좋다. 만약 현재 상황이 아래와 같다면 재정의 하지 마라.
1. 각각의 객체가 고유하다.
2. 클래스에 `논리적 동일성`<sup>Logical equality</sup> 검사 방법이 있건 없건 상관없다.
3. 상위 클래스에서 재정의한 equals()가 하위 클래스에서도 사용하기 적당하다. 
4. 클래스가 private 혹은 package-protected로 선언되었고, equals() 메소드를 호출 할 일이 없다.

4의 경우에는 논란의 여지가 있는데, 필자는 아래와 같이 사용하지 못하도록 한다.
```java
@Override
public Boolean equals(Object o) {
	throw new AssertionError(); // 호출 되면 안되는 메서드를 호출한 경우.
}
```

## 실제로 그럼 Object.equals()를 재정의 하는 때는 언제인가?

- 객체의 동일성이 아닌 논리적 동일성의 개념을 지원하는 클래스일 경우
- 상위 클래스의 equals()가 하위 클래스의 필요를 충족시켜주지 못할 경우


equals() 메소드는 동치관계<sup>equivalence relation</sup>를 구현하는데, 아래와 같은 관계를 동치관계라 한다.

- 반사성 : null 이 아닌 참조 x 가 있을 때, x.equals(x) == true 를 반환.
- 대칭성 : null 이 아닌 참조 x 와 y 가 있을 때, x.equals(y) 는 y.equals(x) 가 true 일때만 true를 반환.
- 추이성 : null 이 아닌 참조 x, y, z 가 있을 때, x.equals(y) == true 이고 y.equals(z) == true 이면 z.equals(x) == true 를 반환.
	\- 보통 부모와 자식 객체간의 비교 시에는 무조건 equals 규약을 어길수 밖에 없는 구조인데, [계승 하는 대신 구성](rule16.md)<sup>composition</sup> 하면 자연스레 해결.
- 일관성 : null 이 아닌 참조 x와 y 가 있을 때,  x.equals(x) 의 결과는 호출 횟수에 상관없이 항상 같아야 한다. 
- null 이 아닌 참조 x 에 대해, x.equals(null) 은 항상 false 이다.



## 훌륭한 equals() 메소드를 만들기 위한 지침.
1. == 연산자를 이용하여 equals() 의 인자가 자기 자신인지 검사하라.(객체비교 오버헤드가 클 경우 위력을 발휘.)
2. instanceof 연산자를 사용하여 인자의 자료형이 정확한지 검사하라.
3. equals 의 인자를 정확한 자료형으로 변환하라.
4. `중요` 필드 각각이 인자로 주어진 객체의 해당 필드와 일치하는지 검사한다. 만약 null을 허용할 경우 아래와 같이 하자.

```java
(field == null ? o.field == null : field.equals(o.field))

(field == o.field || (field != null : field.equals(o.field)) //field와 o.field 가 같을 때가 많다면, 이렇게 하는게 더 성능상 좋다.
```

5. equals() 메소드 구현을 끝냈다면, 동치관계를 단위테스트<sup>unit test</sup> 로 검사하라.
6. equals() 메소드를 구현 할 때, [hashCode() 도 재정의 하라.](rule9.md)
7. equals() 메소드의 인자 형을 Object에서 다른 것으로 바꾸지 마라.
