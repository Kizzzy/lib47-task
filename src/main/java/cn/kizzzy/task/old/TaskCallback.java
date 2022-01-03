package cn.kizzzy.task.old;

import java.util.function.Consumer;

public class TaskCallback {
    private final int eventType;
    private final Consumer<Object> callback;

    public TaskCallback(int eventType, Consumer<Object> callback) {
        this.eventType = eventType;
        this.callback = callback;
    }

    public int getEventType() {
        return eventType;
    }

    public Consumer<Object> getCallback() {
        return callback;
    }
}
