package com.eceris.batch.application.Todo;

import com.eceris.batch.domain.Todo;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StepService {

    private final StepBuilderFactory stepBuilderFactory;
    private final TodoFeignService feignService;
    private final TodoService service;


    private static final List<String> NAMES = Lists.newArrayList("KOOKMINBANK", "SHINHANBANK", "KBANK");

    public List<Step> getSteps() {
        return NAMES.stream()
                .map(name -> stepBuilderFactory.get(name)
                        .tasklet((contribution, chunkContext) -> {
//                            log.info(">>>>> {} Step", name);
                            Todo todo1 = feignService.getTodo("1");
                            Todo todo2 = feignService.getTodo("2");
                            Todo todo3 = feignService.getTodo("3");
                            Todo todo4 = feignService.getTodo("4");
                            Todo todo5 = feignService.getTodo("5");
                            Todo todo6 = feignService.getTodo("6");
                            Todo todo7 = feignService.getTodo("7");
                            Todo todo8 = feignService.getTodo("8");

                            todo1.setBankCd("1");
                            todo2.setBankCd("1");
                            todo3.setBankCd("1");
                            todo4.setBankCd("1");
                            todo5.setBankCd("2");
                            todo6.setBankCd("2");
                            todo7.setBankCd("2");
                            todo8.setBankCd("2");


                            todo1.setUseType("A");
                            todo2.setUseType("A");
                            todo3.setUseType("B");
                            todo4.setUseType("B");
                            todo5.setUseType("C");
                            todo6.setUseType("C");
                            todo7.setUseType("D");
                            todo8.setUseType("D");

                            todo1.setCreatedAt(new Date());
                            todo2.setCreatedAt(new Date());
                            todo3.setCreatedAt(new Date());
                            todo4.setCreatedAt(new Date());
                            todo5.setCreatedAt(new Date());
                            todo6.setCreatedAt(new Date());
                            todo7.setCreatedAt(new Date());
                            todo8.setCreatedAt(new Date());
                            service.save(Lists.newArrayList(todo1, todo2, todo3, todo4, todo5, todo6, todo7, todo8));

                            log.info(">>>>> {}", String.join(", ", Lists.newArrayList(todo1.getTitle(), todo2.getTitle(), todo3.getTitle())));
                            return RepeatStatus.FINISHED;
                        })
                        .build())
                .collect(Collectors.toList());
    }
}

