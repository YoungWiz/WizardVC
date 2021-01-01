package objects;

import utils.KeyValueStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/*
 * 待实现的功能：
 * 1. 添加作者信息
 * 2. 添加commit注释
 * 3. 用commit存储的内容计算commit对象的hashcode
 * */
public class Commit extends KVObject {
    private String rootPath;
    // 储存父提交的hashcode
    private String parent = null;
    private String authorInfo = "WizardVC 1.0";
    private String message = "a new commit";
    private final Date date;

    public Commit(String path) {
        // 设定commit对象对应的根目录
        File dir = new File(path);
        if (dir.isFile()) {
            rootPath = dir.getAbsolutePath();
        } else if (dir.isDirectory()) {
            rootPath = dir.getAbsolutePath();
        }

        // 设定日期
        date = new Date();

        objectType = "commit";
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parentHashCode) {
        parent = parentHashCode;
    }

    @Override
    public String toString() {
        return "\n" + "parent " + parent + "\n" + authorInfo + "\n" + date.toString();
    }

    @Override
    public void store() {
        try {
            Tree root = new Tree(rootPath);
            String filepath = KeyValueStore.createObjectFile(root.getKey());
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
            out.write(root.toString() + this.toString());
            out.flush();
            out.close();
            convert(rootPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 给定文件目录，将目录下的文件转化为tree和blob并保存
    private static void convert(String filepath) throws IOException {
        File dir = new File(filepath);
        File[] files = dir.listFiles();
        for (File i : files) {
            if (i.isDirectory()) {
                Tree tree = new Tree(i);
                tree.store();
                convert(i.getAbsolutePath());
            }
            if (i.isFile()) {
                Blob blob = new Blob(i);
                blob.store();
            }
        }
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
