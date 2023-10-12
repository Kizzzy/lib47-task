package cn.kizzzy.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ScheduledCheckThread extends Thread {
    
    private static final Logger logger = LoggerFactory.getLogger(ScheduledCheckThread.class);
    
    private final List<Taskable> waitQueue;
    
    private final List<Taskable> execQueue;
    
    private volatile boolean running;
    
    public ScheduledCheckThread(String name) {
        super(name);
        this.waitQueue = new LinkedList<>();
        this.execQueue = new LinkedList<>();
        setPriority(Thread.MAX_PRIORITY);
    }
    
    public void terminate() {
        running = false;
        synchronized (waitQueue) {
            waitQueue.notify();
        }
    }
    
    public void addTask(Taskable task) {
        synchronized (waitQueue) {
            waitQueue.add(task);
            waitQueue.notify();
        }
    }
    
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                synchronized (waitQueue) {
                    if (waitQueue.isEmpty()) {
                        waitQueue.wait();
                    }
                    
                    long time = System.currentTimeMillis();
                    
                    Iterator<Taskable> iterator = waitQueue.iterator();
                    while (iterator.hasNext()) {
                        Taskable task = iterator.next();
                        if (task.isValid()) {
                            if (task.canExec()) {
                                iterator.remove();
                                execQueue.add(task);
                            }
                        } else {
                            iterator.remove();
                        }
                    }
                    
                    time = System.currentTimeMillis() - time;
                    if (time > 100) {
                        logger.warn("check delay thread is spend too much time !!!  interval = {} ms", time);
                    }
                    
                }
                
                for (Taskable task : execQueue) {
                    task.getQueue().addTask(task);
                }
                execQueue.clear();
                
                Thread.sleep(10);
            } catch (Exception e) {
                logger.error("delayCheckThread error ! ", e);
            }
        }
    }
}
