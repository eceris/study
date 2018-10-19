package kr.co.eceris.post;

import kr.co.eceris.post.infra.ID;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        Post other = (Post) o;
        if (this.getId() == null ? other.getId() != null : !this.getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Command implements Serializable  {
        private String id;
        private String title;
        private String content;
    }
}
