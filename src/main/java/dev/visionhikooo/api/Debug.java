package dev.visionhikooo.api;

import dev.visionhikooo.main.SchollBot;

public enum Debug {
    HIGH(3),
    NORMAL(2),
    LOW(1),
    NONE(0);

    private int value;

    private Debug(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isAllowed() {
        return value <= SchollBot.getDebug().getValue();
    }
}
