[TOC]

## Spring Batch 基本介绍

首先，Spring Batch运行的基本单位是一个Job，一个Job就做一件批处理的事情。一个Job包含很多Step，step就是每个job要执行的单个步骤。如下图所示，Step里面，会有Tasklet，Tasklet是一个任务单元，它是属于可以重复利用的东西。

然后是Chunk，chunk就是数据块，你需要定义多大的数据量是一个chunk。Chunk里面就是不断循环的一个流程，读数据，处理数据，然后写数据。Spring Batch会不断的循环这个流程，直到批处理数据完成。

[官方手册]: https://docs.spring.io/spring-batch/docs/4.0.x/reference/html/index.html



## Spring Batch 功能

- 批量处理
- 日志跟踪
- 事务管理
- 作业重启
- 跳过和资源管理
- 分割技术缩放弹簧批量应用程序

## Spring Batch 特点

- 灵活性
  - Spring批处理应用程序非常灵活。只需更改XML文件即可更改应用程序中的处理顺序。
- 可维护性
  - Spring批量应用程序易于维护。 Spring Batch作业包括步骤，每个步骤都可以进行分离，测试和更新，而不影响其他步骤。
- 可伸缩性
  - 使用分区技术，可以缩放Spring Batch应用程序。
  - 并行执行作业的步骤。
  - 并行执行单个线程。
- 可靠性
  - 如果发生任何故障，可以通过拆除步骤来从停止的地方重新开始作业。
- 支持多种文件格式
  - Spring Batch为XML，Flat文件，CSV，MYSQL，Hibernate，JDBC，Mongo，Neo4j等大量写入器和读取器提供支持。
- 多种启动作业的方式
  - 可以使用Web应用程序，Java程序，命令行等来启动Spring Batch作业。

### 环境搭建

- pom.xml配置


- ```
  <!-- Spring Batch dependencies -->
  	<dependency>
          <groupId>org.springframework.batch</groupId>
          <artifactId>spring-batch-core</artifactId>
          <version>${spring.batch.version}</version>
  	</dependency>

  	<dependency>
          <groupId>org.springframework.batch</groupId>
          <artifactId>spring-batch-infrastructure</artifactId>
          <version>${spring.batch.version}</version>
  	</dependency>  
  ```

## Spring Batch 架构

- 应用程序 - 此组件包含我们使用Spring Batch 框架编写的所有作业和代码
- 批处理核心 - 此组件包含控制和启动批处理作业所需的所有API类
- 批处理基础结构 - 此组件包含应用程序和批处理核心组件使用的读取器, 编写器, 服务


## Spring Batch 组件及连接

``` mermaid
graph LR
jobLauncher[Job Launcher] --- job[Job] 
job[Job]  --- step[Step]
jobRepository[Job Repository] --- jobLauncher[Job Launcher]
jobRepository[Job Repository] --- job[Job]
jobRepository[Job Repository] --- step[Step] 
step[Step] --- itemReader[Item Reader]
step[Step] --- iterProcess[Item Process]
step[Step] --- iterWriter[Item Writer]
```



- **Job** - 在Spring Batch应用程序中，作业是要执行的批处理。它从头到尾不间断地运行。此作业进一步分为步骤（或作业包含步骤）。

  - 我们将使用XML文件或 Java类在Spring Batch中配置作业。以下是Spring Batch中作业的XML配置。

    ``` xml
    <job id = "jobid">
       <step id = "step1" next = "step2"/>
       <step id = "step2" next = "step3"/>
       <step id = "step3"/>
    </job>
    ```

    在`<job>` `</ job>`标记内配置批处理作业。它有一个名为 **id** 的属性。在这些标签中，我们定义了步骤的定义和顺序。

- **Restartable** - 通常，当一个作业正在运行时，我们尝试再次启动它，这被认为是 **restart** ，它将再次启动。为避免这种情况，您需要将 **restartable** 值设置为 **false** ，如下所示。

  ``` xml
  <job id = "jobid" restartable = "false" >

  </job>
  ```

