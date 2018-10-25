package kr.co.eceris.post;

import kr.co.eceris.post.infra.ID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PostRepository {

    private final Map<ID, Post> persistMap = new ConcurrentHashMap<>();

    public Mono<Post> save(Post post) {
        persistMap.put(post.getId(), post);
        return Mono.just(post);
    }

    public Mono<Post> get(ID id) {
        return Mono.just(persistMap.get(id));
    }

    public Flux<Post> list() {
        return Flux.fromIterable(persistMap.values());
    }

    public Mono<Void> delete(ID id) {
        persistMap.remove(id);
        return Mono.empty();
    }
}