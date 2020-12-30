package object;

import functions.Hash;
import functions.KeyValueStore;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

public class Blob extends KVObject {

    private final String filepath;

    // 根据文件路径创建blob的构造方法
    public Blob(String path) {
        objectType = "blob";
        filepath = path;
        try {
            key = Hash.stringHash(KeyValueStore.readFileContent(path));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        filename = filepath.substring(filepath.lastIndexOf(File.separator));
    }

    // 根据File对象创建blob的方法
    public Blob(File file) {
        objectType = "blob";
        filepath = file.getAbsolutePath();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            Hash.fileHash(file, md);
            key = Hash.digest(md);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        filename = file.getName();
    }

    public String getFilepath() {
        return filepath;
    }

    @Override
    public void store() {
        try {
            KeyValueStore.blobStore(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return objectType + " " + key + "\t" + filename;
    }
}
