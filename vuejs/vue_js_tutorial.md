youtube 의 [vue.js tutorial](https://www.youtube.com/playlist?list=PL4cUxeGkcC9gQcYgjhBoeQH7wiAyZNrYa)을 보고 기록한다.

# 27. [Slots](https://youtu.be/F44OoFk8spg)

- 부모 뷰의 마크업을 자식 뷰에 slot의 형태로 삽입해주는 디렉티브
- data는 부모 뷰를 참조하지만, 자식 뷰의 style scoped 를 사용하면 자식 뷰의 css를 참조

app.vue
```html
<template>
	<div>
		<form-helper>
			<h1 slot="title">{{title}}</h1>
			<p slot="text">I am the paragraph text for the slot!</p>
		</form-helper>
	</div>
</template>
<script>
import formHelper from './components/formHelper.vue'

export default {
	components : {
		'form-helper' : formHelper
	},
	data : function() {
		return {
			title : 'I am a dynamic slot title'
		};
	},
	methods : {

	}
}
</script>
```


formHelper.vue
```html
<template>
	<div>
		<slot name="title"></slot>
		<h1>I am the form helper</h1>
		<slot name="text"></slot>
	</div>
</template>

<script>
export default {
	components : {

	}, 
	data : function() {
		return {}
	},
	methods : function() {

	}
}
</script>

<style scoped>
</style>
```


# 28. [Dynamic Components](https://youtu.be/09n2945JW_0)
- 부모 뷰의 컴포넌트를 동적으로 지정
- 아래 코드의 button을 누르면 각 component가 보이는데, 데이터는 잃는다 왜냐면, component 가 destroy 되므로...

app.vue
```html
<template>
	<div>
		<component v-bind:is="component"></component>
		<button v-on:click="component = 'form-one' ">show component one!</button>
		<button v-on:click="component = 'form-two' ">show component two!</button>
	</div>
</template>
<script>
import formOne from './components/formOne.vue',
import formTwo from './components/formTwo.vue'

export default {
	components : {
		'form-one' : formOne,
		'form-two' : formTwo
	},
	data : function() {
		return {
			component : 'form-two'
		};
	},
	methods : {

	}
}
</script>
```

- 근데 데이터를 잃기 싫으면, `<keep-alive>` 태그로 감싸면 된다.
```html
<template>
	<div>
		<keep-alive>
			<component v-bind:is="component"></component>
		</keep-alive>
		<button v-on:click="component = 'form-one' ">show component one!</button>
		<button v-on:click="component = 'form-two' ">show component two!</button>
	</div>
</template>
```

# 29. Input Binding
# 30. Checkbox Binding
# 31. Select Box Binding
# 32. HTTP Requests
- 기본적으로 vue resource 를 기준으로 설명
- 아래에서 `.prevent`가 이벤트 전파를 방지. 
```html
<button v-on:click.prevent="post">add blog</button>
```

# 33. GET Requests
별 내용 없음

# 34. Custom Directives
- v-on, v-if, v-for, `v-`으로 시작하는건 모두 directives 
- directive 의 value를 직접 지정할 경우에는 ""로 감싸고 안에는 {}, [], '' 모두 지정할 수 있고, `binding.value` 로 접근 가능하다.
```html
<template>
<div v-theme:column="'wide'" id='show-blogs'>
<h2 v-rainbow> </h2>
</div>

</template>
```

```vue
Vue.directive('rainbow', {
	bind(el, binding, vnode){
		el.style.color = "#" + Math.random().toString().slice(2, 8);
	}
});

Vue.directive('theme', {
	bind(el, binding, vnode){
		if (binding.value == 'wide') {
			el.style.maxWidth= '1200px';
		} else if (binding.value == 'narrow') {
			el.style.maxWidth= '560px';
		}

		if (binding.arg == 'column') {
			el.style.background = '#ddd';
			el.style.padding = '20px';
		}
	}
});

new Vue({
	el :'#app',
	render : h => h(App)
});
```

# 35. Filters
- vue에서 간단하게 사용할 수 있게 제공하는 Filter, data 에 `|` 를 사용하여 filtering.

```html
<template>
	<div id="show-blogs">
		<div v-for"blog in blogs" class="single-blog">
			<h2>{{blog.title | to-uppercase}}</h2>
			<article>{{blog.body | snippet }}</article>
		</div>
	</div>
</template>
```
```vue

//Filters
Vue.filter('to-uppercase', function(value) {
	return value.toUpperCase();
});


Vue.filter('snippet', function(value) {
	return value.slice(0, 100) + '...';
});


new Vue({
	el :'#app',
	render : h => h(App)
});
```

# 36. Custom Search Filter
- 위의 필터로 사용해도 되지만 Array 형태의 데이터를 filtering 하기에는 성능의 문제가 있음 ... 그래서 아래와 같이 computed 를 사용하여 filtering..

```html
<template>
	<div id="show-blogs">
		<input type="text" v-model="search" placeholder="search blogs"/>
		<div v-for"blog in filteredBlogs" class="single-blog">
			<h2>{{blog.title | to-uppercase}}</h2>
			<article>{{blog.body | snippet }}</article>
		</div>
	</div>
</template>
<script>

export default {
	data() {
		return {
			data :[],
			search : ''
		}
	},
	created() {
		this.$http.get('https://jsonplaceholder.typeicode.com/posts')
		.then(function(data) {
			this.blogs = data.body.slice(0, 10);
		})
	},
	computed : {
		filteredBlogs : function() {
			return this.blogs.filter((blog) => {
				return blog.title.match(this.search);
			});
		}
	}
}

</script>

```
```vue

//Filters
Vue.filter('to-uppercase', function(value) {
	return value.toUpperCase();
});


Vue.filter('snippet', function(value) {
	return value.slice(0, 100) + '...';
});


new Vue({
	el :'#app',
	render : h => h(App)
});
```

# 37. Registring Things Locally
- 글로벌로 선언된 위의 예제들(Filter, directive)등을 locally 하게 선언하는 방법

```html
<script>
export default {
	data() {
		...
	},


	filters: {
		'to-uppercase' : function(value) {
			return value.toUpperCase();
		},
		toUppercase(value)) { //위와 똑같이 동작
			return value.toUpperCase();
		}
	},
	directives : {
		'rainbow' : {
			bind(el, binding, vnode) {
				el.style.color = "#" + Math.random().toString().slice(2, 8);
			}
		}
	}
}

</script>
```

# 38. Mixins

- 모든 컴포넌트에 같은 동작을 하는 computed method 같은 property를 모아놓고  믹스인해서 사용하는 방법

- property 의 집합이다. 미리 선언해놓고 import 해서 사용하는 방법

```html
//mixin/searchMixin.js
export default {
	computed : {
		filteredBlogs : function() {
			return this.blogs.filter((blog) => {
				return blog.title.match(this.search);
			});
		}
	}
}
```

아래와 같이 mixins property에 array 형태로 import된 mixin을 삽입해주면 마치 해당 vue 파일에 존재하는 computed 메소드를 사용하는 것과 동일하게 동작한다.

```vue
// component/listBolg.vue
import searchMixin from '../mixin/searchMixin'

<script>
export default {
	...
	...

	mixins: [searchMixin]
}
</script>
```

# 39. Setting up Routing 
- router.js 파일을 따로 분리하여 관리하고 main.js 에서 import 하여 설정한다.

```html
// router.js

import showBlogs from './components/showBlogs.vue'
import addBlogs from './components/addBlogs.vue'

export default [
	{ path: '/', component: showBlogs },
	{ path: '/add', component: addBlogs }
]
```

```html
// main.js

...
import Routes from './routes'

Vue.use(VueRouter);

const router = new VueRouter({
	routes: Routes	
})

new Vue({
	el:'#app',
	render: h=> h(App),
	router: router
})
```

# 40. Routing Mode(Hash vs History)
- hash Thing 은 url 이 clean 하게 보이지 않게 한다. 
- # 을 통한 요청은 실제로 추가 요청을 만들지 않음.
- # 은 사실 `<a>` 로 페이지의 다른 뷰를 보여주는 것일 뿐이다.
- url 에 # 을 지우고 싶다면, router 의 mode 를 history로 하도록 한다.


# 41. Adding Router Links
- `router-link` 디렉티브를 사용하여 routing 한다.
- 디렉티브에 `exact` 를 사용하면, 실제 url 의 contain이 아닌 match 일 경우만 표현 한다.

```html
<template>
	<div>
		<nav>
			<ul>
				<li>
					<router-link to="/" exact>Blog</router-link>
				</li>
				<li>
					<router-link to="/" exact>Blog</router-link>
				</li>
			<ul>
		<nav>
	</div>
</template>
```

# 42. Route Parameters
localhost:8080:/blog/:id -> Route Parameter 
{ path:'/blog/:id', component: singleBlog } 

- 보통은 created 에서 route.params.id 로 아래와 같이 data binding 하는 것 같음.
```html
<script>
export default {
	data() {
		return {
			id : this.$route.params.id,
			blog:{}
		}
	},
	created() {
		this.$http.get('http://jsonplaceholder.typicode.com/posts/' + this.id)
		.then(function(data) {
			console.log(data);
			this.blog = data.body;
		})
	}
}
</script>
```

# 43. Posting to Firebase
- [firebase](https://firebase.google.com) 를 이용하여 간단하게 데이터를 쓰기 읽기 가능.
- 

# 44. Retrieving Posts from Firebase
- data.json() 는 promise 객체이다. 그래서 `.then`으로 데이터를 받아서 표현.

```html
<script>
export default {
	created() {
		this.$http.get('https://abscd.com/posts.json')
		.then(function(data) {
			return data.json();
		})
		.then(function(data) {
			var blogsArray = [];
			for (let key in data) {
				//console.log(data[key]);
				data[key].id = key
				blogsArray.push(data[key]);
			}
			//console.log(blogsArray);

			this.blogs = blogsArray;
		})
	}
}
</script>
```



# 1. Vue CLI Introduction

## Creating a Project
old cli: 
	vue init webpack-simple MYAPP 

new cli: 
	vue create MYAPP 

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
$ node -v 
$ npm uninstall -g vue-cli // vue cli 구버전 삭제
$ npm install -g  @vue/cli // vue cli 신버전 설치 vue scope의 cli 설치
$ vue create MYAPP // vue cli 로 project 생성

- babel.config.js 에 presets 목록에 @vue/app 이 기본으로 세팅되는데 이것은
babel은 아직 지원하지 않는 새로운 기능들이나 polyfills 를 자동으로 지원해줌.


# 3. Dev Server
- package.js 에 보면 @vue/cli-service 가 보이는데, 이것은 vue cli service 를 로컬에 설치해서 lint 나 이런것들을 자유롭게 사용하도록 도와줌.
$ npm run serve 

# 4. Custom Presets
- $ vue create 할 때 옵션을 지정




































