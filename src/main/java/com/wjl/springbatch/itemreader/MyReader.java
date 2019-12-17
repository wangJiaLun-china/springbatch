package com.wjl.springbatch.itemreader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

/**
 * @author wangJiaLun
 * @date 2019-12-16
 **/
public class MyReader implements ItemReader<String> {

    private Iterator<String> iterator;

    public MyReader(List<String> list) {
        this.iterator = list.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        // 数据一个一个数据读
        if (iterator.hasNext()) {
            return this.iterator.next();
        }
        return null;
    }
}
