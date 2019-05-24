# [Vue Test Utils](https://vue-test-utils.vuejs.org/guides/#getting-started)
vue 에서 제공하는 api 설명

## mount()
mounted 되거나 rendered 된 컴포넌트를 포함하는 `Wrapper` 인스턴스를 생성한다.
Creates a Wrapper that contains the mounted and rendered Vue component.

## shallowMount()
`mount` 와 같지만, stubbed 된 자식 컴포넌트도 포함된 `Wrapper` 를 생성한다.

## render()
객체를 문자열로 렌더링하고 [`cheerio`](https://github.com/cheeriojs/cheerio) 래퍼를 반환한다.

## renderToString()
구성 요소를 HTML로 렌더링합니다.

## Selectors
많은 메소드가 선택자를 인수로 사용합니다. 선택기는 CSS 선택자, Vue 구성 요소 또는 찾기 옵션 객체가 될 수 있습니다.

### CSS Selectors

Mount handles any valid CSS selector:

- tag selectors (div, foo, bar)
- class selectors (.foo, .bar)
- attribute selectors ([foo], [foo="bar"])
- id selectors (#foo, #bar)
- pseudo selectors (div:first-of-type)

You can also use combinators:

- direct descendant combinator (div > #bar > .foo)
- general descendant combinator (div #bar .foo)
- adjacent sibling selector (div + .foo)
- general sibling selector (div ~ .foo)

# [Jest](https://jestjs.io/docs/en/snapshot-testing)

Jest 를 선택한 이유
- [가장 완벽한 테스트러너이다.](https://vue-test-utils.vuejs.org/guides/#getting-started)
