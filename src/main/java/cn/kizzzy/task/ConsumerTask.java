package cn.kizzzy.task;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class ConsumerTask<T> implements Runnable {
    
    private ConcurrentLinkedQueue<T> taskQueue;
    
    private Consumer<T> callback;
    
    public ConsumerTask(ConcurrentLinkedQueue<T> taskQueue, Consumer<T> callback) {
        this.taskQueue = taskQueue;
        this.callback = callback;
    }
    
    @Override
    public void run() {
        try {
            Thread currentThread = Thread.currentThread();
            while (true) {
                T data;
                do {
                    data = taskQueue.poll();
                    if (data != null) {
                        break;
                    }
                    Thread.sleep(10);
                } while (!Thread.interrupted());
                
                if (currentThread.isInterrupted()) {
                    break;
                }
                
                if (callback != null) {
                    callback.accept(data);
                }
            }
        } catch (InterruptedException ignored) {
        
        }
    }
}
