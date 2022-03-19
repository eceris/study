# 가변성을 제한하라

## 코틀린에서 가변성 제한하기

### 읽기전용 프로퍼티 val
- 
### 가변 컬렉션과 불변 컬렉션 구분 

### 데이터 클래스의 copy
- data 클래스의 copy 메서드를 사용해 불변객체로 만드는 것이 좋음
```kotlin
data class User(
	val name: String, 
	val surname: String
)
var user = User("Maja", "Markiewicz")
user = user.copy(surname = "Moskała")
print(user) // User(name=Maja, surname=Moskała)
```

## 정리
- var보다는 val을 사용
- mutable 프로퍼티보다는 immutable 프로퍼티를 사용
- mutable 객체와 클래스보다는 immutable 객체와 클래스를 사용
- 변경이 필요한 대상을 만들어야 한다면, immutable data 클래스로 만들
고 copy를 활용
- 컬렉션에 상태를 저장해야 한다면, mutable 컬렉션보다는 읽기 전용 컬렉션을 사용.
- mutable 객체를 외부에 노출하지 말기
