youtube 의 [Vue CLI3 tutorial](https://www.youtube.com/playlist?list=PL4cUxeGkcC9iCKx06qSncuvEPZ7x1UnKD)을 보고 기록한다.

# 1. Vue CLI Introduction

## Creating a Project
old cli: 
	```
	vue init webpack-simple MYAPP 
	```

new cli: 
	```
	vue create MYAPP 
	```

Default preset

- eslint
- babel

Custom preset

- vuex
- vue router
- pwa support
- etc...

## Webpack Configuration

- Webpack config는 추상화되어 사라짐(hidden)
- Webpack config 를 vue.config.js 로 끌어댕김
- Plugins 으로? Webpack config 를 수정가능


## Plugins

- config 와 functionality 를 확장 가능
- 기본 dependencies를 지원하고 아래도 또한 지원
	- Edit the webpack config
	- Edit source files(e.g. templates)
	- Add extra commands to the CLI

## Instant Prototyping

- 빠르게 single, standalone 형태의 components를 프로토타입핑이 가능.
- Vue project 개발환경을 직접 세팅할 필요 없음

## Graphical User Interface

- projects 를 쉽게 생성하고 관리 가능
- dependencies 와 plugins 를 설치 및 관리


# 2. Using the new CLI
```
$ node -v 
$ npm uninstall -g vue-cli // vue cli 구버전 삭제
$ npm install -g  @vue/cli // vue cli 신버전 설치 vue scope의 cli 설치
$ vue create MYAPP // vue cli 로 project 생성
```
- babel.config.js 에 presets 목록에 @vue/app 이 기본으로 세팅되는데 이것은
babel은 아직 지원하지 않는 새로운 기능들이나 polyfills 를 자동으로 지원해줌.


# 3. Dev Server

- package.js 에 보면 @vue/cli-service 가 보이는데, 이것은 vue cli service 를 로컬에 설치해서 lint 나 이런것들을 자유롭게 사용하도록 도와줌. 

```
$ npm run serve 
```

# 4. Custom Presets
- `$ vue create` 할 때 옵션을 지정할 수 있는데, dedicated config files 를 선택하면 packages.json 에 모든 옵션이 포함되지 않고, 프로젝트 루트에 관련 파일들이 생성된다.(.eslintrc.js, .browserslistrc... 등등...)

# 5. Plugins 
cli 에서 아래와 같이 동작한다. 

- `vue add babel` // cli-plugin-babel 이 추가된다.
- `vue add vuetify` // mertierial 디자인으로 쉽게 디자인할 수 있도록 도와줌.

vuetify 를 add 하면 template의 모든 마크업에 클래스도 바뀌고 자동으로 마이그레이션 해준다.

# 6. Build & Deploy to Firebase

1. console.firebase.google.com 에 프로젝트 추가 
2. project dashboard 에서 hosting을 클릭
3. `$ npm install -g firebase-tools`
4. firebase 를 이용하여 로그인하여 init!
	`$ firebase login`
	`$ firebase init`
	`$ firebase deploy`
5. hosting을 선택하고 deploy할 폴더를 선택 : dist
6. 이렇게 해서 `npm run build` 할 경우, firebase cli가 dist에 자동으로 bundling 해줌
7. 실제 firebase에 배포하기 위해 
	`$ firebase deploy --only hosting`
8. 그리고 console에 보면 배포된 것을 확인

# 7. Instant Prototyping
- 프로토타이핑을 위한 작업
	`$ npm install -g @vue/cli-service-global `

- 아래와 같이 하면 npm 이 싱글 컴포넌트를 프로토타이핑 할 수 있도록 도와줌.
	`$ vue serve online.vue`

# 8. The Vue GUI
- ui 로 제공하는 vue cli
	`$ vue ui`
- 위에서 제공했던 cli 를 gui 로 모두 처리 할 수 있다.
- graphql 을 위한 apollo 

# 9. Using 'vue init' with the CLI3 
- CLI 1, 2 에서 사용하던 vue init 을 사용하기 위해서는 아래와 같이 cli-init 을 수동으로 설치해주어야 한다. 
	`$ npm install -g @vue/cli-init`




































