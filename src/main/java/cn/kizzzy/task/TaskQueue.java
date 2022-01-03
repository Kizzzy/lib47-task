package cn.kizzzy.task;

public interface TaskQueue {
    
    void addTask(Taskable task);
    
    void removeTask(Taskable task);
}
