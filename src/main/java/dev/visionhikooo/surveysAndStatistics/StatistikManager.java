package dev.visionhikooo.surveysAndStatistics;

import dev.visionhikooo.main.SchollBot;

import java.util.HashMap;

public class StatistikManager {

    private SchollBot schollBot;

    public class Counter {
        private int val;
        public Counter(int val) {
            this.val = val;
        }
        public Counter() {
            this(0);
        }
        public void increase() {
            val++;
        }
        public void decrease() {
            val--;
        }
        public void setVal(int val) {
            this.val = val;
        }
    }

    public enum StatisticCategory {
        USER_COUNT,
        MESSAGES_PER_DAY,
        VOICE_CHANNEL_USER
    }

    public StatistikManager(SchollBot schollBot) {
        this.schollBot = schollBot;
    }

    private HashMap<StatisticCategory, Counter> statistics;

    public void addStatisticValue(StatisticCategory category, int value) {
        if (statistics.containsKey(category))
            statistics.get(category).increase();
        else
            statistics.put(category, new Counter());
    }

    public void safeStatistics() {

    }


}
