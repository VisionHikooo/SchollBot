package dev.visionhikooo.features.filesystem;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.main.SchollBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionManager implements Safeable {

    public enum Options {
        RULES_ID,
        CLASSES_ID,
        TEMP_CAT_ID,
        BOT_ID,
        DEBUG_ID,
        TICKET_CAT_ID,
        SCHOLLTIMES_ID,
        LAST_SCHOLLTIMES_ID
    }

    private SchollBot bot;
    private HashMap<Options, Long> specialIDs;
    private static Debug debug = Debug.HIGH;
    private static boolean sendDebugToChannel = false;

    public OptionManager(SchollBot bot) {
        this.bot = bot;
        load();
        if (specialIDs == null)
            specialIDs = new HashMap<>();
    }

    public static boolean isSendDebugToChannel() {
        return sendDebugToChannel;
    }

    public static void setSendDebugToChannel(boolean b) {
        sendDebugToChannel = b;
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

    public void removeID(Options option) {
        specialIDs.remove(option);
    }

    public long getID(Options option) {
        return specialIDs.getOrDefault(option, 0L);
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
        HashMap<String, Object> map = new HashMap<>();
        map.put("Debug", debug.toString());
        map.put("SendDebugToChannel", sendDebugToChannel);
        HashMap<String, Object> ids = new HashMap<>();
        for (Options d : Options.values()) {
            ids.put(d.toString(), specialIDs.getOrDefault(d, 0L));
        }
        map.put("Ids", ids);
        bot.getFileManager().writeToYaml(map,"config.scholl");
    }

    @Override
    public void load() {
        HashMap<String, Object> map = bot.getFileManager().readFromYaml("config.scholl");
        if (map.containsKey("Debug")) debug = Debug.valueOf(((String) map.get("Debug")).toUpperCase());
        if (map.containsKey("SendDebugToChannel")) sendDebugToChannel = (boolean) map.get("SendDebugToChannel");
        specialIDs = new HashMap<>();
        if (map.containsKey("Ids")) {
            HashMap<String, Object> ids = (HashMap<String, Object>) map.get("Ids");
            System.out.println("True");
            for (Options option : Options.values()) {
                if (ids.containsKey(option.toString())) {
                    specialIDs.put(option, Long.valueOf(ids.get(option.toString()) instanceof Integer ? String.valueOf((int) ids.get(option.toString())) : String.valueOf((long) ids.get(option.toString()))));
                }
            }
        }
    }
}
