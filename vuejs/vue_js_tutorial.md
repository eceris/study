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
- 근데 데이터를 잃기 싫으면, `<keep-alive>` 태그로 감싸면 된다.

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










