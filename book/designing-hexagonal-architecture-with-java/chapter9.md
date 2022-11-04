# 자바 모듈을 이용한 의존성 역전 적용

## JPMS 소개
- java 9 에서 Module 에 추가된 기능
- Module-info.java 를 생성하고 의존성을 관리
```java
ServiceLoader<RouterManagementOutputPort> loaderOutputRouter = ServiceLoader.load(RouterManagementOutputPort.class);
        RouterManagementOutputPort routerManagementOutputPort = loaderOutputRouter.findFirst().get();

```
와 같은 형태로 `RouterManagementOutputPort`의 구현체를 찾아줌.