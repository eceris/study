package kr.co.eceris.post;

import kr.co.eceris.post.infra.ID;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Post implements Serializable {

    private ID id;
    private String title;
    private String content;

    public Post(String title, String content) {
        this.id = ID.newID();
        this.title = title;
        this.content = content;
    }


    @Data
    @AllArgsConstructor
    static class Command implements Serializable  {
        private String id;
        private String title;
        private String content;

        public Command(String title, String content) {
            this.title = title;
            this.content = content;
        }

    }
}
