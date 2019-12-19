package com.wjl.springbatch.restart;

import org.springframework.batch.item.*;

import java.util.Iterator;
import java.util.List;

/**
 * @author wangJiaLun
 * @date 2019-12-16
 **/
public class MyRestartReader implements ItemStreamReader<String> {

    private Iterator<String> iterator;

    public MyRestartReader(List<String> list) {
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

    /**
     *   step 执行前处理
     * @param executionContext
     * @throws ItemStreamException
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("open...");
    }

    /**
     *  chunk 处理完一批数据后触发
     * @param executionContext
     * @throws ItemStreamException
     */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("update...");
    }

    /**
     *   step 执行完之后触发
     * @throws ItemStreamException
     */
    @Override
    public void close() throws ItemStreamException {
        System.out.println("close...");
    }
}
