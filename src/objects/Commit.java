package objects;

import utils.Hash;
import utils.KeyValueStore;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

/*
 * 待实现的功能：
 * 1. 添加作者信息
 * 2. 添加commit注释
 * */
public class Commit extends KVObject {
    private static Tree rootTree;
    private final Date date;
    // 储存父提交的hashcode
    private String parent = null;
    private String authorInfo = "WizardVC 1.0";
    private String message = "a new commit";

    public Commit(Tree rootTree) {
        date = new Date();
        this.rootTree = rootTree;
        objectType = "commit";
    }


    public String getParent() {
        return parent;
    }

    public void setParent(String parentHashCode) {
        parent = parentHashCode;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "\n" + "parent " + parent + "\n" + authorInfo + "\n" + date.toString();
    }

    @Override
    public void store() {
        try {
            String objectFilePath = KeyValueStore.createObjectFile(Hash.stringHash(toString()));
            BufferedWriter out = new BufferedWriter(new FileWriter(objectFilePath));
            out.write(rootTree.toString() + this.toString());
            out.flush();
            out.close();
//            convert(rootPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
