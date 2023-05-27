package dev.visionhikooo.filesystem;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.main.SchollBot;

import java.util.HashMap;

public class OptionManager implements Safeable {

    public enum Options {
        RULES_ID,
        CLASSES_ID,
        TEMP_CAT_ID,
        BOT_ID,
        DEBUG_ID,
        TICKET_CAT_ID,
        ADMIN_ID,
        MOD_ID
    }

    private SchollBot bot;
    private HashMap<Options, Long> specialIDs;
    private static Debug debug = Debug.HIGH;

    public OptionManager(SchollBot bot) {
        this.bot = bot;
        load();
        if (specialIDs == null)
            specialIDs = new HashMap<>();
    }

    public static Debug getDebug() {
        return debug;
    }

    public static void setDebug(Debug debug) {
        OptionManager.debug = debug;
    }

    public void safeIDs() {
        bot.getFileManager().writeObjectToFile("ids.scholl", specialIDs);
    }

    public void loadIDs() {
        specialIDs = (HashMap<Options, Long>) bot.getFileManager().getObjectFromFile("ids.scholl");
    }

    public long getID(Options option) {
        return specialIDs.containsKey(option) ? specialIDs.get(option) : 0L;
    }

    public boolean hasID(Options options) {
        return getID(options)!=0L;
    }

    public void setID(Options options, Long id) {
        bot.getGuildReactionManager().changeID(getID(options), id);
        specialIDs.put(options, id);
    }

    @Override
    public void safe() {
        safeIDs();
    }

    @Override
    public void load() {
        loadIDs();
    }
}
