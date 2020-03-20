package com.eceris.batch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Todo {
    private Long userId;
    private String title;
    private boolean completed;

    @Data
    public static class Req extends Todo {

    }

    @Data
    public static class Res extends Todo {
        private Long id;
    }
}