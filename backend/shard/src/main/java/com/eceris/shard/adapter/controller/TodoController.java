package com.eceris.shard.adapter.controller;

import com.eceris.shard.domain.Todo;
import com.eceris.shard.domain.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TodoController {

    private final TodoRepository repository;


    @GetMapping("/master")
    public List<Todo> getMaster() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @GetMapping("/slave")
    public List<Todo> getSlave() {
        return repository.findAll();
    }

}
