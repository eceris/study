# youtube 의 [Vuetify tutorial](https://www.youtube.com/playlist?list=PL4cUxeGkcC9g0MQZfHwKcuB0Yswgb3gA5)을 보고 기록.

1. [What is Vuetify?](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#1-what-is-vuetify)
2. [Setting Up Vuetify](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#2-setting-up-vuetify)
3. [Vuetify Basics](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#3-vuetify-basics)
4. [Text & Colors](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#4-text--colors)
5. [Button & Icons](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#5-buttons--icons)
6. [Breakpoints & Visibility](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#6-breakpoints--visibility)
7. [Toolbars](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#7-toolbars)
8. [Navigation Drawers](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#8-navigation-drawers)
9. [Themes](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#9-themes)
10. [Lists](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#10-lists)
11. [Adding Routes](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#11-adding-routes)
12. [Padding & Margin](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#12-padding--margin)
13. [The Grid System(part 1)](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#13-the-grid-system---1)
14. [The Grid System(part 2)](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#14-the-grid-system---2)
15. [Dummy Project Data](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#15-dummy-project-data)
16. [Chips](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#16-chips)
17. [Sorting Projects](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#17-sorting-projects)
18. [Tooltips](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#18-tooltips)
19. [Cards](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#19-cards)
20. [Avatars](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#20-avatars)
21. [Expansion Panels](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#21-expansion-panels)
22. [Menus](https://github.com/eceris/study/blob/master/frontend/vuejs/vuetify_tutorial.md#22-menus)
---

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


# 5. Buttons & Icons

## [Buttons](https://vuetifyjs.com/en/components/buttons#button)
- 아래와 같이 사용 가능하고 `class` attr 로 색상을 다룰 수도 있고 약속된 `depressed, flat` 과 같은 attr을 추가하여 사용할 수 있다.
```html
<v-btn class="pink white--text">click me</v-btn>
<v-btn depressed color="pink">click me</v-btn>
<v-btn flat color="pink">click me</v-btn>
```
- 근데 depressed 의 color 는 background color 인데 flat 의 color는 font 컬러를 뜻한다. 좀 헷갈림.

## [Icons](https://vuetifyjs.com/en/framework/icons#icons)
- 간단히 [material 아이콘](https://material.io/tools/icons)을 사용할 수 있음.
- 재밌는게 icons에 fav 라는 attr이 있음.

```html
<v-btn flat color="pink">
	<v-icon left>email</v-icon>
</v-btn>

<v-btn fab color="pink">
	<v-icon left>email</v-icon>
</v-btn>
```

# 6. Breakpoints & Visibility

## [Breakpoints](https://vuetifyjs.com/en/framework/breakpoints#breakpoints)
- vuetify는 Material Design 의 Viewport Breakpoints 를 갖고 있는데, 그것에 따라 노출 여부 등을 변경할 수 있다.(window 에 resize 이벤트로 걸려있는듯)
- 각각의 code<sup>xs, sm, md, lg, xl</sup> 는 width range를 갖고 있음.

## Visibility
- BreakePoints 에 따라 Visibility를 정할 수 있는데, `class="hidden-md-and-down"` 과 같이 정할 수 있다.
```html
<v-btn class="hidden-md-and-down">Click me</v-btn> // 미디엄 사이즈 하위로는 버튼을 감춤
<v-btn class="hidden-md-and-up">Click me</v-btn> // 미디움 사이즈 상위로는 감춤
<v-btn class="hidden-sm-only">Click me</v-btn> // 스몰 사이즈 에만 감춤
```

# 7. Toolbars

- `<v-toolbar>` 를 이용하여 툴바를 만들 수 있는데 `app` attr 을 추가하면 항상 상단에 fixed 된 상태로 스크롤된다.

```html
<template>
	<nav>
		<v-toolbar flat app>
			<v-toolbar-title>
				<span class="font-weight-light">Todo</span>
				<span>App</span>
			</v-toolbar-title>
			<v-spacer></v-spacer>
			<v-btn flat color="grey">
				<span>Sign out</span>
				<v-icon>exit_to_app</v-icon>
			</v-btn>
		</v-toolbar>
	</nav>
</template>
```
- `<v-spacer>`는 빈 공간을 채우기 위한 유용한 tag 이다.


# 8. Navigation Drawers

```
<v-toolbar>
	<v-toolbar-side-icon class="grey--text" v-on:click="drawer = !drawer"></v-toolbar-side-icon>
</v-toolbar>

<v-navigation-drawer app class="indigo" v-model="drawer">
<p>test</p>
</v-navigation-drawer>

<script>
export default {
	data() {
		return {
			drawer : true
		}
	}
}
</script>
```
- 사이드에 네비게이션 바를 넣고 싶은 경우 `v-navigation-drawer` 를 사용
- `v-navigation-drawer` 에 꼭 app prop 을 넣어주어야 현재 앱에 적용됨.
- v-toolbar-side-icon 는 toolbar 에 아이콘을 넣는 것.
- `app` prop를 넣어주어야 함.

# 9. Themes
- 기본적으로 success, error, waring, info 의 theme이 존재.
- plugin 의 vuetify.js 에 theme을 overwrite 할 수 있음.
```
Vue.use(Veutify, {
	iconfont : 'md',
	theme : {
		primary : '#9652ff',
		success : '#3cd1c2',
		info : '#ffaa2c',
		warning : '#f83e70'
	}
})
```
- 위와 같이 기본적인 theme을 수정 가능.

# 10. [Lists](https://vuetifyjs.com/en/components/lists#list)
- `v-list-tile-content` 은 컨텐츠를 담기 위한 tag
- `v-list-tile-action` 은 아이콘을 추가하기 위한 tag

```html
<v-list>
	<v-list-tile>
		<v-list-tile-action>
			<v-icon>dashboard</v-icon>
		</v-list-tile-action>

		<v-list-tile-content>
			<v-list-tile-title class="white--text">Dashboard</v-list-tile-title>
		</v-list-tile-content>
	</v-list-tile>
</v-list>
```

- route 하기 위해 `<v-list-tile>` 에 router to prop을 추가.
```html
<template>
	<v-list>
		<v-list-tile v-for="link in links" :key="link.text" router to ="link.route">
			<v-list-tile-action>
				<v-icon>{{ link.icon }}</v-icon>
			</v-list-tile-action>

			<v-list-tile-content>
				<v-list-tile-title class="white--text">{{ link.text }}</v-list-tile-title>
			</v-list-tile-content>
		</v-list-tile>
	</v-list>
</template>
<script>
export default {
	data() {
		return {
			links : [
				{icon : 'dashboard', text : 'Dashboard', route : '/'},
				{icon : 'folder', text : 'My Projects', route : '/projects'},
				{icon : 'person', text : 'Team', route : '/team'}
			]
		}
	}
}
</script>
```

# 11. Adding Routes
- skip 해도 될 것 같음. 단순한 router 설명.

# 12. Padding & Margin
- {property}{direction}--{size} 의 형태로 padding 과 margin 을 줄 수 있음.

## property

아래와 같이 줄여서 사용

- `m` : margin
- `p` : padding

## direction
아래와 같이 줄여서 사용

- `t` : *-top
- `b` : *-bottom
- `l` : *-left
- `r` : *-right
- `x` : *-left와 *-right
- `y` : *-top와 *-bottom
- `a` : 모든 direction

## size
- `auto` : 사이즈를 자동으로 지정
- `0` : 공간 제거
- `1` : 공간을 `$spacer * .25` 로 지정
- `2` : 공간을 `$spacer * .5` 로 지정
- `3` : 공간을 `$spacer` 로 지정
- `4` : 공간을 `$spacer * 1.5` 로 지정
- `5` : 공간을 `$spacer * 3` 로 지정

## example

- 아래는 `<v-content>` tag 에 `horizontal` `margin` 을 `$spacer * 1.5` 정도 준 것.

```html
<v-content class="mx-4">
</v-content>
```

- `<v-container>` 는 center focused page 를 위해 사용 혹은 `fluid` 속성을 줘서 full width로 확장 가능.
- `<v-layout>` section을 separate 하고 `v-flex` 를 포함한다.

```html
<v-container class="my-5">
</v-container>
```


# 13. [The Grid System - 1](https://vuetifyjs.com/ko/framework/grid#)
- 기본적으로 `flex-box` 을 바탕으로 구현되었고 12 포인트 그리드 시스템을 사용.
- 아래와 같이 `v-flex`의 prop을 통해 각 breakepoint 에 정렬을 지정할 수 있음.

## example
- 예를 들어 버튼을 노출하는데 xs 일때는 12 컬럼을 모두 차지, md 일때는 6 컬럼 만 차지 하고 싶다면 꼭 `v-latout` 에 `wrap` 속성을 명시할 것.
```html
<v-content class="my-5">
	<v-layout row wrap>
		<v-flex xs12 md6>
			<v-btn outline block class="primary">button 1</v-btn>
		</v-flex>
		<v-flex xs12 md6>
			<v-btn outline block class="secondary">button 2</v-btn>
		</v-flex>
	</v-layout>
</v-content>
```

- 아래는 미디어타입 브레이크포인트가 xs 인 경우 상단에 button1이 12컬럼을 차지, 하단에 나머지 버튼이 노출 
- 미디어타입 브레이크 포인트가 md 일때는 상단에 button1이 6컬럼을 차지, 나머지 버튼이 2컬럼씩 차지하여 노출
```html
<v-content class="my-5">
	<v-layout row wrap>
		<v-flex xs12 md6>
			<v-btn outline block class="primary">button 1</v-btn>
		</v-flex>
		<v-flex xs4 md2>
			<v-btn outline block class="secondary">button 2</v-btn>
		</v-flex>
		<v-flex xs4 md2>
			<v-btn outline block class="waring">button 3</v-btn>
		</v-flex>
		<v-flex xs4 md2>
			<v-btn outline block class="error">button 2</v-btn>
		</v-flex>
	</v-layout>
</v-content>
```

- 만약 아래와 같이 12컬럼을 채우지 못할 경우, 나머지가 빈칸으로 노출되는데, 빈칸을 제외하고 가운데 정렬을 하고 싶다면 `<v-layout>` 에 `justify-center` 속성을 추가하면 됨. 자세한 내용은 [문서를 참조](https://vuetifyjs.com/ko/framework/grid#layout-playground)
```html
<v-content>
	<v-layout row wrap>
		<v-flex xs4 md3>
			<v-btn outline block class="success">1</v-btn>
		</v-flex>
		<v-flex xs4 md3>
			<v-btn outline block class="success">2</v-btn>
		</v-flex>
	</v-layout>
</v-content>
```

# 14. [The Grid System - 2](https://vuetifyjs.com/ko/framework/grid#)

- 아래와 같이 reactive 하게 grid 를 생성할 수 있다.
```html
<v-content>
	<v-card flat class="pa-3">
		<v-layout row wrap>
			<v-flex xs12 md6>
				<div class="caption grey--text">Project Title</div>
				<div>Create new website</div>
			</v-flex>
			<v-flex xs6 sm4 md2>
				<div class="caption grey--text">Person</div>
				<div>John</div>
			</v-flex>
			<v-flex xs6 sm4 md2>
				<div class="caption grey--text">Due by</div>
				<div>1st Jan 2019</div>
			</v-flex>
			<v-flex xs2 sm4 md2>
				<div class="caption grey--text">Status</div>
				<div>ongoing</div>
			</v-flex>
		</v-layout>
	</v-card>
</v-content>
```

# 15. Dummy Project Data
- `v-for` 를 이용하여 Dummy Project 를 순회하여 보여줌. 
- 동적으로 프로젝트의 상태를 보여주기 위해 클래스를 동적으로 변경하는데 `${project.status}` 와 같이 노출하면 동적으로 바인딩 

```html
<template>
  <div class="dashboard">
    <h1 class="subheading grey--text">Dashboard</h1>

    <v-container class="my-5">
      
      <v-card flat v-for="project in projects" :key="project.title">
        <v-layout row wrap :class="`pa-3 project ${project.status}`">
          <v-flex xs12 md6>
            <div class="caption grey--text">Project title</div>
            <div>{{ project.title }}</div>
          </v-flex>
          <v-flex xs6 sm4 md2>
            <div class="caption grey--text">Person</div>
            <div>{{ project.person }}</div>
          </v-flex>
          <v-flex xs6 sm4 md2>
            <div class="caption grey--text">Due by</div>
            <div>{{ project.due }}</div>
          </v-flex>
          <v-flex xs2 sm4 md2>
            <div class="caption grey--text">Status</div>
            <div>{{ project.status }}</div>
          </v-flex>
        </v-layout>
        <v-divider></v-divider>
      </v-card>

    </v-container>
   
  </div>
</template>

<script>
export default {
  data() {
    return {
      projects: [
        { title: 'Design a new website', person: 'The Net Ninja', due: '1st Jan 2019', status: 'ongoing', content: 'content'},
        { title: 'Code up the homepage', person: 'Chun Li', due: '10th Jan 2019', status: 'complete', content: 'content'},
        { title: 'Design video thumbnails', person: 'Ryu', due: '20th Dec 2018', status: 'complete', content: 'content'},
        { title: 'Create a community forum', person: 'Gouken', due: '20th Oct 2018', status: 'overdue', content: 'content'},
      ]
    }
  }
}
</script>

<style>
.project.complete{
  border-left: 4px solid #3CD1C2;
}
.project.ongoing{
  border-left: 4px solid orange
}
.project.overdue{
  border-left: 4px solid tomato;
}
```

# 16. [Chips](https://vuetifyjs.com/en/components/chips#chip)
- 작은 정보를 보여주기위해 사용하는 컴포넌트

```html
<template>
<v-flex xs2 sm4 md2>
	<div class="right">
	  <v-chip small :class="`${project.status} white--text my-2 caption`">{{ project.status }}</v-chip>
	</div>
</v-flex>
</template>
<style>
.v-chip.complete{
  background: #3cd1c2;
}
.v-chip.ongoing{
  background: #ffaa2c;
}
.v-chip.overdue{
  background: #f83e70;
}

</style>
```

# 17. Sorting Projects
- sorting 을 위해 javascript 에서 array 객체에 제공하는 sort 함수를 사용
```javascript

sortBy(prop) {
	this.projects.sort((a,b) => a[prop] < b[prop] ? -1 : 1)	
}
```

# 18. [Tooltips](https://vuetifyjs.com/en/components/tooltips#tooltip)
- tooltip 을 사용하기 위해 `slot="activator"`을 이용하여 상위 컴포넌트에 tooltip을 꽂을꺼라는 것을 알려줌.

```html
<v-layout row justify-start class="mb-3">
	<v-tooltip top>
	  <v-btn small flat color="grey" @click="sortBy('title')" slot="activator">
	    <v-icon small left>folder</v-icon>
	    <span class="caption text-lowercase">By project name</span>
	  </v-btn>
	  <span>Sort by project name</span>
	</v-tooltip>
	<v-tooltip top>
	  <v-btn small flat color="grey" @click="sortBy('person')" slot="activator">
	    <v-icon small left>person</v-icon>
	    <span class="caption text-lowercase">By Person</span>
	  </v-btn>
	  <span>Sort by project author</span>
	</v-tooltip>
</v-layout>
```
# 19. [Cards](https://vuetifyjs.com/en/components/cards#card)
- 이미지를 바탕으로 한 카드형태의 뷰
- 아래는 `v-flex` 컴포넌트로 뷰포인트별 정렬을 지정하고 `v-card` 로 카드형태의 아이템을 나열하였다.
- `v-responsive` 컴포넌트로 이미지를 리액티브하게 표현.

```
<template>
  <div class="team">
    <h1 class="subheading grey--text">Team</h1>

    <v-container class="my-5">

      <v-layout row wrap>
        <v-flex xs12 sm6 md4 lg3 v-for="person in team" :key="person.name">
          <v-card flat class="text-xs-center ma-3">
            <v-responsive class="pt-4">
              image here
            </v-responsive>
            <v-card-text>
              <div class="subheading">{{ person.name }}</div>
              <div class="grey--text">{{ person.role }}</div>
            </v-card-text>
            <v-card-actions>
              <v-btn flat color="grey">
                <v-icon small left>message</v-icon>
                <span class="">Message</span>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-flex>
      </v-layout>

    </v-container>
    
  </div>
</template>

<script>
export default {
  data() {
    return {
      team: [
        { name: 'The Net Ninja', role: 'Web developer' },
        { name: 'Ryu', role: 'Graphic designer' },
        { name: 'Chun Li', role: 'Web developer' },
        { name: 'Gouken', role: 'Social media maverick' },
        { name: 'Yoshi', role: 'Sales guru'}
      ]
    }
  }
}
</script>
```

# 20. [Avatars](https://vuetifyjs.com/en/components/avatars#avatar)
- 이미지와 같은 asset은 `/public` 에서 기본적으로 서빙한다. 
- `v-avatar` 컴포넌트를 사용.

```html
<template>
  <div class="team">
    <h1 class="subheading grey--text">Team</h1>

    <v-container class="my-5">

      <v-layout row wrap>
        <v-flex xs12 sm6 md4 lg3 v-for="person in team" :key="person.name">
          <v-card flat class="text-xs-center ma-3">
            <v-responsive class="pt-4">
              <v-avatar size="100" class="grey lighten-2">
                <img :src="person.avatar">
              </v-avatar>
            </v-responsive>
            <v-card-text>
              <div class="subheading">{{ person.name }}</div>
              <div class="grey--text">{{ person.role }}</div>
            </v-card-text>
            <v-card-actions>
              <v-btn flat color="grey">
                <v-icon small left>message</v-icon>
                <span class="">Message</span>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-flex>
      </v-layout>

    </v-container>
    
  </div>
</template>

<script>
export default {
  data() {
    return {
      team: [
        { name: 'The Net Ninja', role: 'Web developer', avatar: '/avatar-1.png' },
        { name: 'Ryu', role: 'Graphic designer', avatar: '/avatar-2.png' },
        { name: 'Chun Li', role: 'Web developer', avatar: '/avatar-3.png' },
        { name: 'Gouken', role: 'Social media maverick', avatar: '/avatar-4.png' },
        { name: 'Yoshi', role: 'Sales guru', avatar: '/avatar-5.png'}
      ]
    }
  }
}
</script>
```
# 21. [Expansion Panels](https://vuetifyjs.com/en/components/expansion-panels#expansion-panel)
- 펼쳐지는 패널
- `Array.filter()`를 이용하여 데이터 필터링

```html
<template>
  <div class="team">
    <h1 class="subheading grey--text">Team</h1>

    <v-container class="my-5">

      <v-layout row wrap>
        <v-flex xs12 sm6 md4 lg3 v-for="person in team" :key="person.name">
          <v-card flat class="text-xs-center ma-3">
            <v-responsive class="pt-4">
              <v-avatar size="100" class="grey lighten-2">
                <img :src="person.avatar">
              </v-avatar>
            </v-responsive>
            <v-card-text>
              <div class="subheading">{{ person.name }}</div>
              <div class="grey--text">{{ person.role }}</div>
            </v-card-text>
            <v-card-actions>
              <v-btn flat color="grey">
                <v-icon small left>message</v-icon>
                <span class="">Message</span>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-flex>
      </v-layout>

    </v-container>
    
  </div>
</template>

<script>
export default {
  data() {
    return {
      team: [
        { name: 'The Net Ninja', role: 'Web developer', avatar: '/avatar-1.png' },
        { name: 'Ryu', role: 'Graphic designer', avatar: '/avatar-2.png' },
        { name: 'Chun Li', role: 'Web developer', avatar: '/avatar-3.png' },
        { name: 'Gouken', role: 'Social media maverick', avatar: '/avatar-4.png' },
        { name: 'Yoshi', role: 'Sales guru', avatar: '/avatar-5.png'}
      ]
    }
  }
}
</script>

```

# 22. [Menus](https://vuetifyjs.com/en/components/menus#menu)

- 드롭다운 형태의 메뉴
- `v-menu`의 메뉴 버튼을 가리게 되는데 `offset-y` 는 드롭다운이 아래로 펼쳐지도록 한다.

```html
 <!-- dropdown menu -->
  <v-menu offset-y>
    <v-btn flat slot="activator" color="grey">
      <v-icon left>expand_more</v-icon>
      <span>Menu</span>
    </v-btn>
    <v-list>
      <v-list-tile v-for="link in links" :key="link.text" router :to="link.route">
        <v-list-tile-title>{{ link.text }}</v-list-tile-title>
      </v-list-tile>
    </v-list>
  </v-menu>
```
# 23. [Popups](https://vuetifyjs.com/en/components/dialogs#dialog)
- `v-dialog`는 popup 형태를 제공하는 컴포넌트
- `v-btn` 은 팝업을 부르는 부모 컴포넌트에 nested 형태로 들어가는 것이 특징<sup>팝업을 부르는 버튼까지도 해당 컴포넌트에 구현.</sup>

```html
<template>
  <v-dialog max-width="600px">
    <v-btn flat slot="activator" class="success">Add New Project</v-btn>
    <v-card>
      <v-card-title>
        <h2>Add a New Project</h2>
      </v-card-title>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data() {
    return {
    }
  }
}
</script>
```

# 24. [Form Basics](https://vuetifyjs.com/en/components/forms#form)
- form 의 컴포넌트에 대한 내용은 너무 많아서 [공식 문서](https://vuetifyjs.com/en/components/text-fields)에서 확인 할 것.
- 특이한 건 없지만 `prepend-icon`에 material 아이콘을 넣을 수 있음.

```html
<template>
  <v-dialog max-width="600px">
    <v-btn flat slot="activator" class="success">Add New Project</v-btn>

    <v-card>
      <v-card-title>
        <h2>Add a New Project</h2>
      </v-card-title>

      <v-card-text>
        <v-form class="px-3">
          <v-text-field v-model="title" label="Title" prepend-icon="folder"></v-text-field>
          <v-textarea v-model="content" label="Information" prepend-icon="edit"></v-textarea>
          <v-spacer></v-spacer>
          <v-btn flat @click="submit" class="success mx-0 mt-3">Add Project</v-btn>
        </v-form>
      </v-card-text>
    </v-card>

  </v-dialog>
</template>

<script>
export default {
  data() {
    return {
      title: '',
      content: ''
    }
  },
  methods: {
    submit() {
      console.log(this.title, this.content)
    }
  }
}
</script>
```

# 25. [Datepickers](https://vuetifyjs.com/en/components/date-pickers#date-month-picker)
- Datepicker 를 이용하기 위해서는 `v-menu` 컴포넌트를 사용해 드랍다운처럼 구현.
- `v-date-picker` 의 @change 이벤트는 값이 변경되면 바로 해당 창을 닫기 위해 구현.
- 
```html
<template>
  <v-dialog max-width="600px">
    <v-btn flat slot="activator" class="success">Add New Project</v-btn>
    <v-card>
      <v-card-title>
        <h2>Add a New Project</h2>
      </v-card-title>
      <v-card-text>
        <v-form class="px-3">
          <v-text-field v-model="title" label="Title" prepend-icon="folder"></v-text-field>
          <v-textarea v-model="content" label="Information" prepend-icon="edit"></v-textarea>

          <v-menu v-model="menu" :close-on-content-click="false">
            <v-text-field slot="activator" :value="formattedDate" clearable label="Due date" prepend-icon="date_range"></v-text-field>
            <v-date-picker v-model="due" @change="menu = false"></v-date-picker>
          </v-menu>

          <v-spacer></v-spacer>

          <v-btn flat @click="submit" class="success mx-0 mt-3">Add Project</v-btn>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import format from 'date-fns/format'
export default {
  data() {
    return {
      title: '',
      content: '',
      due: null,
      menu: false
    }
  },
  methods: {
    submit() {
      console.log(this.title, this.content)
    }
  },
  computed: {
    formattedDate () {
      console.log(this.due)
      return this.due ? format(this.due, 'Do MMM YYYY') : ''
    }
  }
}
</script>
```













