package kr.co.eceris.webflux.person;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.BaseStream;

@Slf4j
@RestController
public class PersonController {

    static final WebClient.Builder WEBCLIENT_BUILDER = WebClient.builder();

    @Autowired
    private PersonRepository repository;

    @GetMapping(value = "/file/{idx}")
    Flux<String> fileRead(@PathVariable String idx) {
        log.info("webflux file read count : {}", idx);
        Path path = Paths.get("/file.log");
        return Flux.using(() -> Files.lines(path), Flux::fromStream, BaseStream::close);
    }

    @GetMapping("/db/save/{id}")
    Mono<Person> saveAll(@PathVariable String id) {
        String[] names = {"곽면순", "조도영", "류선영", "이상은", "김현우", "박성현"};
        return repository.save(new Person(id, names[Integer.valueOf(id) % 5]));
    }
    @GetMapping("/db/find/{idx}")
    Flux<Person> findAll(@PathVariable String idx) {
        log.info("webflux findAll count : {}", idx);
        return repository.findAll();
    }

    @GetMapping("/rest/call/{idx}")
    Mono<String> restCall(@PathVariable String idx) throws InterruptedException, ExecutionException {
        log.info("rest call count : {}", idx);

        //URL을 로컬로 지정할 경우 hang..
//        String url = "http://deploy.daouoffice.co.kr:9000/api/build/companies";
        String url = "http://mobile.terracetech.co.kr/api/login/config";
        return WEBCLIENT_BUILDER.baseUrl(url).build().get().retrieve().bodyToMono(String.class);
    }

}