- **Step** - 是其中包含必要的信息以定义并执行作业（其一部分）的作业的一个独立部分。

  如图中所示，每个步骤由ItemReader，ItemProcessor（可选）和ItemWriter组成。

  - **Reader** -  一个 **item reader** 数据读入来自特定源的一个Spring批量应用，
  - **Writer** - 一个 **item writer** 从Spring批处理应用到特定目的地写入数据。
  - **processor**  -  一个 **Item processor** 是包含其处理读入弹簧批次的数据的处理代码的类。如果应用程序读取 **“n”** 条记录，则处理器中的代码将在每条记录上执行。

  当没有给出读写器时， **tasklet** 充当SpringBatch的处理器。它只处理一个任务。例如，如果我们正在编写一个简单的步骤，我们从MySQL数据库读取数据并处理它并将其写入文件（平面），那么我们的步骤使用
  - 一个 **reader** 从MySQL数据库中读取。

  - 一个 **writer** ，其写入到一个平面文件。

  - 一个 **custom processor** ，按照我们的愿望处理数据。

  - ```xml
    <job id = "helloWorldJob">
       <step id = "step1">
          <tasklet>
             <chunk reader = "mysqlReader" writer = "fileWriter"
                processor = "CustomitemProcessor" ></chunk>
          </tasklet>
       </step>
    </ job>
    ```

- **JobRepository** Spring Batch中的作业存储库为JobLauncher，Job和Step实现提供创建，检索，更新和删除（CRUD）操作。我们将在XML文件中定义一个作业存储库，如下所示。

  ```xml
  <job-repository id = "jobRepository"
       data-source = "dataSource"
       transaction-manager = "transactionManager"
       isolation-level-for-create = "SERIALIZABLE"
       table-prefix = "BATCH_"
       max-varchar-length = "1000"/>
  ```

- #### **JobLauncher** 

  JobLauncher是一个使用 **给定参数集** 启动Spring Batch作业的接口。 **SampleJoblauncher** 是实现 **JobLauncher** 接口的类。以下是JobLauncher的配置。

  ```
  <bean id = "jobLauncher"
     class = "org.springframework.batch.core.launch.support.SimpleJobLauncher">
     <property name = "jobRepository" ref = "jobRepository" />
  </bean>
  ```

  #### JobInstance

  一个 **JobIinstance** 代表工作的逻辑运行; 它是在我们开始工作时创建的。每个作业实例由作业名称和运行时传递给它的参数区分。

  如果JobInstance执行失败，则可以再次执行相同的JobInstance。因此，每个JobInstance可以有多个作业执行。

  #### JobExecution和StepExecution

  JobExecution和StepExecution是作业/步骤执行的表示。它们包含作业/步骤的运行信息，例如开始时间（作业/步骤），结束时间（作业/步骤）。

  ​


## Job

- 介绍：

  -  作业，批处理中的核心概念，是Batch操作的基础单元

  - 每个作业Job有一个或多个作业步骤Step

    ```java
    package com.wjl.springbatch.config;

    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.Step;
    import org.springframework.batch.core.StepContribution;
    import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
    import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
    import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
    import org.springframework.batch.core.scope.context.ChunkContext;
    import org.springframework.batch.core.step.tasklet.Tasklet;
    import org.springframework.batch.repeat.RepeatStatus;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    /**
     * @author wangJiaLun
     * @date 2019-12-11
     **/
    @Configuration
    @EnableBatchProcessing
    public class JobDemo {

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
        public Job jobDemoJob(){
            return jobBuilderFactory.get("jobDemoJob")
                    // next 顺序执行
    /*                .start(step1())
                    .next(step2())
                    .next(step3())*/
                    // on 指定条件  to 到达什么步骤 from 从哪个步骤开始 fail 失败 stopAndRestart 停止重启某步骤
                    .start(step1()).on("COMPLETED").to(step2())
                    .from(step2()).on("COMPLETED").to(step3())
                    .from(step3()).end()
                    .build();
        }

        @Bean
        public Step step1() {
            return stepBuilderFactory.get("step1")
                    .tasklet(new Tasklet() {
                        @Override
                        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                            System.out.println("step1");
                            return RepeatStatus.FINISHED;
                        }
                    })
                    .build();
        }

        @Bean
        public Step step2() {
            return stepBuilderFactory.get("step2")
                    .tasklet(new Tasklet() {
                        @Override
                        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                            System.out.println("step2");
                            return RepeatStatus.FINISHED;
                        }
                    })
                    .build();
        }

        @Bean
        public Step step3() {
            return stepBuilderFactory.get("step3")
                    .tasklet(new Tasklet() {
                        @Override
                        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                            System.out.println("step3");
                            return RepeatStatus.FINISHED;
                        }
                    })
                    .build();
        }
    }

    ```

