# 개발 영역에 따른 TDD 작성 패턴

## 일반적인 애플리케이션

### 생성자 테스트
- 보통은 생성자는 굳이 테스트 케이스를 작성하지 않음. 그러나 생성자에서 상태를 갖는다면 isXXX() 테스트를 하기도 함.
- 일반적으로 생성자의 파라미터로 지정된 값 외에 항목을 이용하는 코드를 생성자 메소드 안에 넣지 않음. 숨 겨진 로직의 복잡도로 인해..
```java
public class EmployeeDaoTest {
	@Test
	public EmployeeDaoTest {
		EmployeeDao dao = new EmployeeDao();
		assertTrue(dao.isConnected());
	}
}
```

### DTO 스타일의 객체 테스트
- 클래스가 속성필드로만 이루어젼 단순 DTO 인 경우 굳이 테스트케이스를 작성하지 않음.

### 닭과 달걀 메소드 테스트
- 메소드가 서로 맞물려 있어서, 완전히 하나만 독립적으로 테스트하기 어려운 경우.
- 하나의 기능만 테스트는 불가. 구현되지 않은 기능이 물려 있을 경우, 리플렉션을 이용한 테스트는 하지 않을 것.

### 배열 테스트
- 순서도 고려하므로 만약 순서가 영향을 미치지 않는 데이터라면, Arrays.sort()메소드를 이용할 것


### 객체 동치성 테스트
- 같은 값을 갖는 객체인지만 판별하면 되는 테스트인 경우
```java
@Test
public void testEquals_case1() {
	Music musicA = new Music("Better in time", "Leona Lewis");
	Music musicB = new Music("Better in time", "Leona Lewis");
	assertEquals ( musicA.getPerformerName(), musicB.getPerformerName());
	assertEquals ( musicA.getSongName(), musicB.getSongName() );
}
```

### 컬렉션 테스트

#### 자바 기본형<sup>primitive</sup> 이나 String이 컬렉션에 들어 있는 경우
- 컬렉션의 equals 메서드는 객체들을 열거 형태로 꺼내서 순차적으로 equals하도록 되어있다.

```java
@Test
public void testListEqual_Primitive() {
	List<String> listA = new ArrayList<String>();
	listA.add("변정훈");
	listA.add("조연희");
	List<String> listB = new ArrayList<String>();
	listB.add("변정훈");
	listB.add("조연희");
	assertEquals ("리스트 비교", listA, listB);
}
```
#### 일반 객체가 컬렉션에 들어 있는 경우
- assertEquals는 기본적으로 기대값과 실제값을 서로 equals 비교한다. 그리고 컬렉션은 equals 비교 시에 원소를 하나씩 꺼내서 다시 각각에 대해 equals 비교를 한다. 그렇기 때문에 equals 메소드를 이용하기 위해서는 객체의 **equals 메소드를 재정의 할 것.**
```java
@Test
public void testListEqual_NotSorted() {
	List<Employee> listA = new ArrayList<Employee>();
	listA.add( new Employee("변정훈"));
	listA.add( new Employee("조연희"));
	List<Employee> listB = new ArrayList<Employee>();
	listB.add( new Employee("변정훈"));
	listB.add( new Employee("조연희"));
	assertEquals ("리스트 비교", listA, listB);
}
```
<sup></sup>