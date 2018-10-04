제네릭으로 선언된 타입을 리플렉션으로도 가져올 수없다

예를 들면 new ArrayList<String>(); 이 코드는 런타임 시점에 TypeErasure 작업으로 타입 정보를 잃어버림.
실제로는 new ArrayList()로 바뀜.....

근데 만약 이것을 
List<String> s = new ArrayList<String>();
Type t =  s.getClass().getGenericSuperClass();
ParameterizedType p = (ParameterizedType) t;
p.getActuralTypeArguments()[0]
이렇게 하면 String을 가져온다.<sup>TypeErasure작업이 일어나지 않음</sup>