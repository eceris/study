youtube 의 vuetify tutorial을 보고 기록.

# 1. What is Vuetify?
- UI component library based on Google Material Design
- Like Materialize, but specifically for Vue.js
- 샘플 코드는 [여기](https://github.com/iamshaunjp/vuetify-playlist) 에서 확인 가능

# 2. Setting Up Vuetify
- vue create APPNAME
후에 select some options(saas, router, vuex...)
- cli 3를 이용해 위와 같이 앱을 생성하면 기본적인 vue scafolding 완성

## install vuetify
- vue cli3 를 이용해 vuetify 플러그인 설치
```
vue add vuetify
```
- 플러그인이 설치됨과 동시에 vue 파일들이 vuetify 형식에 맞게 수정
- `v-` 으로 시작하는 directive 가 vuetify directive