## Flow

- 介绍：

  - Flow 是多个step的集合

  - 可以被多个Job 复用

  - 使用FlowBuilder来创建

    ```java
    package com.wjl.springbatch.config;

    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.Step;
    import org.springframework.batch.core.StepContribution;
    import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
    import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
    import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
    import org.springframework.batch.core.job.builder.FlowBuilder;
    import org.springframework.batch.core.job.flow.Flow;
    import org.springframework.batch.core.scope.context.ChunkContext;
    import org.springframework.batch.core.step.tasklet.Tasklet;
    import org.springframework.batch.repeat.RepeatStatus;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    /**
     * @author wangJiaLun
     * @date 2019-12-11
     **/
    @Configuration
    @EnableBatchProcessing
    public class FlowDemo {

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

        /**
         *  创建 flowDemoFlow 对象， 指明包含了哪些step
         * @return
         */
        @Bean
        public Flow flowDemoFlow(){
            return new FlowBuilder<Flow>("flowDemoFlow")
                    .start(flowDemoStep1())
                    .next(flowDemoStep2())
                    .build();
        }

        @Bean
        public Job flowDemoJob(){
            return jobBuilderFactory.get("flowDemoJob")
                    .start(flowDemoFlow())
                    .next(flowDemoStep3())
                    .end()
                    .build();
        }

        @Bean
        public Step flowDemoStep1(){
            return stepBuilderFactory.get("flowDemoStep1")
                    .tasklet(new Tasklet() {
                        @Override
                        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                            System.out.println("flowDemoStep1");
                            return RepeatStatus.FINISHED;
                        }
                    })
                    .build();
        }
        @Bean
        public Step flowDemoStep2(){
            return stepBuilderFactory.get("flowDemoStep2")
                    .tasklet(new Tasklet() {
                        @Override
                        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                            System.out.println("flowDemoStep2");
                            return RepeatStatus.FINISHED;
                        }
                    })
                    .build();
        }
        @Bean
        public Step flowDemoStep3(){
            return stepBuilderFactory.get("flowDemoStep3")
                    .tasklet(new Tasklet() {
                        @Override
                        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                            System.out.println("flowDemoStep3");
                            return RepeatStatus.FINISHED;
                        }
                    })
                    .build();
        }
    }

    ```

## Split 并发执行

- 介绍：实现任务中多个step或多个flow并发执行

  ```java
  package com.wjl.springbatch.config;

  import org.springframework.batch.core.Job;
  import org.springframework.batch.core.Step;
  import org.springframework.batch.core.StepContribution;
  import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
  import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
  import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
  import org.springframework.batch.core.job.builder.FlowBuilder;
  import org.springframework.batch.core.job.flow.Flow;
  import org.springframework.batch.core.scope.context.ChunkContext;
  import org.springframework.batch.core.step.tasklet.Tasklet;
  import org.springframework.batch.repeat.RepeatStatus;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.core.task.SimpleAsyncTaskExecutor;

  /**
   * @author wangJiaLun
   * @date 2019-12-11
   **/
  @Configuration
  @EnableBatchProcessing
  public class SpiltDemo {

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
      public Step spiltDemoStep1(){
          return stepBuilderFactory.get("spiltDemoStep1")
                  .tasklet(new Tasklet() {
                      @Override
                      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                          System.out.println("spiltDemoStep1");
                          return RepeatStatus.FINISHED;
                      }
                  })
                  .build();
      }

      @Bean
      public Step spiltDemoStep2(){
          return stepBuilderFactory.get("spiltDemoStep2")
                  .tasklet(new Tasklet() {
                      @Override
                      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                          System.out.println("spiltDemoStep2");
                          return RepeatStatus.FINISHED;
                      }
                  })
                  .build();
      }

      @Bean
      public Step spiltDemoStep3(){
          return stepBuilderFactory.get("spiltDemoStep3")
                  .tasklet(new Tasklet() {
                      @Override
                      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                          System.out.println("spiltDemoStep3");
                          return RepeatStatus.FINISHED;
                      }
                  })
                  .build();
      }

      @Bean
      public Flow splitDemoFlow1(){
          return new FlowBuilder<Flow>("splitDemoFlow1")
                  .start(spiltDemoStep1())
                  .build();
      }

      @Bean
      public Flow splitDemoFlow2(){
          return new FlowBuilder<Flow>("splitDemoFlow2")
                  .start(spiltDemoStep2())
                  .next(spiltDemoStep3())
                  .build();
      }

      @Bean
      public Job splitDemoJob(){
          return jobBuilderFactory.get("splitDemoJob")
                  .start(splitDemoFlow1())
                  .split(new SimpleAsyncTaskExecutor()).add(splitDemoFlow2())
                  .end()
                  .build();
      }
  }
  ```

