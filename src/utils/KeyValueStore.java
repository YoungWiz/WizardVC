package utils;

import objects.Blob;
import objects.Tree;

import java.io.*;

/*
 * 待完成任务：
 * 1. 以二进制方式存取文件
 * 2. 优化文件读取，减少内存占用
 * */
public class KeyValueStore {

    private static final String wvcRootPath = File.separator + "wvc" + File.separator;
    private static final String objectsPath = wvcRootPath + "objects" + File.separator;
    private static final String refsPath = wvcRootPath + "refs" + File.separator;
    private static final String logsPath = wvcRootPath + "logs" + File.separator;
    private static String workingDirectory = null;

    private KeyValueStore() {
    }

    public static String getWvcRootPath() {
        return wvcRootPath;
    }

    // 初始化时，在当前目录新建用于保存文件的objects文件夹
    public static void initialize() {
        makeDirs(workingDirectory + objectsPath);
        makeDirs(workingDirectory + refsPath);
        makeDirs(workingDirectory + logsPath);
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

    // 读取文件内容的方法
    public static String readFileContent(String filepath) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader in = new BufferedReader(new FileReader(filepath));
        String temp;

        // 读取文件内容
        temp = in.readLine();
        while (temp != null) {
            sb.append(temp + " ");
            temp = in.readLine();

        }
        return sb.toString();
    }

    // 创建存储KVObjects的文件，并返回对象文件的绝对路径
    public static String createObjectFile(String hashcode) throws IOException {
        String foldername, filename, folderpath, filepath;
        // 以git的存储结构保存object
        foldername = hashcode.substring(0, 2);
        filename = hashcode.substring(2);
        folderpath = workingDirectory + objectsPath + foldername;
        filepath = folderpath + File.separator + filename;

        makeDirs(folderpath);
        createFile(filepath);

        return filepath;
    }

    public static void writeToFile(String content, String filepath) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
        out.write(content);
        out.flush();
        out.close();
    }

    // 给定String类型的文件内容，进行Key-Value存储，并返回文件内容的hash值
    public static String kvStore(String value) {
        // 初始化objects文件夹
        initialize();
        String hashcode = null;

        try {
            // 以git的存储结构保存object
            hashcode = Hash.stringHash(value);
            String filepath = createObjectFile(hashcode);

            // 写入文件内容
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
            out.write(value);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashcode;
    }

    // 根据文件的hash值(key)返回文件内容
    public static String returnValue(String key) {
        String filepath = workingDirectory + objectsPath + key.substring(0, 2) + File.separator + key.substring(2);
        String value = null;

        try {
            value = readFileContent(filepath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getRefsPath() {
        return refsPath;
    }

    public static String getLogsPath() {
        return logsPath;
    }

    public static String getWorkingDirectory() {
        return workingDirectory;
    }

    public static void setWorkingDirectory(String wd) {
        workingDirectory = wd;
    }
}

