package kr.co.eceris.post;

import kr.co.eceris.post.Post.Command;
import kr.co.eceris.post.infra.ID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PostController {

    final private PostRepository repository;

    @GetMapping("/post/{id}")
    public Mono<Post> get(@PathVariable(value = "id") String id) {
        return repository.get(ID.fromString(id));
    }

    @GetMapping("/posts")
    public Flux<Post> list() {
        return repository.list();
    }

    @PostMapping("/post")
    public Mono<Post> create(@RequestBody Command command) {
        return repository.save(new Post(command.getTitle(), command.getContent()));
    }

    @PutMapping("/post")
    public Mono<Post> update(@RequestBody Command command) {
        return repository.get(ID.fromString(command.getId()))
                .flatMap(p -> {
                    p.setTitle(command.getTitle());
                    p.setContent(command.getContent());
                    return repository.save(p);
                });
    }

    @DeleteMapping("/post/{id}")
    public Mono<Void> delete(@PathVariable(value = "id") String id) {
        return repository.delete(ID.fromString(id));
    }
}
