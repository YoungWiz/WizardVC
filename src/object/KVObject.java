package object;

public abstract class KVObject {
    protected String objectType;
    protected String key;
    protected String filename;

    public String getType() {
        return objectType;
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
