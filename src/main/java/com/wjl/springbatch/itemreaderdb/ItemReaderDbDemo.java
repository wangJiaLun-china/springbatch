package com.wjl.springbatch.itemreaderdb;

import com.wjl.springbatch.model.AccessDemo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangJiaLun
 * @date 2019-12-16
 **/
//@Configuration
//@EnableBatchProcessing
public class ItemReaderDbDemo {

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
    private DataSource dataSource;

    @Autowired
    @Qualifier("dbJdbcWriter")
    private ItemWriter<AccessDemo> dbJdbcWriter;

    @Bean
    public Job itemReaderDbJob(){
        return jobBuilderFactory.get("itemReaderDbJob")
                .start(itemReaderDbStep())
                .build();
    }

    @Bean
    public Step itemReaderDbStep() {
        return stepBuilderFactory.get("itemReaderDbStep")
                .<AccessDemo, AccessDemo>chunk(10)
                .reader(dbJdbcReader())
                .writer(dbJdbcWriter)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<AccessDemo> dbJdbcReader() {
        JdbcPagingItemReader<AccessDemo> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(10);
        // 把读取到的记录转换成AccessDemo对象
        reader.setRowMapper(new RowMapper<AccessDemo>() {
            @Override
            public AccessDemo mapRow(ResultSet resultSet, int rows) throws SQLException {
                AccessDemo accessDemo = new AccessDemo();
                accessDemo.setId(resultSet.getInt(1));
                accessDemo.setUsername(resultSet.getString(2));
                accessDemo.setShopName(resultSet.getString(3));
                return accessDemo;
            }
        });
        // 指定sql语句
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id, username, shop_name");
        provider.setFromClause("from access");
        // 指定根据哪个字段排序
        Map<String, Order> sort = new HashMap<>(1);
        sort.put("id", Order.ASCENDING);
        provider.setSortKeys(sort);
        reader.setQueryProvider(provider);
        return reader;
    }
}
