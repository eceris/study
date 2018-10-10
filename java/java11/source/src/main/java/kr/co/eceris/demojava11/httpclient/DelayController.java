package kr.co.eceris.demojava11.httpclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class DelayController {

    @GetMapping("/delay")
    public String delay(@RequestParam int seconds) throws InterruptedException {
        System.out.println("delaying " + seconds + " seconds on " + Thread.currentThread());
        TimeUnit.SECONDS.sleep(1);
        return "delayed " + seconds + " seconds";
    }
}
