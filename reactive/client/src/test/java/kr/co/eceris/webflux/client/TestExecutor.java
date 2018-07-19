package kr.co.eceris.webflux.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class TestExecutor {

    private final String url;
    private final int count;

    public static final RestTemplate REST_TEMPLATE = new RestTemplateBuilder().setConnectTimeout(3000).build();
    private final ExecutorService executor;
    private final CountDownLatch latch;

    private Status status;
    private long startTimeMillis;
    private long endTimeMillis;

    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failCount = new AtomicInteger(0);

    private enum Status {
        READY, PROGRESS, DONE
    }

    private TestExecutor(String url, int count) {
        this.status = Status.READY;
        this.url = url;
        this.count = count;
        this.executor = Executors.newFixedThreadPool(count);
        this.latch = new CountDownLatch(count);
    }

    public static TestExecutor create(String url, int count) {
        return new TestExecutor(url, count);
    }

    public TestExecutor run() {
        this.status = Status.PROGRESS;
        this.startTimeMillis = System.currentTimeMillis();

        CompletableFuture<String>[] futures = new CompletableFuture[this.count];

        IntStream.range(0, count).forEach(i -> {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> REST_TEMPLATE.getForObject(url + "/" + i, String.class), executor);
            futures[i] = future;
            future.thenAccept(r -> {
                this.successCount.incrementAndGet();
            }).exceptionally(t -> {
                this.failCount.incrementAndGet();
                return null;
            });
        });

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                CompletableFuture.allOf(futures).get();
            } catch (Exception e) {
                log.error("execute exception. {} ", e);
            }

            this.endTimeMillis = System.currentTimeMillis();
            this.status = Status.DONE;
        });

        return this;
    }

    public long getStartTimeMillis() {
        return this.startTimeMillis;
    }

    public long getEndTimeMillis() {
        return this.endTimeMillis;
    }

    public long getElapsedTimeMillis() {
        return System.currentTimeMillis() - this.startTimeMillis;
    }

    public long getTotalTimeMillis() {
        if (this.isDone()) {
            return this.endTimeMillis - this.startTimeMillis;
        } else {
            return getElapsedTimeMillis();
        }
    }

    public double getAverageTimeMillis() {
        int denominator = successCount.get() == 0 ? 1 : successCount.get();
        if (this.isDone()) {
            return getElapsedTimeMillis() / denominator;
        } else {
            return this.getTotalTimeMillis() / denominator;
        }
    }

    public boolean isDone() {
        return this.status == Status.DONE;
    }

    public CharSequence print() {
        StringBuilder sb = new StringBuilder();
        sb.append("url: ").append(url).append(System.lineSeparator());
        sb.append("total count: ").append(count).append(System.lineSeparator());
        sb.append("success count: ").append(this.successCount.get()).append(System.lineSeparator());
        sb.append("fail count: ").append(this.failCount.get()).append(System.lineSeparator());
        sb.append("status: ").append(this.status).append(System.lineSeparator());
        sb.append("start time(millis): ").append(this.getStartTimeMillis()).append(System.lineSeparator());
        sb.append("elapsed time(millis): ").append(this.getElapsedTimeMillis()).append(System.lineSeparator());
        sb.append("average time(millis): ").append(this.getAverageTimeMillis());
        if (this.isDone()) {
            sb.append(System.lineSeparator()).append("end Time(millis): ").append(this.getEndTimeMillis());
        }
        return sb;
    }

    @Override
    public String toString() {
        return print().toString();
    }
}
