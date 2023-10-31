package com.zh.multiThread;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName MyThread
 * @Description 多线程配置类
 * @Author @O_o
 * @Date 2023/10/30 22:07
 * @Version 1.0
 */
@EnableAsync    // 开启异步执行
@Configuration
@Slf4j
public class MyThread {
//    private static Logger logger = LoggerFactory.getLogger(MyThread.class.getName());

    /**
     * 覆盖springboot中默认的线程池
     *
     * @return TaskExecutor
     */
    @Bean
    public TaskExecutor taskExecutor() {
        log.info("Start asyncExecutor......");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(20);
        //配置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(20);
        //配置线程池中的线程的名称前缀 (指定一下线程名的前缀)
        executor.setThreadNamePrefix("async-tank");

        // rejection-policy：当pool已经达到max pool size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        //配置守护线程
        executor.setDaemon(true);

        //执行初始化
        executor.initialize();

        return executor;
    }
}
