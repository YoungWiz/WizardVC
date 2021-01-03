package objects;

import utils.Hash;
import utils.KeyValueStore;

import java.io.File;
import java.util.Date;

public class Commit extends KVObject {
    private static String authorInfo = "WizardVC 1.0";
    private Tree rootTree;
    private Date date;
    // 储存父提交的hashcode
    private String parent = null;
    private String message = "a new commit";

    // 根据rootTree创建Commit对象
    public Commit(Tree rootTree) {
        date = new Date();
        this.rootTree = rootTree;
        objectType = "commit";
    }

    // 根据已有Commit的ID创建Commit对象
    public Commit(String commitID) {
        String objectFilePath = KeyValueStore.getObjectsPath() + commitID.substring(0, 2) + File.separator + commitID.substring(2);
    }

    public static void setAuthorInfo(String authorName) {
        authorInfo = authorName;
    }

    public String getParent() {
        return parent;
    }

    public String getTreeID() {
        return rootTree.key;
    }

    public void setParent(String parentHashCode) {
        parent = parentHashCode;
    }

    public void setMessage(String message) {
        this.message = message;
        try {
            this.key = Hash.stringHash(this.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return System.getProperty("line.separator") + "parent " + parent + System.getProperty("line.separator") + authorInfo + System.getProperty("line.separator") + date.toString() + System.getProperty("line.separator") + message;
    }

    @Override
    public void store() {
        try {
            String objectFilePath = KeyValueStore.createObjectFile(this.getKey());
            KeyValueStore.writeToFile(rootTree.toString() + System.getProperty("line.separator") + this.toString(), objectFilePath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
