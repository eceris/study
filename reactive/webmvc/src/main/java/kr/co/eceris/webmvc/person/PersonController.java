package kr.co.eceris.webmvc.person;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@RestController
public class PersonController {

    public static final RestTemplate REST_TEMPLATE = new RestTemplate();
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(200, new CustomizableThreadFactory("worker"));
    @Autowired
    private PersonRepository repository;

    @GetMapping(value = "/file/{idx}")
    ResponseEntity<StreamingResponseBody> fileRead(@PathVariable String idx) throws IOException {
        log.info("webmvc file read count : {}", idx);
        BufferedReader br = new BufferedReader(new FileReader("/file.log"));

        StreamingResponseBody stream = out -> {
            br.lines().forEach((line) -> {
                try {
                    out.write(line.getBytes());
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            log.info("close file num : {}", idx);
        };
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(stream);
    }

    @GetMapping("/db/save/{id}")
    Person saveAll(@PathVariable String id) {
        String[] names = {"곽면순", "조도영", "류선영", "이상은", "김현우", "박성현"};
        return repository.save(new Person(id, names[Integer.valueOf(id) % 5]));
    }

    @GetMapping("/db/find/{idx}")
    List<Person> findAll(@PathVariable String idx) {
        log.info("webmvc findAll count : {}", idx);
        List<Person> all = repository.findAll();
        log.info("size : {}", all.size());
        return all;
    }

    @GetMapping("/rest/call/{idx}")
    String restCall(@PathVariable String idx) throws InterruptedException, ExecutionException {
        log.info("rest call count : {}", idx);

//        String url = "http://deploy.daouoffice.co.kr:9000/api/build/companies";
//        String url = "http://localhost:8082/rest/service/" + idx;
        String url = "http://mobile.terracetech.co.kr/api/login/config";
        Future<String> submit = EXECUTOR_SERVICE.submit(() -> REST_TEMPLATE.getForObject(url, String.class));
        return "Rest call / " + submit.get() + " : " + idx;
//        return REST_TEMPLATE.getForObject(url, String.class);
    }
}
