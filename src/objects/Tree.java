package objects;

import utils.Hash;
import utils.KeyValueStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;

public class Tree extends KVObject {

    private final String filepath;
    // 存放tree对象目录下的所有KVObjects
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
            Hash.dirHash(root, md);
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
            Hash.dirHash(root, md);
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
        String filepath;
        try {
            filepath = KeyValueStore.createObjectFile(this.getKey());
            // 写入tree对象的信息
            KVObject[] objects = this.getObjects();
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
            out.write(this.toString() + "\n");
            for (KVObject i : objects) {
                out.write(i.toString() + "\n");
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return objectType + " " + key + "\t" + filename;
    }
}
