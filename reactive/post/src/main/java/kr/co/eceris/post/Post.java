package kr.co.eceris.post;

import kr.co.eceris.post.infra.ID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Post implements Serializable {
    @Setter
    private ID id;
    private String title;
    private String content;

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
}
