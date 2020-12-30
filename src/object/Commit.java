package object;

import functions.KeyValueStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/*
 * 待实现的功能：
 * 1. 添加作者信息
 * 2. 添加commit注释
 * */
public class Commit extends KVObject {
    private Tree root;
    private Commit parent = null;
    private String authorInfo = "WizardVC 1.0";
    private String message = "new commit";
    private Date date;

    public Commit(String path) {
        // 设定commit对象对应的根目录
        File dir = new File(path);
        if (dir.isFile()) {
            root = new Tree(dir.getParentFile());
        } else if (dir.isDirectory()) {
            root = new Tree(dir);
        }

        // 设定日期
        date = new Date();

        objectType = "commit";
        key = root.getKey();
    }

    public Commit getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return root.toString() + "\n" + "parent " + parent.getKey() + "\n" + authorInfo + "\n" + date.toString();
    }

    @Override
    public void store() {
        // 若当前为首次commit，将HEAD指向当前提交
        if (Head.currentCommit == null) {
            Head.currentCommit = this;
            Head.store();
            try {
                KeyValueStore.commitStore(this);
                KeyValueStore.treeStore(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 若存在之前的提交，则更新parent指针，并将HEAD指向当前提交
        else if (root.key != Head.currentCommit.key) {
            parent = Head.currentCommit;
            Head.currentCommit = this;
            Head.store();
            try {
                KeyValueStore.commitStore(this);
                KeyValueStore.treeStore(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 存放head指针的内部类
    private static class Head {
        public static Commit currentCommit = null;
        private static String headPath = KeyValueStore.getWvcRootPath() + "HEAD" + File.separator;
        private static String headFilePath = headPath + "head";

        public static void store() {
            KeyValueStore.makeDirs(headFilePath);
            try {
                KeyValueStore.createFile(headFilePath);
                // 写入文件内容
                BufferedWriter out = new BufferedWriter(new FileWriter(headFilePath));
                out.write(Head.currentCommit.getKey());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        public Commit getCurrentCommit() {
//            return currentCommit;
//        }
//        public void setCurrentCommit(Commit commit) {
//            currentCommit = commit;
//        }
    }
}
