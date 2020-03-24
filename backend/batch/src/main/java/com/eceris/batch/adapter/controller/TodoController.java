package com.eceris.batch.adapter.controller;

import com.eceris.batch.application.Todo.TodoFeignService;
import com.eceris.batch.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoFeignService service;

    @GetMapping("/test")
    public Todo get() {
        return service.getTodo("1");
    }
}
