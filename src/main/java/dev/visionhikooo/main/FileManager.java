package dev.visionhikooo.main;

import com.iwebpp.crypto.TweetNaclFast;

import java.io.*;
import java.util.HashMap;

public class FileManager {

    private final SchollBot bot;

    public void safeIDs() {
        writeObjectToFile("ids.scholl", specialIDs);
    }

    public enum Options {
        RULES,
        CLASSES,
        TEMP_CAT,
        BOT,
        DEBUG,
        TICKET_CAT
    }

    private String defaultURL = System.getProperty("user.dir") + File.separator + "Data";
    private HashMap<Options, Long> specialIDs;

    public FileManager(SchollBot bot) {
        this.bot = bot;
        loadIDs();
        if (specialIDs == null)
            specialIDs = new HashMap<>();
        initFolderStructure();
    }

    public void loadIDs() {
        specialIDs = (HashMap<Options, Long>) getObjectFromFile("ids.scholl");
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

    public void initFolderStructure() {
        File folder = new File(defaultURL);
        if (!folder.exists()) {
            folder.mkdirs();
            folder.mkdir();
        }
    }

    public void writeObjectToFile(File file, Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeObjectToFile(String path, String fileName, Object obj) {
        try {
            new File(defaultURL + (path.isEmpty()?"":File.separator) + path).mkdirs();
            File file = new File(defaultURL + (path.isEmpty()?"":File.separator) + path + File.separator + fileName);

            if (!file.exists())
                file.createNewFile();

            writeObjectToFile(file, obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeObjectToFile(String fileName, Object obj) {
        writeObjectToFile("", fileName, obj);
    }

    public Object getObjectFromFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayInputStream bais = new ByteArrayInputStream(fis.readAllBytes());
            return new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getObjectFromFile(String path, String fileName) {
        new File(defaultURL + (path.isEmpty()?"":File.separator) + path).mkdirs();
        File file = new File(defaultURL + (path.isEmpty()?"":File.separator) + path + File.separator + fileName);

        if (!file.exists())
            return null;

        return getObjectFromFile(file);
    }

    public Object getObjectFromFile(String fileName) {
        return getObjectFromFile("", fileName);
    }
}
