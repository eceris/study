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











