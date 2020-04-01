package com.eceris.batch.application.Todo;

import com.eceris.batch.domain.Todo;
import com.eceris.batch.domain.TodoDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {

    @Query(value = "SELECT " +
            "    new com.eceris.batch.domain.TodoDto(COUNT(t), t.bankCd, t.useType) " +
            "FROM " +
            "    Todo t " +
            "WHERE t.createdAt BETWEEN :from and :to and t.completed = true " +
            "GROUP BY " +
            "    t.bankCd, t.useType")
    List<TodoDto> get(Date from, Date to);
}

