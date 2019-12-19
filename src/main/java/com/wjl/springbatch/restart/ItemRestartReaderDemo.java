package com.wjl.springbatch.restart;

import com.wjl.springbatch.itemreader.MyReader;
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
@Configuration
@EnableBatchProcessing
public class ItemRestartReaderDemo {

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
    public Job itemRestartReaderDemoJob(){
        return jobBuilderFactory.get("itemRestartReaderDemoJob")
                .start(itemRestartReaderDemoStep())
                .build();
    }

    @Bean
    public Step itemRestartReaderDemoStep() {
        return stepBuilderFactory.get("itemRestartReaderDemoStep")
                .<String, String>chunk(2)
                .reader(itemRestartReaderDemoRead())
                .writer(list ->{
                    for (String item : list) {
                        System.out.println(item + "...");
                    }
                })
                .build();
    }

    @Bean
    public MyRestartReader itemRestartReaderDemoRead() {
        List<String> data = Arrays.asList("cat", "dog", "pig", "duck");
        return new MyRestartReader(data);
    }
}
