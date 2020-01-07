package com.wjl.springbatch.itemwriter;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangJiaLun
 * @date 2019-12-19
 **/
@Component("itemWriterDemo")
public class ItemWriterDemo implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {
        System.out.println(items.size());
        for (String item : items) {
            System.out.println(item);
        }
    }
}
