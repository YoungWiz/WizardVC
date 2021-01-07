package refs;

import utils.KeyValueStore;

import java.io.File;
import java.io.IOException;

public class Branch {
    private String branchName, refsPath, logsPath, associatedCommitID;

    public Branch(String branchName) throws IOException {
        this.branchName = branchName;
        refsPath = KeyValueStore.getRefsPath() + this.branchName;
        logsPath = KeyValueStore.getLogsPath() + this.branchName;

        File refsFile = new File(refsPath);
        File logsFile = new File(logsPath);

        // 若refs文件和logs文件均不存在，则生成存储文件；若存在，则对associatedCommitID进行赋值
        if (!refsFile.exists() && !logsFile.exists()) {
            KeyValueStore.createFile(this.getRefsPath());
            KeyValueStore.createFile(this.getLogsPath());
        } else {
            associatedCommitID = KeyValueStore.readFileContent(refsPath).replaceAll("\r|\n", "");
        }
    }

    public String getAssociatedCommitID() {
        return associatedCommitID;
    }

    public String getRefsPath() {
        return refsPath;
    }

    public String getLogsPath() {
        return logsPath;
    }

    public String getBranchName() {
        return branchName;
    }

    public void pointTo(String commitID) {
        associatedCommitID = commitID;
    }

    public void store() {
        try {
            KeyValueStore.writeToFile(this.getAssociatedCommitID(), this.getRefsPath(), true);
            KeyValueStore.writeToFile(this.getAssociatedCommitID(), this.getLogsPath(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 更新refs指针和log
    public void update() {
        try {
            KeyValueStore.writeToFile(this.getAssociatedCommitID(), this.getRefsPath(), true);
            KeyValueStore.writeToFile(this.getAssociatedCommitID(), this.getLogsPath(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

