package object;

import functions.Hash;
import java.security.MessageDigest;

public class Commit extends KVObject {
    private final Commit lastcommit;
    private final Tree TreeRoot;

    //根据tree和lastcommit创建Commit对象
    public Commit(Tree TreeRoot, Commit lastcommit) {
        objectType = "commit";
        this.TreeRoot = TreeRoot;
        this.lastcommit = lastcommit;

        // 计算commit对象的hash值
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-1");
            Hash.commitHash(TreeRoot, lastcommit, md);
            key = Hash.digest(md);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void store() {

    }
}
