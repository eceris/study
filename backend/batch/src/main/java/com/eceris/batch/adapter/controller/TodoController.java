package com.eceris.batch.adapter.controller;

import com.eceris.batch.application.Todo.TodoFeignService;
import com.eceris.batch.application.Todo.TodoService;
import com.eceris.batch.domain.Todo;
import com.eceris.batch.domain.TodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoFeignService feignService;

    private final TodoService service;

    @GetMapping("/test")
    public List<TodoDto> get() {
//        return feignService.getTodo("1");
        return service.
                get();
    }
}
