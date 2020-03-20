package com.eceris.batch.application.Todo;

import com.eceris.batch.domain.Todo;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StepService {

    private final StepBuilderFactory stepBuilderFactory;
    private final TodoService service;

    private static final List<String> NAMES = Lists.newArrayList("KOOKMINBANK", "SHINHANBANK", "KBANK");

    public List<Step> getSteps() {
        return NAMES.stream()
                .map(name -> stepBuilderFactory.get(name)
                        .tasklet((contribution, chunkContext) -> {
                            log.info(">>>>> {} Step", name);
                            Todo todo1 = service.getTodo("1");
                            Todo todo2 = service.getTodo("2");
                            Todo todo3 = service.getTodo("3");

                            log.info(">>>>> {}", String.join(", ", Lists.newArrayList(todo1.getTitle(), todo2.getTitle(), todo3.getTitle())));
                            return RepeatStatus.FINISHED;
                        })
                        .build())
                .collect(Collectors.toList());
    }
}

