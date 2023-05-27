package dev.visionhikooo.surveysAndStatistics;

import dev.visionhikooo.filesystem.Safeable;
import dev.visionhikooo.listener.TempChannelManager;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        long initialDelay = 20;
        long period = 60*60*24;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        executor.scheduleAtFixedRate(new Runnable() {
            @Override public void run() {
                safe();
            }
        }, initialDelay, period, timeUnit);
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
        System.out.println("SAFE");
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
