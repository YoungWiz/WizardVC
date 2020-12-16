package object;

import functions.Hash;

import java.io.File;
import java.security.MessageDigest;

/*
 * 待完成的任务：
 * 1. 实现toString方法
 * */
public class Tree extends KVObject {

    private final String filepath;
    private final File[] files;

    // 根据路径创建tree对象
    public Tree(String path) {
        objectType = "tree";
        filepath = path;
        filename = filepath.substring(filepath.lastIndexOf(File.separator));
        File dir = new File(filepath);
        files = dir.listFiles();

        // 计算tree对象的hash值
        File treeRoot = new File(filepath);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            Hash.treeHash(treeRoot, md);
            key = Hash.digest(md);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据File对象创建tree对象
    public Tree(File file) {
        objectType = "tree";
        filepath = file.getAbsolutePath();
        filename = file.getName();
        File dir = new File(filepath);
        files = dir.listFiles();

        // 计算tree对象的hash值
        File treeRoot = new File(filepath);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            Hash.treeHash(treeRoot, md);
            key = Hash.digest(md);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilepath() {
        return filepath;
    }

    public File[] getFiles() {
        return files;
    }

    @Override
    public void store() {

    }

}