## 决策器

- 接口：JobExecutionDecider

  ```java
  package com.wjl.springbatch.config;

  import org.springframework.batch.core.*;
  import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
  import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
  import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
  import org.springframework.batch.core.job.flow.FlowExecutionStatus;
  import org.springframework.batch.core.job.flow.JobExecutionDecider;
  import org.springframework.batch.core.scope.context.ChunkContext;
  import org.springframework.batch.core.step.tasklet.Tasklet;
  import org.springframework.batch.repeat.RepeatStatus;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;

  /**
   * @author wangJiaLun
   * @date 2019-12-11
   **/
  @Configuration
  @EnableBatchProcessing
  public class DeciderDemo {

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
      public Step deciderDemoStep1(){
          return stepBuilderFactory.get("deciderDemoStep1")
                  .tasklet(new Tasklet() {
                      @Override
                      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                          System.out.println("deciderDemoStep1");
                          return RepeatStatus.FINISHED;
                      }
                  })
                  .build();
      }

      @Bean
      public Step deciderDemoStep2(){
          return stepBuilderFactory.get("deciderDemoStep2")
                  .tasklet(new Tasklet() {
                      @Override
                      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                          System.out.println("even");
                          return RepeatStatus.FINISHED;
                      }
                  })
                  .build();
      }

      @Bean
      public Step deciderDemoStep3(){
          return stepBuilderFactory.get("deciderDemoStep3")
                  .tasklet(new Tasklet() {
                      @Override
                      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                          System.out.println("odd");
                          return RepeatStatus.FINISHED;
                      }
                  })
                  .build();
      }

      /**
       *  决策器
       */
      @Bean
      public JobExecutionDecider myDecider(){
          return new MyDecider();
      }

      class MyDecider implements JobExecutionDecider{

          private int count;
          @Override
          public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
              count ++;
              if (count % 2 == 0) {
                  return new FlowExecutionStatus("even");
              }else {
                  return new FlowExecutionStatus("odd");
              }
          }
      }

      @Bean
      public Job deciderDemoJob(){
          return jobBuilderFactory.get("deciderDemoJob")
                  .start(deciderDemoStep1())
                  .next(myDecider())
                  .from(myDecider()).on("even").to(deciderDemoStep2())
                  .from(myDecider()).on("odd").to(deciderDemoStep3())
                  .from(deciderDemoStep3()).on("*").to(myDecider())
                  .end()
                  .build();
      }
  }
  ```

## Job 嵌套

