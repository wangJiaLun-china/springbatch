package com.wjl.springbatch.config;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
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
public class DeciderDemo {

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
    public Step deciderDemoStep1(){
        return stepBuilderFactory.get("deciderDemoStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("deciderDemoStep1");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step deciderDemoStep2(){
        return stepBuilderFactory.get("deciderDemoStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("even");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step deciderDemoStep3(){
        return stepBuilderFactory.get("deciderDemoStep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("odd");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    /**
     *  决策器
     */
    @Bean
    public JobExecutionDecider myDecider(){
        return new MyDecider();
    }

    class MyDecider implements JobExecutionDecider{

        private int count;
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            count ++;
            if (count % 2 == 0) {
                return new FlowExecutionStatus("even");
            }else {
                return new FlowExecutionStatus("odd");
            }
        }
    }

    @Bean
    public Job deciderDemoJob(){
        return jobBuilderFactory.get("deciderDemoJob")
                .start(deciderDemoStep1())
                .next(myDecider())
                .from(myDecider()).on("even").to(deciderDemoStep2())
                .from(myDecider()).on("odd").to(deciderDemoStep3())
                .from(deciderDemoStep3()).on("*").to(myDecider())
                .end()
                .build();
    }
}
