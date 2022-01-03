package cn.kizzzy.task.old;

import cn.kizzzy.event.EventArgs;
import cn.kizzzy.helper.LogHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public abstract class TaskBase<T extends TaskContext> implements Taskable {
    protected final ScheduledExecutorService executor;
    protected final T context;
    
    private Future<?> future;
    private Map<Integer, TaskCallback> callbacks = new HashMap<>();
    
    public TaskBase(ScheduledExecutorService executor, T context) {
        this.executor = executor;
        this.context = context;
    }
    
    @Override
    public void doInitial() {
        this.registerCallbacks();
        this.afterInitial();
    }
    
    protected TaskCallback[] callbacks() {
        return new TaskCallback[]{};
    }
    
    /**
     * 添加监听
     */
    private void registerCallbacks() {
        for (TaskCallback callback : callbacks()) {
            callbacks.put(callback.getEventType(), callback);
        }
        if (context != null) {
            for (int eventType : callbacks.keySet()) {
                context.addListener(eventType, this);
            }
        }
    }
    
    public void onNotify(EventArgs args) {
        TaskCallback callback = callbacks.get(args.getType());
        if (callback != null) {
            callback.getCallback().accept(args.getParams());
        }
    }
    
    /**
     * 额外初始化
     */
    protected void afterInitial() {
    
    }
    
    protected void beforeStart(Object params) {
    
    }
    
    protected void start(Object params) {
        beforeStart(params);
        if (future != null) {
            future.cancel(true);
        }
        future = start0();
    }
    
    protected abstract Future<?> start0();
    
    protected void beforeStop(Object params) {
    
    }
    
    protected void stop(Object params) {
        beforeStop(params);
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }
    
    @Override
    public void run() {
        try {
            execImpl();
        } catch (Throwable e) {
            if (e instanceof InterruptedException) {
                stop(null);
            } else {
                LogHelper.error(null, e);
            }
        }
    }
    
    protected abstract void execImpl() throws Exception;
}
