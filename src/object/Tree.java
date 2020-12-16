package object;

import functions.Hash;
import functions.KeyValueStore;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

/*
 * 待完成的任务：
 * 1. 实现toString方法
 * */
public class Tree extends KVObject {

    private final String filepath;
    private final KVObject[] objects;

    // 根据路径创建tree对象
    public Tree(String path) {
        objectType = "tree";
        filepath = path;
        filename = filepath.substring(filepath.lastIndexOf(File.separator));

        File root = new File(filepath);
        File[] files = root.listFiles();
        objects = new KVObject[files.length];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                objects[i] = new Tree(files[i]);
            }
            if (files[i].isFile()) {
                objects[i] = new Blob(files[i]);
            }
        }

        // 计算tree对象的hash值
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            Hash.treeHash(root, md);
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

        File root = new File(filepath);
        File[] files = root.listFiles();
        objects = new KVObject[files.length];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                objects[i] = new Tree(files[i]);
            }
            if (files[i].isFile()) {
                objects[i] = new Blob(files[i]);
            }
        }

        // 计算tree对象的hash值
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            Hash.treeHash(root, md);
            key = Hash.digest(md);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilepath() {
        return filepath;
    }

    public KVObject[] getObjects() {
        return objects;
    }

    @Override
    public void store() {
        try {
            KeyValueStore.treeStore(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return objectType + " " + key + "\t" + filename;
    }

}
