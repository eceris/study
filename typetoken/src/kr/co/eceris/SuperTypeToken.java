package kr.co.eceris;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class SuperTypeToken {
    static class Super<T> {
        T value;
    }
    static class Sub extends Super<String> {
    }
    public static void main(String[] args) {
        Sub b = new Sub();
        Type t =  b.getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) t;
        System.out.println(p.getActualTypeArguments()[0]);
    }
}
