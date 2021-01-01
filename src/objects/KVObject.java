package objects;

public abstract class KVObject {
    protected String objectType;
    protected String key;
    protected String filename;

    public boolean isBlob() {
        return objectType == "blob";
    }

    public boolean isTree() {
        return objectType == "tree";
    }

    public String getKey() {
        return key;
    }

    public String getFileName() {
        return filename;
    }

    public void store() {
    }
}
