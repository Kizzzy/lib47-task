package cn.kizzzy.task;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class SupplierConsumerExecutor<T> {
    
    private ExecutorService executorService;
    
    private ConcurrentLinkedQueue<T> taskQueue;
    
    private int threadCount;
    
    private Consumer<T> callback;
    
    public SupplierConsumerExecutor(int threadCount, Consumer<T> callback) {
        this.threadCount = threadCount;
        this.callback = callback;
    }
    
    public void start() {
        executorService = Executors.newFixedThreadPool(threadCount);
        taskQueue = new ConcurrentLinkedQueue<>();
        
        for (int i = 0; i < threadCount; ++i) {
            executorService.submit(new ConsumerTask<>(taskQueue, callback));
        }
    }
    
    public void stop() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
    
    public void addTask(T task) {
        taskQueue.offer(task);
    }
}
