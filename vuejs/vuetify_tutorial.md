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

# 3. Vuetify Basics 
- `v-app`은 우리가 알고 있는 `<div id="app"></div>` 와 같다.
- html의 attribute 를 통해 디자인을 변경 가능한데 이것은 각 component 마다 다르니 [문서](https://vuetifyjs.com/en/components/toolbars)를 참조하는 것이 좋다. 예를 들면..
```html
<v-toolbar app light height="150" class="indigo">
</v-toolbar>
```
- 위의 app 은 nav bar를 상단에 고정시키는 역할, light 는 theme을 밝게, height는 높이를 150으로 지정
- `class="indigo"` 는 배경색을 indigo로 하겠다는 의미, 자세한 것은 위와 같이 [문서](https://vuetifyjs.com/en/components/toolbars)를 참조
- 보통 `<v-contnet>` 는 body의 컨텐츠를 의미하고 아래와 같이 `<router-view>`를 생성해서 사용한다.
```html
<v-app>
	<v-content>
		<router-view></router-view>
	</v-content>
</v-app>
```

# 4. Text & Colors

## Colors
- 기본적인 html tag 에 class 를 추가하여 색상을 다룰 수 있다.
```html
<div>
	<p class="red white--text">빨간색 배경에 흰색 글씨</p>
	<p class="pink lighten-4 red--text text--darken-4">분홍 배경인데 밝음 정도가 4, 빨간 글씨인데 진한 정도가 4</p>
</div>
```
- 기본적인 색상은 [문서](https://vuetifyjs.com/en/framework/colors) 를 참조

## Text
- 기본적인 html tag 에 class 를 추가하여 텍스트 크기나 모양을 다룰 수 있다.
```html
<div>
	<h1 class="display-4">엄청나게 큰 글씨</p>
	<h4 class="display-1">작은데 h1 display-4 보다 작음</p>
</div>
```
- 심지어 텍스트의 lowercase, uppercase, capitalize 도 가능.
```html
<div>
	<h1 class="display-4 text-capitalize">엄청나게 큰 글씨인데 capitalize 함</p>
	<h4 class="display-1 text-uppercase">작은데 h1 display-4 보다 작고 uppercase 함</p>
</div>
```
- 자세한 건 [문서](https://vuetifyjs.com/en/framework/typography)를 참조










