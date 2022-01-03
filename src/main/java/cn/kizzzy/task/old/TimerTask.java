package cn.kizzzy.task.old;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class TimerTask<T extends TaskContext> extends TaskBase<T> {
    private final int interval;
    private final TimeUnit timeUnit;
    
    public TimerTask(ScheduledExecutorService executor, T context, int interval, TimeUnit timeUnit) {
        super(executor, context);
        this.interval = interval;
        this.timeUnit = timeUnit;
    }
    
    @Override
    protected Future start0() {
        return executor.scheduleAtFixedRate(this, 0, interval, timeUnit);
    }
}
