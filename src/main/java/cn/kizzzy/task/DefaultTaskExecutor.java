package cn.kizzzy.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

public class DefaultTaskExecutor implements TaskExecutor {
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultTaskExecutor.class);
    
    private final Supplier<ExecutorService> executorServiceSupplier;
    
    private ExecutorService executor;
    
    private DefaultTaskQueue taskQueue;
    
    private ScheduledCheckThread scheduledCheckThread;
    
    public DefaultTaskExecutor() {
        this(Executors::newCachedThreadPool);
    }
    
    public DefaultTaskExecutor(Supplier<ExecutorService> executorServiceSupplier) {
        this.executorServiceSupplier = executorServiceSupplier;
    }
    
    @Override
    public boolean start() {
        try {
            scheduledCheckThread = new ScheduledCheckThread("ScheduledCheckThread");
            scheduledCheckThread.start();
            
            executor = executorServiceSupplier.get();
            taskQueue = new DefaultTaskQueue(this);
            taskQueue.start();
            return true;
        } catch (Exception var2) {
            logger.error("start executor error", var2);
            return false;
        }
    }
    
    @Override
    public boolean stop() {
        try {
            scheduledCheckThread.terminate();
            taskQueue.stop();
            executor.shutdown();
            return true;
        } catch (Exception var2) {
            logger.error("stop executor error", var2);
            return false;
        }
    }
    
    @Override
    public boolean setThread(int min, int max) {
        if (executor instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) executor).setCorePoolSize(min);
            ((ThreadPoolExecutor) executor).setMaximumPoolSize(max);
            return true;
        }
        return false;
    }
    
    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
    
    @Override
    public void addTask(Taskable task) {
        taskQueue.addTask(task);
    }
    
    @Override
    public void removeTask(Taskable task) {
        taskQueue.removeTask(task);
    }
    
    @Override
    public void addScheduledTask(Taskable task) {
        scheduledCheckThread.addTask(task);
    }
}
