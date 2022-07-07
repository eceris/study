# Kibana Query Language 정리

## 목차
1. 특정 필드 겁색
2. Boolean 검색
3. 범위 검색
4. 존재 여부 검색
5. 와일드카드 검색
6. Nested 필드 검색



## 1. 특정 필드 검색 (Terms Query)
특정 필드에 하나 이상의 term 이 포함되어 있는지 검색한다.

- company 필드에서 naver 를 검색하는 경우
    - `company:naver`
- `"naver kakao line"` 과 같이 구문 (phrase) 검색을 하는 경우 쌍따옴표 (`"`) 로 묶는다.
    - `company:"naver kakao line"`

> 만일 구문을 쌍따옴표로 묶지 않고 검색하면 단어 순서와 상관없이 검색된다.
> 예를들면 `naver kakao line` 도 검색되고 `naver line kakao` 도 검색된다.

> 만일 특정 필드를 지정하지 않고 검색하면 모든 필드에 대해 매칭을 시도한다.



## 2. Boolean 검색
`or`, `and`, `not` 연산자를 기본으로 지원하며, `and` 가 `or` 보다 높은 우선순위를 갖는다.
이를 변경하고 싶으면 소괄호로 묶어 그룹핑을 하면 된다.

- 직업이 개발자거나 회사가 naver 인 문서 검색
    - `job:developer or company:naver`
- 직업이 개발자이면서 회사가 naver 인 문서 검색
    - `job:developer and company:naver`
- 회사가 naver 또는 kakao 인 문서 검색
    - `company:(naver or kakao)`
- 직업이 개발자이면서 회사가 네이버 또는 카카오인 문서 검색
    - `job:developer and company:naver or company:kakao`
- 직업이 개발자가 아닌 문서 검색
    - `not job:developer`
- 직업이 개발자인데 회사가 네이버 또는 카카오가 아닌 문서 검색
    - `job:developer and not (company:naver or company:kakao)`
- 여러 단어들을 담고 있는 multi-value 필드 검색
    - `area:(frontend and backend and infra)`



## 3. 범위 검색 (Range Query)
`>`, `>=`, `<`, `<=` 를 사용할 수 있다

- 연봉 4000 이상 8000 미만 검색
    - `salary >= 4000 and salary < 8000`



## 4. 존재 여부 검색 (Exist queries)
특정 필드의 값이 존재하는 문서를 검색한다.

- 직업이 있는 문서 검색
    - `job:*`



## 5. 와일드카드 검색
- company.name 이 `naver shopping`, `naver cloud` 와 같이 naver 로 시작하는 문서들 검색
    - `company.name:naver*`
- 여러 필드 매칭 검색
    - `company.location*:Seongnam-Si`
    - `company.location`, `company.location.keyword` 와 같이 특정 필드가 text 와 keyword 등의 여러 종류의 버전을 가질 때 편리하게 사용할 수 있다.



## 6. Nested 필드 검색
Nested 필드 검색은 nested query를 어떻게 각각의 nested documents 에 매칭시켜볼 것인가가 주요 포인트이다.

1. **쿼리를 1개의 nested document 에만 매칭**: 대부분의 유저가 원하는 방식
2. 쿼리를 서로 다른 여러 nested documents 에 매칭: Regular object 필드에 적용되는 방식으로 단일 문서에 매칭시키는 것에 비해 효용성이 낮음

아래와 같이 `companies` 라는 nested field 로 예를 들어보자

```json
{
    "category": "COMPANY",
    "companies": [
        {
            "name": "naver",
            "industry": "engineering",
            "emp_num": 15000,
            "location": {
                "country": "Korea",
                "city": "Seongnam"
            }
        },
        {
            "name": "kakao",
            "industry": "internet",
            "emp_num": 8600,
            "location": {
                "country": "Korea",
                "city": "Jeju"
            }
        },
        {
            "name": "line",
            "category": "internet",
            "emp_num": 2300,
            "location": {
                "country": "Japan",
                "city": "Tokyo"
            }
        },
        {
            "name": "coupang",
            "emp_num": 37000,
            "category": "e_commerce",
            "location": {
                "country": "Korea",
                "city": "Seoul"
            }
        }
    ]
}
```
>기업 정보는 [Wikipedia](https://en.wikipedia.org/) 에서 발췌

#### 단일 문서에 대해 검색
- `분야가 internet 이면서 직원수가 5000명 이상인 회사` 검색
    - `companies:{ industry:internet and emp_num >= 5000 }`

#### 서로 다른 문서들에 대해 검색
- `분야가 e-commerce 인 회사`와 `직원수가 2500명 미만인 회사` 검색
    - `companies:{ industry:e_commerce } and companies:{ emp_num < 2500 }`
> 이렇게 하면 coupang 과 line 이 검색된다

#### 단일 문서와 서로 다른 문서들을 같이 검색
- `분야가 engineering 이면서 직원수가 5000 명인 회사`와 `직원수가 10000명 미만인 회사` 검색
    - `companies:{ industry:engineering and emp_num:5000 } and companies:{ emp_num < 10000 }`
> 이렇게 하면 첫번째 쿼리의 결과는 없고, 두번째 결과로는 kakao와 line이 검색된다

#### 여러 depth 의 nested 필드 검색
- `본사가 대한민국 성남에 위치한 회사` 검색
    - `companies.location:{ country:Korea and city:Seongnam }`



## 참고
- https://www.elastic.co/guide/en/kibana/current/kuery-query.html



