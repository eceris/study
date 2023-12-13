# 02장 데이터 모델과 질의 언어

## 관계형 데이터베이스와 오늘날의 문서 데이터베이스
- 관계형 모델에서는 `외래키` 라고 부르고, 문서 모델에서는 `문서참조` 라고 부름.

### 문서 모델에서의 스키마 유연성
- 문서 데이터베이스를 스키마리스(schemaless) 라고 부르지만 실제로는 그렇지 않음.
- 쓰기 스키마(schema-on-write) : 관계형 데이터베이스의 전통적인 접근 방식으로 스키마는 명시적이고 데이터 베이스는 쓰여진 모든 데이터가 스키마를 따르고 있음을 보장.
- 읽기 스키마(schema-on-read) : 데이터 구조는 암묵적이고 데이터를 읽을 때만 해석.
- mysql은 아래와 같은 데이터 마이그레이션 시에 전체 테이블을 복사해야하는 제약사항 존재
```
ALTER TABLE users ADD COLUMN first name text;
UPDATE users SET first_name = split_part (name,'', 1); --postgresql
UPDATE users SET first name = substring index (name, '', 1); --mysql
```
- 보통은 first_name 의 default = Null 로 하고 읽기 시점에 마이그레이션 함.

### 문서 데이터베이스와 관계형 데이터 베이스의 통합
- 서로 부족한 부분을 채워나가는 중
	+ 예를 들면 mySqldml json 타입
	
## 데이터를 위한 질의 언어
- 선언형 과 명령형
	+ 웹의 css 는 선언형
	```
	li.selected > p {
		background-color : blue;
	}
	```
	+ javascript 는 명령형
	```
	var liElements = document.getElementsByTagName ("li");
	for (var i = 0; i < liElements.length; itt) {
		if (lielements [i].className === "selected") {
			var children = liElements[i].childNodes;
			for (var j = 0; j < children. length; j++) {
				var child = children[j];
				if (child.nodeType === Node.ELEMENT_NODE && child.tagName === "P")  {
					child.setAttribute("style", "background-color: blue");
				}
			}
		}
	}
	```

### 맵리듀스 질의
- 몽고, 카우치 등 일부 NoSql 에서 지원
- 선언형도 명령형도 아닌 중간의 형태
- 여러 함수형 프로그래밍언어에 있는 map(collect 라고도 함), reduce(fold or inject) 함수를 기반으로 함.

### 그래프형 데이터 모델
- 애플리케이션이 주로 다대다 관계가 많다면 그래프로 데이터를 모델링이 좋음.
- 
#### 사이퍼 질의 언어
- 속성그래프를 위한 선언형 질의 언어

#### 트리플 저장소와 스파클
- 트리플 저장소는 모든 정보를 주어(subject), 서술어(predicate), 목적어(object)의 세 부분 구문으로 저장
- 스파클은 RDF(Resource Description Framework) 데이터 모델을 사용한 트리플 저장소 질의 언어

## 정리
- 문서 데이터베이스 : 데이터가 문서 자체에 포함되어 있음. 하나의 문서가 다른 문서와 거의 관련이 없음.
- 그래프 데이터베이스 : 문서데이터베이스와 반대로 모든것이 잠재적으로 관련이 있는 사례를 대상으로 함 
- 각 데이터 모델은 고유한 `질의 언어` 혹은 `프레임워크`를 제공
	+ SQL, MapReduce, 몽고DB의 aggregation pipeline, 사이퍼, 스파클, 데이터로그 등등..
