package dev.visionhikooo.features.surveysAndStatistics;

import dev.visionhikooo.features.filesystem.Safeable;
import dev.visionhikooo.listener.TempChannelManager;
import dev.visionhikooo.main.SchollBot;

import java.util.HashMap;

public class StatistikManager implements Safeable {

    public enum StatisticCategory {
        MESSAGES_PER_DAY,
        VOICE_CHANNEL_USERS,
        COMMANDS_PER_DAY,
        TICKETS_PER_DAY,
        REPORTS_PER_DAY,
        NEW_USERS
    }

    private transient SchollBot schollBot;
    private HashMap<StatisticCategory, Counter> statistics;

    public StatistikManager(SchollBot schollBot) {
        statistics = new HashMap<>();
        this.schollBot = schollBot;
    }

    public void addStatisticValue(StatisticCategory category, int value) {
        if (statistics.containsKey(category))
            statistics.get(category).increase(value);
        else
            statistics.put(category, new Counter(value));
    }

    public void addStatisticValue(StatisticCategory category) {
        if (statistics.containsKey(category))
            statistics.get(category).increase();
        else
            statistics.put(category, new Counter(1));
    }

    @Override
    public void safe() {
        schollBot.getFileManager().safeStatistics(statistics);
        for (StatisticCategory cat: StatisticCategory.values()) {
            statistics.put(cat,new Counter());
        }
        TempChannelManager.resetUserMemory();
    }

    @Override
    public void load() {

    }
}
