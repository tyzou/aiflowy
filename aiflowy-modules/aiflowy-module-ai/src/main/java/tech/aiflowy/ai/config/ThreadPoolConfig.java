package tech.aiflowy.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    /**
     * SSE消息发送专用线程池
     * 核心原则：IO密集型任务（网络推送），线程数 = CPU核心数 * 2 + 1
     */
    @Bean(name = "sseThreadPool")
    public ThreadPoolTaskExecutor sseThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cpuCoreNum = Runtime.getRuntime().availableProcessors(); // 获取CPU核心数（4核返回4）

        executor.setCorePoolSize(cpuCoreNum * 2); // 核心线程数：8（IO密集型任务，线程数略多）
        executor.setMaxPoolSize(cpuCoreNum * 4); // 最大线程数：16（峰值时扩容，避免线程过多导致上下文切换）
        executor.setQueueCapacity(2000); // 任务队列容量：2000（缓冲高并发消息，避免队列溢出）
        executor.setKeepAliveSeconds(30); // 空闲线程存活时间：30秒（非核心线程空闲后销毁，节省资源）
        executor.setThreadNamePrefix("sse-sender-"); // 线程名前缀（便于日志排查）

        // 拒绝策略：当队列满+线程数达最大时，由调用线程（如控制器线程）临时处理，避免消息丢失
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 初始化线程池（必须调用，否则线程池不生效）
        executor.initialize();
        return executor;
    }
}
