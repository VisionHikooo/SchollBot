package dev.visionhikooo.filesystem;

import java.io.Serializable;

public interface Safeable extends Serializable {
    public void safe();
    public void load();
}
