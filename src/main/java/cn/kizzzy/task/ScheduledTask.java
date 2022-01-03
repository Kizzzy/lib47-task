package cn.kizzzy.task;

public abstract class ScheduledTask extends Task {
    private int interval;
    private int repeat;
    
    private int count;
    private long nextExeTime;
    
    public ScheduledTask(int delay) {
        this(delay, 0, 0);
    }
    
    public ScheduledTask(int delay, int interval, int repeat) {
        this.interval = interval;
        this.repeat = repeat;
        this.nextExeTime = System.currentTimeMillis() + delay;
    }
    
    protected int getInterval() {
        return interval;
    }
    
    protected void setInterval(int interval) {
        this.interval = interval;
    }
    
    protected int getRepeat() {
        return repeat;
    }
    
    protected void setRepeat(int repeat) {
        this.repeat = repeat;
    }
    
    protected int getCount() {
        return count;
    }
    
    protected void setCount(int count) {
        this.count = count;
    }
    
    @Override
    public void run() {
        super.run();
        if (isValid() && (repeat < 0 || (repeat > 0 && ++count <= repeat))) {
            nextExeTime = System.currentTimeMillis() + interval;
            getQueue().addTask(this);
        }
    }
    
    @Override
    public boolean canExec() {
        return System.currentTimeMillis() >= nextExeTime;
    }
}
