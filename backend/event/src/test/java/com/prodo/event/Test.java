package com.prodo.event;

import javax.validation.constraints.AssertTrue;
import java.util.Optional;
import java.util.regex.Pattern;

public class Test {

    @org.junit.jupiter.api.Test
    public void asdfasd() {

        String regexStr = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        String value1 = "moko23+-4@a.com";
        String value2 = "prodo.hyun@kakaobank.com";
        String value3 = "eceris.aaa.dffdd@naver.com";
        String value4 = "eceris.aaa@naver.co.kr.net.adf";
        String value5 = "eceris.aaa@a.a.co";
        String value6 = "mo.ko23+-4@a.com";
        String value7 = "mo.ko23+-4@a-.com";

        // username 부분

        System.out.println(value1.matches(regexStr));
        System.out.println(value2.matches(regexStr));
        System.out.println(value3.matches(regexStr));
        System.out.println(value4.matches(regexStr));
        System.out.println(value5.matches(regexStr));
        System.out.println(value6.matches(regexStr));
        System.out.println(value7.matches(regexStr));

    }


    public static final Pattern EMAIL_VALID_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @org.junit.jupiter.api.Test
    public void 이메일_정규식_검증() {
        String a = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        return "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{2,256}@";
//        String regex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{2,256}@";
        String regex =  "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{2,256}+@[a-zA-Z0-9\\-]{1,19}";

        String target1 = "prodo.aaa.dffdd@naver.commm";
        String target2 = "prodo@kakaobank.com";
        String target3 = "eceris.aaa.dffdd@naver.comm";
        String target4 = "eceris.aaa@naver.co.kr";
        String target5 = "eceris.aaa@a.o";
        String target6 = "eceris.aaa@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String target7 = "mo.ko%23+-4@a.com";
        String target8 = "mo.k_o23+-4@a-.com";
        String target9 = "prodo.aaa@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaaa";

        System.out.println(target1.matches(regex));
        System.out.println(target2.matches(regex));
        System.out.println(target3.matches(regex));
        System.out.println(target4.matches(regex));
        System.out.println(target5.matches(regex));
        System.out.println(target6.matches(regex));
        System.out.println(target7.matches(regex));
        System.out.println(target8.matches(regex));
        System.out.println(target9.matches(regex));


        Optional<String> empty = Optional.empty();
        Optional.ofNullable(empty).ifPresent(value -> {
            value.orElse(null);
            System.out.println("밸류가져옴");
        });
    }

    @org.junit.jupiter.api.Test
    public void asdf() {
        String regex =  "^(02|010|011|016|017|018|019|031|032|033|041|042|043|044|051|052|053|054|055|061|062|063|064|070|0505)\\d{7,8}$";

        System.out.println("010-6273-7510".matches(regex));
        System.out.println("010-62737510".matches(regex));
        System.out.println("01062737510".matches(regex));
        System.out.println("03062737510".matches(regex));
    }

}
