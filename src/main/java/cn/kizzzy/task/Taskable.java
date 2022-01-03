package cn.kizzzy.task;

public interface Taskable extends Runnable {
    
    TaskQueue getQueue();
    
    void setQueue(TaskQueue taskQueue);
    
    boolean isValid();
    
    void setValid(boolean valid);
    
    boolean canExec();
}
