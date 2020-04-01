package com.eceris.batch.application.Todo;

import com.eceris.batch.domain.Todo;
import com.eceris.batch.domain.TodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository repository;


    public List<TodoDto> get() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        Date today = new Date();
        return repository.get(yesterday, today);
    }

    public void save(List<Todo> todos) {
        repository.saveAll(todos);
    }

}
