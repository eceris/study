package com.eceris.batch.application.Todo;

import com.eceris.batch.domain.Todo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "todoA",
        url = "https://jsonplaceholder.typicode.com",
        configuration = {FeignClientsConfiguration.class})
public interface TodoFeignService {

    @GetMapping("/todos/{id}")
    Todo getTodo(@PathVariable(value = "id") String id);

}
