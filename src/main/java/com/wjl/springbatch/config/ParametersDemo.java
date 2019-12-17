package com.wjl.springbatch.config;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author wangJiaLun
 * @date 2019-12-11
 **/
//@Configuration
//@EnableBatchProcessing
public class ParametersDemo implements StepExecutionListener {

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

    /**
     *  参数存储
     */
    private Map<String, JobParameter> parameterMap;

    @Bean
    public Job parameterJob(){
        return jobBuilderFactory.get("parameterJob")
                .start(parameterStep())
                .build();
    }

    /**
     *  Job 执行的是step, Job使用的数据肯定是在step中使用的
     *  所以只需要给step传递数据
     *   使用step级别的监听来传递数据
     * @return
     */
    @Bean
    public Step parameterStep() {
        return stepBuilderFactory.get("parameterStep")
                .listener(this)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        // 输出接收到的参数值
                        System.out.println(parameterMap.get("info"));
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        parameterMap = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
