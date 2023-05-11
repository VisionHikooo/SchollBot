package dev.visionhikooo.listener;

import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Listener extends ListenerAdapter {
    private SchollBot schollBot;

    public Listener(SchollBot schollBot) {
        this.schollBot = schollBot;
    }

    protected SchollBot getSchollBot() {
        return schollBot;
    }
}
