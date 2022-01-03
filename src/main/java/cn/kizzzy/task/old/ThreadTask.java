package cn.kizzzy.task.old;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public abstract class ThreadTask<T extends TaskContext> extends TaskBase<T> {
    
    public ThreadTask(ScheduledExecutorService executor, T context) {
        super(executor, context);
    }
    
    @Override
    protected Future<?> start0() {
        return executor.submit(this);
    }
}
