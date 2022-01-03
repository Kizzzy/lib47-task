package cn.kizzzy.task;

import cn.kizzzy.helper.LogHelper;

import java.util.LinkedList;
import java.util.Queue;

public class DefaultTaskQueue implements TaskQueue, Runnable {
    
    private volatile boolean running;
    
    private final Queue<Taskable> taskQueue =
        new LinkedList<>();
    
    private TaskExecutor executor;
    
    public DefaultTaskQueue(TaskExecutor executor) {
        this.executor = executor;
    }
    
    public boolean start() {
        running = true;
        executor.execute(this);
        return true;
    }
    
    public boolean stop() {
        running = false;
        synchronized (taskQueue) {
            taskQueue.notify();
        }
        return true;
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                synchronized (taskQueue) {
                    if (taskQueue.isEmpty()) {
                        taskQueue.wait();
                    }
                    
                    Taskable task = taskQueue.poll();
                    if (task != null && task.isValid()) {
                        long time = System.currentTimeMillis();
                        
                        executor.execute(task);
                        
                        time = System.currentTimeMillis() - time;
                        if (time >= 500) {
                            LogHelper.warn("task({}) execute much time: {} ms", task, time);
                        }
                    }
                }
            } catch (InterruptedException e) {
                LogHelper.error(null, e);
            }
        }
    }
    
    @Override
    public void addTask(Taskable task) {
        synchronized (taskQueue) {
            task.setQueue(this);
            if (task.canExec()) {
                taskQueue.add(task);
                taskQueue.notify();
                return;
            }
        }
        executor.addScheduledTask(task);
    }
    
    @Override
    public void removeTask(Taskable task) {
        synchronized (taskQueue) {
            task.setValid(false);
        }
    }
}
