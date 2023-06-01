package dev.visionhikooo.main;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.features.filesystem.OptionManager;

import java.time.chrono.IsoChronology;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleTaskManager {
    private SchollBot bot;

    private ScheduledExecutorService everyHour;
    private ScheduledExecutorService everyDay;


    public ScheduleTaskManager(SchollBot bot) {
        this.bot = bot;
        init();
    }

    public void init() {
        SchollBot.sendConsoleMessage("Initialisiere Scheduled Tasks", Debug.LOW);
        everyHour= Executors.newScheduledThreadPool(1);
        everyHour.scheduleAtFixedRate(this::everyHour, 20, 60*60, TimeUnit.SECONDS);

        everyDay = Executors.newScheduledThreadPool(1);
        everyDay.scheduleAtFixedRate(this::everyDay, 20, 60*60*24, TimeUnit.SECONDS);
    }

    public void everyHour() {
        SchollBot.sendConsoleMessage("Execute Every Hour Tasks", Debug.LOW);
        bot.getScholltimesManager().check(!bot.getOptionManager().hasID(OptionManager.Options.LAST_SCHOLLTIMES_ID));
        bot.changeStatus();
    }

    public void everyDay() {
        bot.getStatistikManager().safe();
    }
}
