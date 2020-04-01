package com.eceris.shard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Todo {

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String title;
    private boolean completed;

//    @CreatedDate
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdAt;

//    @PrePersist
//    public void init() {
//        this.createdAt = new Date();
//    }
}
