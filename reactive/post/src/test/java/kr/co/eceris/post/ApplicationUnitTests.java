package kr.co.eceris.post;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
public class ApplicationUnitTests {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void test() {
        Mono<Post> save = postRepository.save(new Post("test", "test"));

        StepVerifier.create(save)
                .expectNext()
                .assertNext(post -> post.getContent().equals("test"))
                .verifyComplete();
    }

}
