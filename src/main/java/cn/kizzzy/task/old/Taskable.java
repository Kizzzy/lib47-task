package cn.kizzzy.task.old;

import cn.kizzzy.event.IEventListener;

public interface Taskable extends Runnable, IEventListener {
    
    void doInitial();
}
