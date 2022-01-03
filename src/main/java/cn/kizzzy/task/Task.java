package cn.kizzzy.task;

import cn.kizzzy.helper.LogHelper;

public abstract class Task implements Taskable {
    private boolean valid = true;
    private TaskQueue taskQueue;
    
    @Override
    public void run() {
        try {
            onTask();
        } catch (Exception e) {
            LogHelper.error(null, e);
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
