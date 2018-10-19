package kr.co.eceris.post;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.PipedOutputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @Autowired
    private WebTestClient webClient;


    @Test
    public void 새로운_post를_만들어보자() throws Exception {
        Post body = new Post(null, "title", "contents");
        webClient.post()
                .uri("/post")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectStatus().isOk();
    }


//    @Test
//    public void 모든_post를_가져와보자() throws Exception {
//        webClient.get().uri("/api/posts").accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(Post.class)
//                .hasSize(2)
//                .contains(customerMap.get("Jack"), customerMap.get("Peter"));
//    }
}
