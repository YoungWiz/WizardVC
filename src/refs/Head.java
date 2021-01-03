package refs;

import utils.KeyValueStore;

import java.io.IOException;

public class Head {
    private static final String headPath = KeyValueStore.getWorkingDirectory() + KeyValueStore.getWvcRootPath() + "HEAD";
    private static String workingBranch = null;
    private static String workingBranchPath = null;

    public static void update() {
        try {
            KeyValueStore.writeToFile(workingBranch, headPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getHeadPath() {
        return headPath;
    }

    public static String getWorkingBranch() {
        return workingBranch;
    }

    public static void setWorkingBranch(Branch branch) {
        workingBranch = branch.getBranchName();
        setWorkingBranchPath(branch.getRefsPath() + workingBranch);
    }

    public static void setWorkingBranchPath(String workingBranchPath){

    }

}
