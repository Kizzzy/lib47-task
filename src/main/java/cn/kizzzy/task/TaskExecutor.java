package cn.kizzzy.task;

import java.util.concurrent.Executor;

public interface TaskExecutor extends Executor, TaskQueue {
    
    boolean start();
    
    boolean stop();
    
    boolean setThread(int min, int max);
    
    void addScheduledTask(Taskable task);
}
