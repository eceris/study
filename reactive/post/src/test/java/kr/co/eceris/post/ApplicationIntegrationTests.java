package kr.co.eceris.post;

import kr.co.eceris.post.Post.Command;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * webflux 로 구현된 서비스의 integration test 를 위한 클래스
 *
 * 참고 URL
 * https://goo.gl/qsYR7N
 * https://goo.gl/VCMYCH
 *
 * TODO : Integration test 인데, 각 테스트의 given 을 consumeWith 라는 메소드를 통해 구현하고 있다. 이것을 분리 할 방법을 찾을 것.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTests {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void 새로운_post를_만들어보자() {
        webClient.post()
                .uri("/post")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(new Command("title", "contents")))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 모든_post를_가져와보자() {
        webClient.get()
                .uri("/posts")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Post.class);
    }

    @Test
    public void 새로운_post를_생성하고_생성한_post를_가져와보자() {
        webClient.post()
                .uri("/post")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(new Command("title", "contents")))
                .exchange()
                .expectBody(Post.class)
                .consumeWith(createResult -> {
                    Post createResponse = createResult.getResponseBody();
                    webClient.get()
                            .uri("/post/" + createResponse.getId().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(Post.class)
                            .consumeWith(getResult -> {
                                Post getResponse = getResult.getResponseBody();
                                Assert.assertEquals("title", getResponse.getTitle());
                            });
                });
    }

    @Test
    public void 새로운_post를_생성하고_삭제해보자() {
        webClient.post()
                .uri("/post")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(new Command("title", "contents")))
                .exchange()
                .expectBody(Post.class)
                .consumeWith(result -> {
                    Post response = result.getResponseBody();
                    webClient.delete()
                            .uri("/post/" + response.getId().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(Post.class);
                });
    }

    @Test
    public void 새로운_post를_생성하고_업데이트해보자() {
        webClient.post()
                .uri("/post")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(new Command("title", "contents")))
                .exchange()
                .expectBody(Post.class)
                .consumeWith(createResult -> {
                    Post persisted = createResult.getResponseBody();
                    webClient.put()
                            .uri("/post")
                            .accept(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromObject(new Command(persisted.getId().toString(), "updated_title", "updated_contents")))
                            .exchange()
                            .expectBody(Post.class)
                            .consumeWith(postEntityExchangeResult -> {
                                Post result = postEntityExchangeResult.getResponseBody();
                                Assert.assertEquals("updated_title", result.getTitle());
                            });
                });
    }
}
