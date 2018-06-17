package kr.co.eceris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
public class Step14 {

    @GetMapping("/event/{id}")
    public Mono<Event> hello(@PathVariable long id) {
        return Mono.just(new Event(id, "event" + id));
    }

    @GetMapping("/events/mono")
    public Mono<List<Event>> mono() {
        return Mono.just(Arrays.asList(new Event(1l, "event" + 1l), new Event(2l, "event" + 2l)));
    }

    @GetMapping("/events")
    public Flux<Event> events() {
        return Flux.just(new Event(1l, "event" + 1l), new Event(2l, "event" + 2l));
    }

    @GetMapping(value = "/events/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> stream() {
        List<Event> events = Arrays.asList(new Event(1l, "event" + 1l), new Event(2l, "event" + 2l));
        return Flux.fromIterable(events);
    }

    @GetMapping(value = "/events/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> streams() {
        Flux<Event> value1 = Flux.fromStream(Stream.generate(() -> new Event(System.currentTimeMillis(), "value"))).take(10).delayElements(Duration.ofSeconds(1));
        Flux<Event> value = Flux.<Event>generate(sink -> sink.next(new Event(System.currentTimeMillis(), "value"))).take(10).delayElements(Duration.ofSeconds(1));

        return value1;
    }


    public static void main(String[] args) {
        SpringApplication.run(Step14.class, "");
    }

    public static class Event {
        long id;
        String value;

        public Event(long id, String value) {
            this.id = id;
            this.value = value;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
