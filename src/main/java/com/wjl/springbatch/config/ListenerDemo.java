package com.wjl.springbatch.config;

import com.wjl.springbatch.listener.MyChunkListener;
import com.wjl.springbatch.listener.MyJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author wangJiaLun
 * @date 2019-12-11
 **/
//@Configuration
//@EnableBatchProcessing
public class ListenerDemo {

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
    public Job listenerJob(){
        return jobBuilderFactory.get("listenerJob")
                .start(step1())
                .listener(new MyJobListener())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                // 每次读取到 chunk(*) * 次 做输出处理
                .<String,String>chunk(2)
                // 容错
                .faultTolerant()
                .listener(new MyChunkListener())
                .reader(read())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemReader<String> read() {
        return new ListItemReader<>(Arrays.asList("java", "spring", "mybatis"));
    }

    @Bean
    public ItemWriter<String> writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                for (String item: items){
                    System.out.println(item);
                }
            }
        };
    }
}
