package kr.co.eceris.webflux.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NonBlockTest {

    private static String HOST = "http://localhost:8082";

    @Test
    public void file() throws InterruptedException {
        log.info("start NonBlocking file read");
        TestExecutor run = TestExecutor.create(HOST + TestConstant.API_FILE_URI, 200).run();
        while(!run.isDone()) {
            log.info("ing... {}{}", System.lineSeparator(), run);
            TimeUnit.SECONDS.sleep(1);
        }
        log.info("end NonBlocking file read : {}", run);
    }

    @Test
    public void db() throws InterruptedException {
        log.info("start NonBlocking db select");
        TestExecutor run = TestExecutor.create(HOST + TestConstant.API_DB_FIND_URI, 1000).run();
        while(!run.isDone()) {
            log.info("ing... {}{}", System.lineSeparator(), run);
            TimeUnit.SECONDS.sleep(1);
        }
        log.info("end NonBlocking db select : {}", run);
    }

    @Test
    public void rest() throws InterruptedException {
        log.info("start NonBlocking rest call");
        TestExecutor run = TestExecutor.create(HOST + TestConstant.API_REST_URI, 1000).run();
        while (!run.isDone()) {
            log.info("ing... {}{}", System.lineSeparator(), run);
            TimeUnit.SECONDS.sleep(1);
        }
        log.info("end NonBlocking rest call : {}", run);
    }
}
