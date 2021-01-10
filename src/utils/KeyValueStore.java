package utils;

import java.io.*;

public class KeyValueStore {

    private static final String wvcRootPath = File.separator + "wvc" + File.separator;
    private static String workingDirectory = null;
    private static String objectsPath;
    private static String refsPath;
    private static String logsPath;


    private KeyValueStore() {
    }

    public static String getWvcRootPath() {
        return wvcRootPath;
    }

    // 创建文件夹的方法
    public static void makeDirs(String path) {
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdirs();
    }

    // 创建文件的方法
    public static void createFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    // 创建存储KVObjects的文件，并返回对象文件的绝对路径
    public static String createObjectFile(String hashcode) throws IOException {
        String foldername, filename, folderpath, filepath;
        // 以git的存储结构保存object
        foldername = hashcode.substring(0, 2);
        filename = hashcode.substring(2);
        folderpath = objectsPath + foldername;
        filepath = folderpath + File.separator + filename;

        makeDirs(folderpath);
        createFile(filepath);

        return filepath;
    }

    // 向文件中写入内容，并根据要求选择是否覆写，若文件不存在则创建文件。
    public static void writeToFile(String content, String filepath, boolean overWrite) throws IOException {
        if (overWrite == true) {
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
            out.write(content);
            out.flush();
            out.close();
        } else {
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath, true));
            out.write(System.getProperty("line.separator"));
            out.write(content);
            out.flush();
            out.close();
        }
    }

    // 根据文件的hash值(key)返回文件内容
    public static String getValueByKey(String key) {
        String filepath = getStoragePath(key);
        String value = null;

        try {
            value = readFileContent(filepath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    // 读取文件内容的方法
    public static String readFileContent(String filepath) throws IOException {
        StringBuffer sb = new StringBuffer();
        File file = new File(filepath);
        if (file.exists()) {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String temp;

            // 读取文件内容
            temp = in.readLine();
            while (temp != null) {
                sb.append(temp + System.getProperty("line.separator"));
                temp = in.readLine();
            }
            return sb.toString();
        }
        return null;
    }

    public static String getRefsPath() {
        return refsPath;
    }

    public static String getObjectsPath() {
        return objectsPath;
    }

    public static String getLogsPath() {
        return logsPath;
    }

    public static String getWorkingDirectory() {
        return workingDirectory;
    }

    public static void setWorkingDirectory(String wd) {
        workingDirectory = wd;
        objectsPath = workingDirectory + wvcRootPath + "objects" + File.separator;
        refsPath = workingDirectory + wvcRootPath + "refs" + File.separator;
        logsPath = workingDirectory + wvcRootPath + "logs" + File.separator;
    }

    public static String getStoragePath(String hashcode) {
        return objectsPath + hashcode.substring(0, 2) + File.separator + hashcode.substring(2);
    }
}

