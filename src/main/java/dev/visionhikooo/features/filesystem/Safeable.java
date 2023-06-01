package dev.visionhikooo.features.filesystem;

import java.io.Serializable;

public interface Safeable extends Serializable {
    public void safe();
    public void load();

    public default void reload() {
        safe();
        load();
    }
}
