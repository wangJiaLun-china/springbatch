package com.wjl.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangJiaLun
 * @date 2019-12-11
 **/
//@Configuration
//@EnableBatchProcessing
public class JobDemo {

    /**
     *  注入创建任务对象的对象
     */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /**
     *  注入创建Step对象的对象
     */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobDemoJob(){
        return jobBuilderFactory.get("jobDemoJob")
                // next 顺序执行
/*                .start(step1())
                .next(step2())
                .next(step3())*/
                // on 指定条件  to 到达什么步骤 from 从哪个步骤开始 fail 失败 stopAndRestart 停止重启某步骤
                .start(step1()).on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").to(step3())
                .from(step3()).end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step2");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step3");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
