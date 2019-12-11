package com.wjl.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author wangJiaLun
 * @date 2019-12-11
 **/
//@Configuration
//@EnableBatchProcessing
public class NestedDemo {

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

    @Autowired
    private Job childJobOne;

    @Autowired
    private Job childJobTwo;

    /**
     *  启动对象
     */
    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job parentJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return jobBuilderFactory.get("parentJob")
                .start(childJobDemo1(jobRepository, transactionManager))
                .next(childJobDemo2(jobRepository, transactionManager))
                .build();
    }

    /**
     * @return 返回的是Job类型的step， 特殊的step
     */
    @Bean
    public Step childJobDemo1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childJobDemo1"))
                .job(childJobOne)
                // 使用父job的启动对象
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    /**
     * @return 返回的是Job类型的step， 特殊的step
     */
    @Bean
    public Step childJobDemo2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childJobDemo2"))
                .job(childJobTwo)
                // 使用父job的启动对象
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }
}
