package objects;

import utils.Hash;
import utils.KeyValueStore;

import java.io.*;
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
            String objectFilePath = KeyValueStore.createObjectFile(this.getKey());
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(objectFilePath));
            byte[] bytes = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
//            KeyValueStore.writeToFile(KeyValueStore.readFileContent(this.filepath), objectFilePath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return objectType + " " + key + "\t" + filename;
    }
}
