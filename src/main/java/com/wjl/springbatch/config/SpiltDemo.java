package com.wjl.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * @author wangJiaLun
 * @date 2019-12-11
 **/
//@Configuration
//@EnableBatchProcessing
public class SpiltDemo {

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
    public Step spiltDemoStep1(){
        return stepBuilderFactory.get("spiltDemoStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("spiltDemoStep1");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step spiltDemoStep2(){
        return stepBuilderFactory.get("spiltDemoStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("spiltDemoStep2");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step spiltDemoStep3(){
        return stepBuilderFactory.get("spiltDemoStep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("spiltDemoStep3");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Flow splitDemoFlow1(){
        return new FlowBuilder<Flow>("splitDemoFlow1")
                .start(spiltDemoStep1())
                .build();
    }

    @Bean
    public Flow splitDemoFlow2(){
        return new FlowBuilder<Flow>("splitDemoFlow2")
                .start(spiltDemoStep2())
                .next(spiltDemoStep3())
                .build();
    }

    @Bean
    public Job splitDemoJob(){
        return jobBuilderFactory.get("splitDemoJob")
                .start(splitDemoFlow1())
                .split(new SimpleAsyncTaskExecutor()).add(splitDemoFlow2())
                .end()
                .build();
    }
}
