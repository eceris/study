package com.eceris.batch.application.Todo;

import com.eceris.batch.domain.Todo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {


    public int save(List<Todo> todos) {
        return todos.size();

    }

}
