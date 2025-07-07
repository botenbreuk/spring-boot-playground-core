package nl.rdb.springbootplayground.config.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@Profile("!disable-async")
@EnableAsync(proxyTargetClass = true)
public class AsyncConfiguration implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10000);
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
