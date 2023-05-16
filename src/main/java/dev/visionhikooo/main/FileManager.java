package dev.visionhikooo.main;

import java.io.*;

public class FileManager {

    private String defaultURL = System.getProperty("user.dir") + File.separator + "Data";;

    public FileManager() {
        initFolderStructure();
    }

    public void initFolderStructure() {
        File folder = new File(defaultURL);
        if (!folder.exists()) {
            folder.mkdirs();
            folder.mkdir();
        }
    }

    public File getFile(String path, String fileName) {
        try {
            new File(defaultURL + (path.isEmpty()?"":File.separator) + path).mkdirs();
            File file = new File(defaultURL + (path.isEmpty()?"":File.separator) + path + File.separator + fileName);

            if (!file.exists())
                file.createNewFile();

            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        File file = getFile(path, fileName);
        writeObjectToFile(file, obj);
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
        File file = getFile(path, fileName);
        return getObjectFromFile(fileName);
    }

    public Object getObjectFromFile(String fileName) {
        return getObjectFromFile("", fileName);
    }
}
