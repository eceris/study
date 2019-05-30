# interface
- `src/components/interface.js` 에 구현되어 있으며 아래와 같이 사용

```javascript
this.$AppInterface.popup(JSON.stringify({
    "title": "알림",
    "content": "올바른 이메일주소를 입력해주세요. (예 : email@example.com)"
}));

```
# component
- src/components/report 패키지에 구현 중

# http request
- `src/infra/http/http.js` 에 구현되어 있으며 환경에 따라 동작이 다름<sup>dev 인 경우, mocks.json을 참조</sup>

# 상태 관리
- `src/infra/store/*.js` 에 구현되어 있으며 필요에 따라 구현해서 사용.

# production vs development
- .env.* 파일에 기록중이며 각 환경마다 다르게 사용될 변수를 정의

# 테스트
- [비교결과](https://github.com/eceris/study/blob/master/today-i-learned/2019-1Q.md#unit-testing-vue-components-%EC%97%90-%EB%8C%80%ED%95%B4%EC%84%9C-%EC%A2%80-%EC%95%8C%EC%95%84%EB%B4%84) 간단히 사용가능하고, 우리한테 맞을 것 같은 Jest 를 사용.

# 사용되는 라이브러리
- jest : testing
- babel : compiler
- vue : view


- moment : 



# 빠르게 검사하는 숙제

# 뷰가 뭘까?
 - 대략적인 설명....
 - [뷰는 여기에 잘 나와있어요...](https://kr.vuejs.org/v2/guide/index.html)

# 그럼 니가 한건 뭐야?

## View
- 아벤타도르에서 컴포넌트를 담는 틀(router 를 통해 들어오는 화면)

## [Component](https://kr.vuejs.org/v2/guide/components.html)
- 아벤타도르에서 재사용가능한 코드블록(뷰 인스턴스)

## [Vuex](https://vuex.vuejs.org/)
- state
- getters
- mutations
- actions
- 그래서 어떻게 사용했는지?

## Http
- Axios
- 어떻게 사용했는지?

## Test

### [Vue Test Utils](https://vue-test-utils.vuejs.org/)
 - Vue.js 의 공식 테스트 라이브러리로 컴포넌트의 유닛테스팅에 필요한 다양한 api를 제공.
 
### [Jest](https://jestjs.io/docs/en/using-matchers)
 - [가장 완벽한 테스트러너이다.](https://vue-test-utils.vuejs.org/guides/#getting-started)
 - 페이스북에서 만든 테스트 프레임워크
 - snapshot 테스트 기능 제공
 - Mocha 를 사용하면 웹팩 컨피그를 직접 수정하여 뭔가 하는게 많음.
 - 근데 Dependency가 있는 라이브러리들의(Babel 버전과 Mocha 버전등) version mismatch로 호환을 맞춰주기 힘듬
 - 그리고 Jest 는 간단히 SnapshotHtml element에 직접 접근하여 검증하기 위한 html snapshot 테스트도 지원함.
 - test coverage 도 기본적으로 지원하는데, package.json에 아래와 같이 coverage 설정을 해주면 됨.
 
### 어떻게 작성했는가?
- 그리고 어떻게 작성했는지?

## Environment
- [Webpack](https://webpack.js.org/concepts)
- [vue-loader](https://vue-loader-v14.vuejs.org/kr/workflow/production.html)

### [Babel](https://babeljs.io/docs/en/)
 -  최신 버전의 javascript 로 작성을 도와주는 도구. 여러 라이브러리를 참조하여 작성할 경우 각 라이브러리들의 ECMAScript 호환을 도와줌.

# 후기
 - 이 포스팅은 개인적인 경험을 토대로 작성된 글이며, Vue.js 재단으로부터 어떠한 지원도 받지 않았습니다. 지극히 주관적인 후기이니 참고만 해주세요.
 
## 장점
 - 증명서 로직 파악.
 - 새로운 경험, 맨 바닥에서 시작, 내가 하고 싶은대로??!!
 - 2-way-binding 이라, 데이터 동기화에 대해 걱정할 필요 없음.
 - 테스트 환경을 구축해서 마음에 안정을 취할수 있음.
 - 가독성 ⬆
 - 다른 라이브러리? 프레임워크? 에 비해 학습곡선이 낮음

## 단점
 - jQuery 와 비교될 정도로 interactive 한 viewing 이 힘듬.
    - jQuery 는 부모자식 컴포넌트라는 개념이 없고, Document 에서 select 하여 처리하므로 어떤 element 든 접근이 가능.
    - Vue 는 Element 에 직접 접근하는 것을 지양하며, 접근가능한 scope 를 현재 컴포넌트로 제한.
 - Vue.js 는 템플릿문법의 사용을 강제하여, `plain html` 이 아니라 로직이 쉽게 html 코드로 스며듬.
  
## 느낀점
 - backbone -> vue 가 어려운 것이 아님. 그 외에 빌드, 배포, 환경 구축 등이 어려운 것들.
 - 다 좋았는데, 처음부터 이걸 하면서 같이 얘기해보고 구성하는 그런게 없어서 조금 아쉬었다. 다음에 하게되면 같이 해봤으면 좋겠다.
 - 고민이 되는 건, 여러 프로젝트의 프론트엔드를 vue 로 바꿔볼까, 그럼 앞으로 이것들이 또 레거시가 되는건 아닌가? 그래서 조심스러움.


