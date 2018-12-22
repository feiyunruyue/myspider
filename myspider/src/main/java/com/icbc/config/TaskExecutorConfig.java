package com.icbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class TaskExecutorConfig  {

    /** Set the ThreadPoolExecutor's core pool size. */
    private int corePoolSize = 5;
    /** Set the ThreadPoolExecutor's maximum pool size. */
    private int maxPoolSize = 10;
    /** Set the capacity for the ThreadPoolExecutor's BlockingQueue. */
    private int queueCapacity = 2000;


    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;


    }
}
