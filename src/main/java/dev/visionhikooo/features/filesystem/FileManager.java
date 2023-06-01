package dev.visionhikooo.features.filesystem;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.main.SchollBot;
import dev.visionhikooo.features.surveysAndStatistics.Counter;
import dev.visionhikooo.features.surveysAndStatistics.StatistikManager;
import kotlin.Pair;
import net.dv8tion.jda.api.entities.Activity;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FileManager {


    private final SchollBot bot;

    private String defaultURL = System.getProperty("user.dir") + File.separator + "Data";

    public FileManager(SchollBot bot) {
        this.bot = bot;
        initFolderStructure();
    }

    public void initFolderStructure() {
        File folder = new File(defaultURL);
        if (!folder.exists()) {
            folder.mkdirs();
            folder.mkdir();
        }
    }

    public void safeStatistics(HashMap<StatistikManager.StatisticCategory, Counter> statistics) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
            boolean exists = new File(defaultURL + "statistics.scholl").exists();
            FileWriter writer = new FileWriter(defaultURL + File.separator + "statistics.scholl", true);
            String s = "";
            if (!exists) {
                for (StatistikManager.StatisticCategory cat : StatistikManager.StatisticCategory.values()) {
                    s += "[" + cat.name() + "]\t";
                }
                s.substring(0, s.length()-2);
                s+="\n";
                writer.write(s);
                writer.flush();
            }

            s = format.format(new Date()) + "\t";
            for (StatistikManager.StatisticCategory cat : StatistikManager.StatisticCategory.values()) {
                s += (statistics.containsKey(cat) ? statistics.get(cat).getVal() : 0) + "\t";
            }
            s.substring(0, s.length()-2); //Entferne das letzte Tab
            s += "\n";
            writer.write(s);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pair<String, Activity.ActivityType>> loadStatus() {
        SchollBot.sendConsoleMessage("Lade Statusmeldungen", Debug.LOW);
        List<Pair<String, Activity.ActivityType>> list = new LinkedList<>();

        File file = new File(defaultURL + File.separator + "status.scholl");
        if (!file.exists())
            return list;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(defaultURL + File.separator + "status.scholl"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split("\\t");
                if (arr.length == 2) {
                    String message = arr[1];
                    Activity.ActivityType type = Activity.ActivityType.valueOf(arr[0].toUpperCase());
                    Pair<String, Activity.ActivityType> pair = new Pair<>(message, type);
                    list.add(pair);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Status geladen. " + list.size() + " Einträge gefunden!");
        return list;
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
