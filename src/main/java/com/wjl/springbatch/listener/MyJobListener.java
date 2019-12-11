package com.wjl.springbatch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @author wangJiaLun
 * @date 2019-12-11
 **/
public class MyJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName()+"before...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName()+"after...");
    }
}
