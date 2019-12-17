package com.wjl.springbatch.itemreader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author wangJiaLun
 * @date 2019-12-16
 **/
//@Configuration
//@EnableBatchProcessing
public class ItemReaderDemo {

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
    public Job itemReaderDemoJob(){
        return jobBuilderFactory.get("itemReaderDemoJob")
                .start(itemReaderDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderDemoStep() {
        return stepBuilderFactory.get("itemReaderDemoStep")
                .<String, String>chunk(2)
                .reader(itemReaderDemoRead())
                .writer(list ->{
                    for (String item : list) {
                        System.out.println(item + "...");
                    }
                })
                .build();
    }

    @Bean
    public MyReader itemReaderDemoRead() {
        List<String> data = Arrays.asList("cat", "dog", "pig", "duck");
        return new MyReader(data);
    }
}