-  介绍：一个Job可以嵌套在另一个Job中，被嵌套的Job被称为子Job,外部Job称为父Job，子Job不能单独执行，		  需要父Job来启动

  配置文件配置 父Job对象名称 job:  names: parentJob

  子job1:

  ```java
  package com.wjl.springbatch.config;

  import org.springframework.batch.core.Job;
  import org.springframework.batch.core.Step;
  import org.springframework.batch.core.StepContribution;
  import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
  import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
  import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
  import org.springframework.batch.core.scope.context.ChunkContext;
  import org.springframework.batch.core.step.tasklet.Tasklet;
  import org.springframework.batch.repeat.RepeatStatus;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;

  /**
   * @author wangJiaLun
   * @date 2019-12-11
   **/
  @Configuration
  @EnableBatchProcessing
  public class ChildJob1 {

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
      public Step childJob1Step1(){
          return stepBuilderFactory.get("childJob1Step1")
                  .tasklet(new Tasklet() {
                      @Override
                      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                          System.out.println("childJob1Step1");
                          return RepeatStatus.FINISHED;
                      }
                  })
                  .build();
      }

      @Bean
      public Job childJobOne(){
          return jobBuilderFactory.get("childJobOne")
                  .start(childJob1Step1())
                  .build();
      }
  }
  ```

  子job2:

  ```java
  package com.wjl.springbatch.config;

  import org.springframework.batch.core.Job;
  import org.springframework.batch.core.Step;
  import org.springframework.batch.core.StepContribution;
  import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
  import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
  import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
  import org.springframework.batch.core.scope.context.ChunkContext;
  import org.springframework.batch.core.step.tasklet.Tasklet;
  import org.springframework.batch.repeat.RepeatStatus;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;

  /**
   * @author wangJiaLun
   * @date 2029-22-22
   **/
  @Configuration
  @EnableBatchProcessing
  public class ChildJob2 {

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
      public Step childJob2Step1(){
          return stepBuilderFactory.get("childJob2Step1")
                  .tasklet(new Tasklet() {
                      @Override
                      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                          System.out.println("childJob2Step1");
                          return RepeatStatus.FINISHED;
                      }
                  })
                  .build();
      }

      @Bean
      public Step childJob2Step2(){
          return stepBuilderFactory.get("childJob2Step2")
                  .tasklet(new Tasklet() {
                      @Override
                      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                          System.out.println("childJob2Step2");
                          return RepeatStatus.FINISHED;
                      }
                  })
                  .build();
      }

      @Bean
      public Job childJobTwo(){
          return jobBuilderFactory.get("childJobTwo")
                  .start(childJob2Step1())
                  .next(childJob2Step2())
                  .build();
      }
  }
  ```

  父job:

  ```java
  package com.wjl.springbatch.config;

  import org.springframework.batch.core.Job;
  import org.springframework.batch.core.Step;
  import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
  import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
  import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
  import org.springframework.batch.core.launch.JobLauncher;
  import org.springframework.batch.core.repository.JobRepository;
  import org.springframework.batch.core.step.builder.JobStepBuilder;
  import org.springframework.batch.core.step.builder.StepBuilder;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.transaction.PlatformTransactionManager;

  /**
   * @author wangJiaLun
   * @date 2019-12-11
   **/
  @Configuration
  @EnableBatchProcessing
  public class NestedDemo {

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
      private Job childJobOne;

      @Autowired
      private Job childJobTwo;

      /**
       *  启动对象
       */
      @Autowired
      private JobLauncher jobLauncher;

      @Bean
      public Job parentJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
          return jobBuilderFactory.get("parentJob")
                  .start(childJobDemo1(jobRepository, transactionManager))
                  .next(childJobDemo2(jobRepository, transactionManager))
                  .build();
      }

      /**
       * @return 返回的是Job类型的step， 特殊的step
       */
      @Bean
      public Step childJobDemo1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
          return new JobStepBuilder(new StepBuilder("childJobDemo1"))
                  .job(childJobOne)
                  // 使用父job的启动对象
                  .launcher(jobLauncher)
                  .repository(jobRepository)
                  .transactionManager(transactionManager)
                  .build();
      }

      /**
       * @return 返回的是Job类型的step， 特殊的step
       */
      @Bean
      public Step childJobDemo2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
          return new JobStepBuilder(new StepBuilder("childJobDemo2"))
                  .job(childJobTwo)
                  // 使用父job的启动对象
                  .launcher(jobLauncher)
                  .repository(jobRepository)
                  .transactionManager(transactionManager)
                  .build();
      }
  }
  ```

## 监听器

- 介绍：

  - 用来监听批处理作业的执行情况
    - JobExecutionListener(before, after)
    - StepExecutionListener(before, after)
    - ChunkListener(before, after, error)
    - ItemReadListener, ItemProcessListener, ItemWriteListener(before, after, error)
  - 创建监听可以通过实现接口或者使用注解

  job 级别 监听器 基于接口

  ```java
  package com.wjl.springbatch.listener;

  import org.springframework.batch.core.JobExecution;
  import org.springframework.batch.core.JobExecutionListener;

  /**
   * @author wangJiaLun
   * @date 2019-12-11
   **/
  public class MyJobListener implements JobExecutionListener {

      @Override
      public void beforeJob(JobExecution jobExecution) {
          System.out.println(jobExecution.getJobInstance().getJobName()+"before...");
      }

      @Override
      public void afterJob(JobExecution jobExecution) {
          System.out.println(jobExecution.getJobInstance().getJobName()+"after...");
      }
  }
  ```

  chunk 级别监听 基于注解

  ```java
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
  ```

  监听器使用demo

  ```java
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
  @Configuration
  @EnableBatchProcessing
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
  ```

## Job 参数

- 介绍： 在Job运行时 可以通过key=value形式传参

  ​