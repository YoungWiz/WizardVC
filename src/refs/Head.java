package refs;

import utils.KeyValueStore;

import java.io.IOException;

public class Head {
    private static final String headPath = KeyValueStore.getWorkingDirectory() + KeyValueStore.getWvcRootPath() + "HEAD";
    private static String workingBranch = "main";
    private static String branchPath = null;

    public static void update() {
        try {
            KeyValueStore.writeToFile(workingBranch, headPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getHeadPath(){
        return headPath;
    }

    public static String getWorkingBranch() {
        return workingBranch;
    }

    public static void setWorkingBranch(String path) {
        workingBranch = path;
    }

}
