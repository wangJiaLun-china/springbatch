//package com.wjl.springbatch.itemwriterdb;
//
//import com.wjl.springbatch.model.AccessDemo;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.mapping.FieldSetMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.batch.item.file.transform.FieldSet;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.validation.BindException;
//
///**
// *  读取文件输出到数据库
// * @author wangJiaLun
// * @date 2019-12-19
// **/
//@Configuration
//@EnableBatchProcessing
//public class ItemWriterDbDemo {
//
//    /**
//     *  注入创建任务对象的对象
//     */
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//
//    /**
//     *  注入创建Step对象的对象
//     */
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//
//    @Bean
//    public Job itemWriterDbDemoJob(){
//        return jobBuilderFactory.get("itemWriterDbDemoJob")
//                .start(itemWriterDbDemoStep())
//                .build();
//    }
//
//    @Bean
//    public  Step itemWriterDbDemoStep() {
//        return stepBuilderFactory.get("itemWriterDbDemoStep")
//                .<AccessDemo, AccessDemo>chunk(2)
//                .reader(flatFileReader())
//                .writer(itemWriterDb())
//                .build();
//    }
//
//    @Bean
//    @StepScope
////    private JdbcBatchItemWriter<AccessDemo> itemWriterDb() {
////    }
//
//    @Bean
//    @StepScope
//    public FlatFileItemReader<AccessDemo> flatFileReader() {
//        FlatFileItemReader<AccessDemo> reader = new FlatFileItemReader<>();
//        reader.setResource(new ClassPathResource("/metadata/accessdemo.txt"));
//        // 跳过第1行
//        reader.setLinesToSkip(1);
//        // 解析数据
//        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//        tokenizer.setNames(new String[]{"id", "username"});
//        // 解析出来的数据单行映射为对象
//        DefaultLineMapper<AccessDemo> demoDefaultLineMapper = new DefaultLineMapper<>();
//        demoDefaultLineMapper.setLineTokenizer(tokenizer);
//        demoDefaultLineMapper.setFieldSetMapper(new FieldSetMapper<AccessDemo>() {
//            @Override
//            public AccessDemo mapFieldSet(FieldSet fieldSet) throws BindException {
//                AccessDemo accessDemo = new AccessDemo();
//                accessDemo.setId(fieldSet.readInt("id"));
//                accessDemo.setUsername(fieldSet.readString("username"));
//                return accessDemo;
//            }
//        });
//        demoDefaultLineMapper.afterPropertiesSet();
//        reader.setLineMapper(demoDefaultLineMapper);
//        return reader;
//    }
//}
