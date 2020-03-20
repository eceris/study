package com.eceris.batch.config;

import com.eceris.batch.application.Todo.StepService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음
    private final StepService stepService;

//    @Bean
//    public Job simpleJob() {
//        return jobBuilderFactory.get("simpleJob")
//                .start(simpleStep1())
//                .build();
//    }
//
//    public Step simpleStep1() {
//        return stepBuilderFactory.get("simpleStep1")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info(">>>>> This is Step1");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }


    @Bean
    public Job job() {
        SimpleJobBuilder jobBuilder = jobBuilderFactory.get("prodoJob")
                .incrementer(new RunIdIncrementer())
                .start(stepBuilderFactory.get("prodoStartStep")
                        .tasklet((contribution, chunkContext) -> {
                            log.info(">>>>> Start Step >>>>>");
                            return RepeatStatus.FINISHED;
                        })
                        .build());
        stepService.getSteps()
                .forEach(step -> jobBuilder.next(step));
        return jobBuilder.build();
    }

}
