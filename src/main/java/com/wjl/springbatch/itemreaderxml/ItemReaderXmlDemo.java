package com.wjl.springbatch.itemreaderxml;

import com.wjl.springbatch.model.AccessDemo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangJiaLun
 * @date 2019-12-17
 **/
//@Configuration
//@EnableBatchProcessing
public class ItemReaderXmlDemo {

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
    @Qualifier("xmlFileWriter")
    private ItemWriter<AccessDemo> xmlFileWriter;

    @Bean
    public Job xmlItemReaderDemoJob(){
        return jobBuilderFactory.get("xmlItemReaderDemoJob")
                .start(xmlItemReaderDemoStep())
                .build();
    }

    @Bean
    public Step xmlItemReaderDemoStep() {
        return stepBuilderFactory.get("xmlItemReaderDemoStep")
                .<AccessDemo, AccessDemo>chunk(2)
                .reader(xmlFileReader())
                .writer(xmlFileWriter)
                .build();
    }

    @Bean
    @StepScope
    public StaxEventItemReader<? extends AccessDemo> xmlFileReader() {
        StaxEventItemReader<AccessDemo> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("/metadata/accessdemo.xml"));
        // 指定需要处理的根标签
        reader.setFragmentRootElementName("accessdemo");
        // 把xml转成对象
        XStreamMarshaller unmarshaller = new XStreamMarshaller();
        Map<String, Class> map = new HashMap<>(16);
        map.put("accessdemo", AccessDemo.class);
        unmarshaller.setAliases(map);
        reader.setUnmarshaller(unmarshaller);
        return reader;
    }
}
