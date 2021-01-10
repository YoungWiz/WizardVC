package objects;

public abstract class KVObject {
    protected String objectType, key, filePath, filename;

    public boolean isBlob() {
        return objectType == "blob";
    }

    public boolean isTree() {
        return objectType == "tree";
    }

    public String getKey() {
        return key;
    }

    public String getFilePath() {return filePath;}

    public void store() {
    }
}
