package com.eceris.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Todo {

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String title;
    private boolean completed;
    private String bankCd;
    private String useType;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Data
    public static class Req extends Todo {

    }

    @Data
    public static class Res extends Todo {
        private Long id;
    }

    @PrePersist
    public void init() {
        this.createdAt = new Date();
    }
}