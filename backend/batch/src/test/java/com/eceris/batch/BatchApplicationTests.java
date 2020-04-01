package com.eceris.batch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class BatchApplicationTests {

    @Test
    void contextLoads() {

        LocalDate baseDateTime = LocalDate.parse("20200301", DateTimeFormatter.ofPattern("yyyyMMdd")); //어제
        System.out.println(baseDateTime);
        System.out.println(baseDateTime.toString());
        System.out.println(baseDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
//        LocalDateTime todayDateTime = LocalDateTime.parse("2020-03-01", DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(1); //오늘
    }

}
