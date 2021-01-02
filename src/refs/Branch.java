package refs;

import utils.KeyValueStore;

import java.io.IOException;

public class Branch {
    private String branchName, refsPath, logsPath, pointTO;

    public Branch(String branchName) throws IOException {
        this.branchName = branchName;
        String refsPath = KeyValueStore.getRefsPath() + this.branchName;
        String logsPath = KeyValueStore.getLogsPath() + this.branchName;
        // 生成存储文件
        KeyValueStore.createFile(this.getRefsPath());
        KeyValueStore.createFile(this.getLogsPath());
    }

    public String getPointTO() {
        return pointTO;
    }

    public String getRefsPath() {
        return refsPath;
    }

    public String getLogsPath() {
        return logsPath;
    }

    public void store() {
        try {
            KeyValueStore.writeToFile(this.getPointTO(), this.getRefsPath());
            KeyValueStore.writeToFile(this.getPointTO(), this.getLogsPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

