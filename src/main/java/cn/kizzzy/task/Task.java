package cn.kizzzy.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Task implements Taskable {
    
    private static final Logger logger = LoggerFactory.getLogger(Task.class);
    
    private boolean valid = true;
    
    private TaskQueue taskQueue;
    
    @Override
    public void run() {
        try {
            onTask();
        } catch (Exception e) {
            logger.error("execute task error", e);
        }
    }
    
    protected abstract void onTask();
    
    @Override
    public TaskQueue getQueue() {
        return taskQueue;
    }
    
    @Override
    public void setQueue(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }
    
    @Override
    public boolean isValid() {
        return valid;
    }
    
    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    @Override
    public boolean canExec() {
        return true;
    }
}
