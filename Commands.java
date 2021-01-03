package utils;

import objects.Blob;
import objects.Tree;
import objects.Commit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Commands {
    // 用于存放add过程中已添加的object的arraylist
    private static Tree rootTree;

    private Commands() {
    }

    public static void setRootTree(String rootPath) {
        rootTree = new Tree(rootPath);
    }

    // 根据绝对路径设置工作区目录的方法
    public static void setWorkingDirectory(String absolutePath) {
        KeyValueStore.setWorkingDirectory(absolutePath);
    }

    public static void initialize() {
        if (KeyValueStore.getWorkingDirectory() == null) {
            System.out.println("Working directory has not been set." + "\n" + "Use \"wvc set\" to set working directory.");
            return;
        }
        // 判断当前工作区是否已经初始化
        File file = new File(KeyValueStore.getWorkingDirectory() + File.separator + "wvc");
        if (file.exists()) {
            System.out.println("Reinitialized existing Wvc repository in " + KeyValueStore.getWorkingDirectory() + KeyValueStore.getWvcRootPath());
            return;
        }

        setRootTree(KeyValueStore.getWorkingDirectory());
        // 创建wvc文件目录
        KeyValueStore.makeDirs(KeyValueStore.getRefsPath());
        KeyValueStore.makeDirs(KeyValueStore.getLogsPath());
        KeyValueStore.makeDirs(KeyValueStore.getObjectsPath());
        // 创建HEAD文件
//        try {
//            KeyValueStore.createFile(Head.getHeadPath());
//            Head.update();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void add(String relativePath) {
        String filePath = KeyValueStore.getWorkingDirectory() + File.separator + relativePath;

        // add的文件不存在时的处理
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println(filePath + " did not match any files.");
            return;
        }
        // 若add的对象为文件，则直接转化为blob存储
        if (file.isFile()) {
            Blob blob = new Blob(file);
            rootTree.getObjects().add(blob);
            blob.store();
        }
        // 若add的对象为文件夹，则转化为tree，并递归地存储该文件夹下的文件/文件夹
        if (file.isDirectory()) {
            Tree subTree = new Tree(file);
            rootTree.getObjects().add(subTree);
            try {
                subTree.convert();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void commit(String message) {
        Commit newCommit = new Commit(rootTree);
        newCommit.setMessage(message);
    }

    public static void branch(String branchName) {

    }

//    public static String showBranches() {
//
//    }

//    public static String log() {
//
//    }

    public static void rollBack(String commitkey,String path)throws Exception{
        String commitvalue=KeyValueStore.returnValue(commitkey);
        String[] value=commitvalue.split(" ");
        String rootkey=value[1];
        reset(rootkey,path);
    }
    public static void reset(String rootkey, String path) throws Exception {
        String content = KeyValueStore.returnValue(rootkey);
        String [] split = content.split(" ");
        int i = 0;
        //数组中每三个构成一组 分别是type，key，name
        while(i < split.length) {
            if(split[i] == "tree") {
                resetFileFolder(split[i+2], path);
                reset(split[i+1], path + File.separator + split[i+2]);
                i = i + 3;
                continue;
            }
            if(split[i] == "blob") {
                resetFile(split[i+1], split[i+2], path);
                i = i + 3;
                continue;
            }
        }
    }

    public static void resetFile(String key, String name, String path) throws Exception {
        String content = KeyValueStore.returnValue(key);
        File file = new File(path, name);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    public static void resetFileFolder(String name, String path) {
        File dir = new File(path, name);
        dir.mkdir();
    }
    public static void status() {

    }
}
