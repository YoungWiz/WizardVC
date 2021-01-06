package utils;

import objects.Blob;
import objects.Tree;
import objects.Commit;
import refs.Branch;
import refs.Head;

import java.io.*;
import java.util.ArrayList;

public class Commands {
    // 用于关联add过程中已添加的object的tree对象，对应工作区的根目录
    private static Tree rootTree;

    private Commands() {
    }

    private static void setRootTree(String rootPath) {
        rootTree = new Tree(rootPath);
    }

    // 根据绝对路径设置工作区目录的方法
    public static void setWorkingDirectory(String absolutePath) {
        File file = new File(absolutePath);
        if (file.exists() && file.isDirectory()) {
            KeyValueStore.setWorkingDirectory(absolutePath);
        } else {
            System.out.println("Path not found.");
        }
    }

    public static void setAuthor(String authorName) {
        Commit.setAuthorInfo(authorName);
    }

    public static void initialize() {
        if (KeyValueStore.getWorkingDirectory() == null) {
            System.out.println("failure: Working directory has not been set. Use 'wvc set' to set working directory.");
            return;
        }
        // 判断当前工作区是否已经初始化
        File file = new File(KeyValueStore.getWorkingDirectory() + File.separator + "wvc");
        if (file.exists()) {
            System.out.println("Reinitialized existing wvc repository in " + KeyValueStore.getWorkingDirectory() + KeyValueStore.getWvcRootPath());
            return;
        }

        setRootTree(KeyValueStore.getWorkingDirectory());
        // 创建wvc文件目录
        KeyValueStore.makeDirs(KeyValueStore.getRefsPath());
        KeyValueStore.makeDirs(KeyValueStore.getLogsPath());
        KeyValueStore.makeDirs(KeyValueStore.getObjectsPath());
        // 创建HEAD文件
        try {
            KeyValueStore.createFile(Head.getHeadPath());
            System.out.println("Initialized empty wvc repository in " + KeyValueStore.getWorkingDirectory() + KeyValueStore.getWvcRootPath());
//            Head.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void add(String relativePath) {
        String filePath = KeyValueStore.getWorkingDirectory() + File.separator + relativePath;

        // add的文件不存在时的处理
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("failure: '" + filePath + "' did not match any files.");
            return;
        }
        // 若add的对象为文件，则直接转化为blob存储
        if (file.isFile()) {
            Blob blob = new Blob(file);
            rootTree.addObject(blob);
            blob.store();
        }
        // 若add的对象为文件夹，则转化为tree，并递归地存储该文件夹下的文件/文件夹
        if (file.isDirectory()) {
            Tree subTree = new Tree(file);
            rootTree.addObject(subTree);
            try {
                subTree.convert();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void commit(String message) {
        if (KeyValueStore.getWorkingDirectory().equals(null) || !rootTree.containsObjects()) {
            System.out.println("No changes added to commit. Use 'wvc add' to add files.");
        }
        Commit newCommit = new Commit(rootTree);
        newCommit.setMessage(message);
        // 当前提交为初次提交的处理
        if (Head.getWorkingBranch() == null) {
            try {
                Branch mainBranch = new Branch("main");
                mainBranch.setPointTo(newCommit.getKey());
                Head.setWorkingBranch(mainBranch);
                mainBranch.store();
                newCommit.store();
                rootTree.store();
                rootTree.clearObjects();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Branch workingBranch = new Branch(Head.getWorkingBranch());
                String parentCommitID = workingBranch.getAssociatedCommitID();
                String parentCommitContent = KeyValueStore.getValueByKey(parentCommitID);
                int beginIndex = parentCommitContent.indexOf("tree") + 5;
                int endIndex = parentCommitContent.indexOf("\t", beginIndex);
                String parentTreeID = parentCommitContent.substring(beginIndex, endIndex);

                // 判断本次提交是否有文件发生改动，将本次提交的tree hashcode和上次提交的tree hashcode进行比较，若相同则拒绝提交
                if (newCommit.getTreeID().equals(parentTreeID)) {
                    System.out.println("failure: No files changed.");
                    rootTree.clearObjects();
                    return;
                }
                newCommit.setParent(parentCommitID);
                newCommit.store();
                workingBranch.setPointTo(newCommit.getKey());
                workingBranch.update();

                rootTree.store();
                rootTree.clearObjects();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void branch(String branchName) {
        File file = new File(KeyValueStore.getRefsPath() + branchName);
        if (file.exists()) {
            System.out.println("failure: A branch named '" + branchName + "' already exists.");
        }
        try {
            Branch newBranch = new Branch(branchName);
            Branch currentBranch = new Branch(Head.getWorkingBranch());
            newBranch.setPointTo(currentBranch.getAssociatedCommitID());
            newBranch.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 更改当前工作的分支
    public static void switchBranches(String branchName) {
        if (rootTree.containsObjects()) {
            System.out.println("You have uncommitted changes, Please commit them before you switch branches");
            return;
        }
        File file = new File(KeyValueStore.getRefsPath() + branchName);
        if (!file.exists()) {
            System.out.println("failure: Branch '" + branchName + "' did not match any existing branches");
            return;
        }
        try {
            Head.setWorkingBranch(new Branch(branchName));
            System.out.println("Switched to branch " + branchName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 返回当前已有的branch
    public static String listBranches() {
        if (KeyValueStore.getWorkingDirectory() == null || Head.getWorkingBranch() == null) {
            return "failure: No branches to list.";
        }
        String listOfBranches = "";
        File[] files = new File(KeyValueStore.getRefsPath()).listFiles();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            listOfBranches += file.getName() + System.getProperty("line.separator");
        }
        listOfBranches += "Currently on " + Head.getWorkingBranch();
        return listOfBranches;
    }

    // 返回工作区中的文件/文件夹
    public static String listFiles() {
        if (KeyValueStore.getWorkingDirectory() == null) {
            return "failure: Working directory is not set.";
        }
        String listOfFiles = "";
        if (KeyValueStore.getWorkingDirectory() == null) {
            System.out.println("failure: Working directory has not been set.");
            return null;
        }
        File[] files = new File(KeyValueStore.getWorkingDirectory()).listFiles();
        for (File file : files) {
            if (!file.getName().equals("wvc")) {
                listOfFiles += file.getName() + System.getProperty("line.separator");
            }
        }
        return listOfFiles;
    }

    // 返回当前分支的commit history
    public static String getLogs() {

        if (KeyValueStore.getWorkingDirectory() == null || Head.getWorkingBranchPath() == null) {
            return null;
        }

        try {
            String logs = "";
            ArrayList<String> commitsList = new ArrayList<>();
            String commitId = KeyValueStore.readFileContent(Head.getWorkingBranchPath()).replaceAll("\r|\n", "");
            do {
                commitsList.add(commitId);
                commitId = getParentCommit(commitId);
            } while (!commitId.equals("null"));
            for (String commit : commitsList) {
                logs += KeyValueStore.getValueByKey(commit) + System.getProperty("line.separator");
            }
            return logs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getParentCommit(String commitID) {
        String commitContent = KeyValueStore.getValueByKey(commitID);
        return commitContent.substring(commitContent.indexOf("parent: ") + 8, commitContent.indexOf("\n", commitContent.indexOf("parent: "))).replaceAll("\r|\n", "");
    }

    // 返回所有分支的历史提交
    public static String getRefLogs() {

        if (KeyValueStore.getWorkingDirectory() == null || KeyValueStore.getLogsPath() == null) {
            return null;
        }

        File[] files = new File(KeyValueStore.getLogsPath()).listFiles();
        String refLogs = "";
        for (File logFile : files) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile));
                String commitID = bufferedReader.readLine();
                while (commitID != null) {
                    refLogs += "Branch: " + logFile.getName() + System.getProperty("line.separator");
                    refLogs += KeyValueStore.getValueByKey(commitID) + System.getProperty("line.separator");
                    commitID = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return refLogs;
    }

    // 回退到指定的commit，恢复该commit中的文件夹内容，并更新branch指针
    public static void reset(String commitID) {
        String commitContent = KeyValueStore.getValueByKey(commitID);
        if (commitContent == null) {
            System.out.println("failure: Unknown revision to WizardVC. Use 'wvc log' to view commit history.");
            return;
        }
        String treeID = commitContent.substring(5, commitContent.indexOf("\t"));
        rollBack(new File(KeyValueStore.getStoragePath(treeID)), KeyValueStore.getWorkingDirectory());

        // 更新branch指针
        try {
            Branch branch = new Branch(Head.getWorkingBranch());
            branch.setPointTo(commitID);
            branch.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void rollBack(File treeStoragefile, String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(treeStoragefile));
            String record = bufferedReader.readLine();
            while (record != null) {
                String objectType = record.substring(0, 4);
                String key = record.substring(5, record.indexOf("\t"));
                String filename = record.substring(record.indexOf("\t") + 1);

                if (objectType.equals("blob")) {
                    String restoreFilePath = path + File.separator + filename;
                    KeyValueStore.createFile(restoreFilePath);
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(KeyValueStore.getStoragePath(key)));
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(restoreFilePath));
                    byte[] bytes = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(bytes)) > 0) {
                        outputStream.write(bytes, 0, bytesRead);
                    }
                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();
//                    KeyValueStore.writeToFile(KeyValueStore.getValueByKey(key), restoreFilePath, true);
                }
                if (objectType.equals("tree")) {
                    String restoreFilePath = path + File.separator + filename;
                    KeyValueStore.makeDirs(restoreFilePath);
                    rollBack(new File(KeyValueStore.getStoragePath(key)), restoreFilePath);
                }
                record = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void deleteDir(File file) {
//        if (file.isDirectory()) {
//            for (File f : file.listFiles())
//                deleteDir(f);
//        }
//        file.delete();
//    }
}
