package com.wjl.springbatch.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * @author wangJiaLun
 * @date 2019-12-11
 **/
public class MyChunkListener {

    @BeforeChunk
    public void beforeChunk(ChunkContext context){
        System.out.println(context.getStepContext().getStepName()+"before...");
    }

    @AfterChunk
    public void afterChunk(ChunkContext context){
        System.out.println(context.getStepContext().getStepName()+"after...");
    }
}
