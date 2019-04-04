# Transactional

org.springframework.transaction.annotation.Transactional

Spring에서는 transaction을 \@Transactional 이라는 애노테이션으로 간단하게 생성하고 관리할 수 있다.

```java
@EnableTransactionManagement
public class DataSourceConfig {
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory().getObject());
	}
}

```

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void transactionService() {
  log.info("currentTransactionName : {}", TransactionSynchronizationManager.getCurrentTransactionName());
}
```
## isolation
격리수준을 정의, 트랜잭션에서 일관성이 없는 데이터를 허용하는 수준

```java
@Transactional(isolation = Isolation.DEFAULT)
```

### DEAFULT
- datastore의 기본 격리 수준을 그대로 사용.

### READ_UNCOMMITTED (level 0)
- 트랜잭션에 처리중인 혹은 아직 커밋되지 않은 데이터를 다른 트랜잭션이 읽는 것을 허용
- 어떤 사용자가 A라는 데이터를 B라는 데이터로 변경하는 동안 다른 사용자는 B라는 아직 완료되지 않은(Uncommitted 혹은 Dirty) 데이터 B를 읽을 수 있다.

Dirty read
> 다른 트랜잭션에서 처리작업이 완료되지 않았음에도 다른 트랜잭션에서 읽는 것을 dirty read라고 하며 READ UNCOMMITTED 격리수준에서만 일어나는 현상

### READ_COMMITTED (level 1)
- dirty read 방지 : 트랜잭션이 커밋되어 확정된 데이터만을 읽는 것을 허용
- 어떠한 사용자가 A라는 데이터를 B라는 데이터로 변경하는 동안 다른 사용자는 해당 데이터에 접근할 수 없다.

### REPEATABLE_READ (level 2) 
- 트랜잭션이 완료될 때까지 SELECT 문장이 사용하는 모든 데이터에 shared lock이 걸리므로 다른 사용자는 그 영역에 해당되는 데이터에 대한 수정이 불가능
- 선행 트랜잭션이 읽은 데이터는 트랜잭션이 종료될 때까지 후행 트랜잭션이 갱신하거나 삭제하는 것을 불허함으로써 같은 데이터를 두 번 쿼리했을 때 일관성 있는 결과를 리턴함

### SERIALIZABLE (level 3)
- 완벽한 읽기 일관성 모드를 제공
- 데이터의 일관성 및 동시성을 위해 MVCC(Multi Version Concurrency Control)을 사용하지 않음(MVCC는 다중 사용자 데이터베이스 성능을 위한 기술로 데이터 조회 시 LOCK을 사용하지 않고 데이터의 버전을 관리해 데이터의 일관성 및 동시성을 높이는 기술)
- 트랜잭션이 완료될 때까지 SELECT 문장이 사용하는 모든 데이터에 shared lock이 걸리므로 다른 사용자는 그 영역에 해당되는 데이터에 대한 수정 및 입력이 불가능하다.


## propagation
트랜잭션 전파규칙을 정의 , Default=REQURIED

```java
@Transactional(propagation=Propagation.REQUIRED)
```

| 이름 | 동작 |
|:--------|:--------|
| REQUIRED | 부모 트랜잭션 내에서 실행하며 부모 트랜잭션이 없을 경우 새로운 트랜잭션을 생성 |
| SUPPORTS | 트랜잭션 내에서 실행하며 부모 트랜잭션이 없을 경우 nontransactionally로 실행 |
| MANDATORY | 부모 트랜잭션 내에서 실행되며 부모 트랜잭션이 없을 경우 예외가 발생 |
| REQUIRES_NEW | 부모 트랜잭션을 무시하고 무조건 새로운 트랜잭션이 생성 |
| NOT_SUPPORTED | nontransactionally로 실행하며 부모 트랜잭션 내에서 실행될 경우 일시 정지후 실행 |
| NEVER | nontransactionally로 실행되며 부모 트랜잭션이 존재한다면 예외가 발생 |
| NESTED | 해당 메서드가 부모 트랜잭션에서 진행될 경우 별개로 커밋되거나 롤백될 수 있음. 둘러싼 트랜잭션이 없을 경우 REQUIRED와 동일하게 작동 |



## readOnly
transactional readonly를 쓰는 것이 맞는가?에 대한 문제 ...


https://asktom.oracle.com/pls/apex/f?p=100:11:0::::P11_QUESTION_ID:9527439800346577724

https://stackoverflow.com/questions/34797480/why-do-spring-hibernate-read-only-database-transactions-run-slower-than-read-wri


## rollbackFor

## noRollbackFor

## timeout





# 작성중 ....