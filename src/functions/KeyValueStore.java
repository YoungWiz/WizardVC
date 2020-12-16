package functions;

import object.Blob;
import object.Tree;

import java.io.*;

/*
 * 待完成任务：
 * 1. 以二进制方式存取文件
 * 2. tree的存储
 * */
public class KeyValueStore {

    private static final String objectsPath = File.separator + "wvc" + File.separator + "objects" + File.separator;
    public static String workingDirectory = null;

    // 初始化时，在当前目录新建用于保存文件的objects文件夹
    public static void initialize() {
        makeDirs(workingDirectory + objectsPath);
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

    // 给定String类型的文件内容，进行Key-Value存储，并返回文件内容的hash值
    public static String storeValue(String value) {
        // 初始化objects文件夹
        initialize();
        String hashcode = null;

        try {
            String foldername, filename, filepath;
            // 以git的存储结构保存object
            hashcode = Hash.stringHash(value);
            foldername = hashcode.substring(0, 2);
            filename = hashcode.substring(2);
            filepath = workingDirectory + objectsPath + foldername + File.separator + filename;

            // 创建文件夹
            makeDirs(workingDirectory + objectsPath + foldername);

            // 创建文件
            createFile(filepath);

            // 写入文件内容
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
            out.write(value);
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

    public static void blobStore(Blob blob) throws IOException {
        storeValue(readFileContent(blob.getFilepath()));
    }

    public static void treeStore(Tree treeRoot) {

    }

    // 测试
    public static void main(String[] args) {
        String test = "测试12345";
        KeyValueStore.workingDirectory = "D:/书";
        String key = KeyValueStore.storeValue(test);
        System.out.println(KeyValueStore.returnValue(key));
    }

}
