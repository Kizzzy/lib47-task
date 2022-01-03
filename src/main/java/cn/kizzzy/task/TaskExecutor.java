package cn.kizzzy.task;

import java.util.concurrent.Executor;

public interface TaskExecutor extends Executor, TaskQueue {
    
    boolean start();
    
    boolean stop();
    
    void addScheduledTask(Taskable task);
}
