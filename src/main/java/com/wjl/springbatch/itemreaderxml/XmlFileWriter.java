package com.wjl.springbatch.itemreaderxml;

import com.wjl.springbatch.model.AccessDemo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangJiaLun
 * @date 2019-12-17
 **/
@Component("xmlFileWriter")
public class XmlFileWriter implements ItemWriter<AccessDemo> {

    @Override
    public void write(List<? extends AccessDemo> items) throws Exception {
        for (AccessDemo item : items) {
            System.out.println(item+"...");
        }
    }
}
