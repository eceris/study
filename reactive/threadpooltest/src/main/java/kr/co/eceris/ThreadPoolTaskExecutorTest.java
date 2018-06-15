package kr.co.eceris;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTaskExecutorTest {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(100);
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setThreadNamePrefix("박성현 thread");
        taskExecutor.initialize();

        AtomicInteger count = new AtomicInteger(0);
        while (true) {
            count.incrementAndGet();
            count.getAndIncrement();
            TimeUnit.MILLISECONDS.sleep(15);
            if (count.intValue() == 1000) {
                taskExecutor.shutdown();
                break;
            }
            taskExecutor.execute(() -> {
                System.out.print("ThreadName : " + Thread.currentThread().getName() + ", ");
                System.out.print("ThreadPoolSize : " + taskExecutor.getPoolSize() + ", ");
                System.out.print("corePoolSize : " + taskExecutor.getCorePoolSize() + ", ");
                System.out.print("queueSize : " + taskExecutor.getThreadPoolExecutor().getQueue().size() + ", ");
                System.out.print("queueRemainingCapacity : " + taskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity() + ", ");
                System.out.print("maximumPoolSize : " + taskExecutor.getThreadPoolExecutor().getMaximumPoolSize() + ", ");
                System.out.println("count : " + count);
                try {
                    TimeUnit.MILLISECONDS.sleep(1500);
                } catch (InterruptedException e) {
                }
            });

        }

    }
}
