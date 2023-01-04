# Proximity Service

- yelp 와 같은 앱의 기능 구현을 위한 인터뷰
- 기능적 요구사항
	- 사용자의 위치에 기반하여 모든 사업장을 리턴
	- 사장님은 업종을 추가 삭제 업데이ㅡ 할수 있고 리얼타임은 아님.
	- 고객은 업장의 상세정보를 볼수 있음.
- 비기능적 요구사항 
	- 낮은 응답시간. 고객읜 빠르게 주변 업장을 찾을수 있어야 함.
	- 위치정보는 개인정보이므로 적절히 다룰것.
	- ha와 확장성을 고려해야 함. 

## Step 2 - Propose High-level Design and Get Buy-in

### Data model
- For a read-heavy system, a RDB such as MySQL can be a good fit. 

## High-level Design
![image](./high-level-design.png)

### Location-based service(LBS)
- read-heavy service with no write requests. 
- 특히 밀집 지역의 peak hour에 QPS는 높다.
- 상태없는 서비스이고, 수평적으로 스케일하기 쉽다.

### Database cluster
- primary-secondary 셋텁 
- primary 는 write 만 수행, 남은 replica 들이 read 수행. 데이터 불일치 가능성 있음.

### Algorithms to fetch nearby businesses
- 보통의 회사들은 지리공간 DB로 such as Geohash in Redis나  Postgres with PostGIS extension 을 사용.

#### Option 1: Two-dimensional search
```SQL
SELECT business_id, latitude, longitude,
FROM business
WHERE (latitude BETWEEN {:my_lat} - radius AND {:my_lat} + radius)
AND
(longitude BETWEEN {:my_long} - radius AND {:my_long} + radius) 
```
- 범위의 업장을 fetch 하려면 두개 컬럼의 교차연산이 필요한데, 각 경도 위도의 범위가 너무 커서 원하는 성능이 나오지않음. 

- 1차원 적인 쿼리는 인덱스로 커버할 수 있으나, 2차원의 데이터를 얻기는 쉽지 않음.

- 널리 알려진바에 의하면 지리공간 인덱싱 방법은 아래와 같은 두가지 방식이 존재...   
![different-types-of-geospatial-indexes.png](different-types-of-geospatial-indexes.png)

- 두 방법의 구현은 달라도 아이디어는 비슷한데, 쿼리속도를 빠르게 하기 위해 `지도를 작은 크기로 잘라서, 인덱싱을 먹인다는 점`은 비슷함.

#### Option 2: Evenly divided grid
- 균등하게 지도를 나누는 방법
- 도시의 밀집도와 사막, 바다의 밀집도가 다르므로 이슈가 있음...

#### Option 3: Geohash
- 경도와 위도를 하나의 스트링으로 reducing 하는 방법
- 지도를 4분면으로 계속해서 나누어서 계산하면 아래와 같은 범위를 커버
![geohash-length-to-grid-size-mapping](./geohash-length-to-grid-size-mapping.png)
- 그러나 boundary 이슈가 있음. 같은 해쉬값 prefix 라면 근처라는 가정이 깨짐. 4분면의 분면이 다르면 근처라고 하더라도 해쉬값이 많이 변경됨.
```SQL
SELECT * FROM geohash_index WHERE geohash LIKE '9q8zn% 
```
- 위와 같은 쿼리로 검색시 실제 근처지만, 쿼리 불가능.



#### Option 4: Quadtree
- 쿼드트리는 2차원 공간을 반복적으로 4분면으로 나누어 특정한 조건의 장소를 찾는 자료구조.
- 



