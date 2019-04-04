package kr.co.eceris.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Generic {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> r1 = genericMethod(list);
        List<?> r2 = wildcardMethod(list);

        System.out.println(r1);
        System.out.println(r2);
    }

    //T 타입파라미터를 사용하면 이 메소드에서 T를 사용하여 뭔가를 하겠다. 라는 의미임 ..
    static <T> List<T> genericMethod(List<T> list) {
        List<T> result = new ArrayList<T>();
        result.add(list.get(0));
        return result;
    }

    //와일드 카드는 T가 아닌 List에 관심이 있다는 것 ..ex list.size() 라던지 ...
    static List<?> wildcardMethod(List<?> list) {
        List result = Arrays.asList();
        result.add(list.get(0));
        return result;
    }

    interface a {
        default void method() {
            System.out.println();
        }
    }

    public static void test() {
        fn((Function & Serializable & a) p -> p);
    }

    public static <T extends Function & Serializable & a> void fn(T o) {
        o.method();
        o.apply(null);

    }


}
