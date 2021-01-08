package utils;

import objects.KVObject;
import org.junit.Before;
import org.junit.Test;
import refs.Head;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CommandsTest {
    private String testpath = "/Users/wangshiranwang/Documents/课件/java的副本";
    private String testfalsepath = "Users/wangshiranwang/Documents/课件/test";
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(output));
    }

    @Test
    public void setWorkingDirectoryTest() {
        //测试文件路径不存在时
        Commands.setWorkingDirectory(testfalsepath);
        assertEquals("Path not found.\n", output.toString());
        //测试文件路径存在，判断是否被设置为WorkingDirectory
        Commands.setWorkingDirectory(testpath);
        assertEquals(testpath, KeyValueStore.getWorkingDirectory());
    }

    @Test
    public void initializeTest() {
        //测试文件路径不存在时
        Commands.initialize();
        assertEquals("failure: Working directory has not been set. Use 'wvc set' to set working directory.\n", output.toString());
        //测试文件路径存在,判断是否在相应路径中成功添加文件
        Commands.setWorkingDirectory(testpath);
        Commands.initialize();
        assertEquals(true, new File(testpath + KeyValueStore.getWvcRootPath()).exists());
        assertEquals(true, new File(KeyValueStore.getObjectsPath()).exists());
        assertEquals(true, new File(KeyValueStore.getLogsPath()).exists());
        assertEquals(true, new File(KeyValueStore.getRefsPath()).exists());
    }


    @Test
    public void addTest() throws Exception {
        //测试文件路径不存在时
        Commands.add("test.txt");
        assertEquals("failure: '" + null + File.separator + "test.txt" + "' did not match any files.\n", output.toString());
        //测试文件目录存在时
        Commands.setWorkingDirectory(testpath);
        Commands.initialize();
        //添加前没有df文件夹
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA-1");
        Hash.fileHash(new File("/Users/wangshiranwang/Documents/课件/java的副本/Java-11.pdf"),messageDigest);
        String key =Hash.digest(messageDigest);
        assertEquals(false, new File(KeyValueStore.getObjectsPath()+File.separator+key.substring(0,2)).exists());
        Commands.add("Java-11.pdf");
        //添加后生成了文件夹,名称是文件hash值的前两位
        assertEquals(true, new File(KeyValueStore.getObjectsPath()+File.separator+key.substring(0,2)).exists());
        //测试文件名是文件内容的hash值的后38位
        File[] files =new File(KeyValueStore.getObjectsPath()+File.separator+key.substring(0,2)).listFiles();
        File f =files[0];
        assertEquals(key.substring(2),f.getName());

    }

    @Test
    public void commitTest() {
        //测试文件路径不存在时
        Commands.commit("message1");
        assertEquals("No changes added to commit. Use 'wvc add' to add files.\n",output.toString());
        //rootTree不存在时
        //Commands.setWorkingDirectory(testpath);
       // Commands.initialize();
        //Commands.commit("message2");
       // assertEquals("No changes added to commit. Use 'wvc add' to add files.\n",output.toString());
    }

    @Test
    public void branchTest() {
        Commands.setWorkingDirectory(testpath);
        Commands.initialize();
        Commands.add("Java-11.pdf");
        Commands.commit("message1");
        Commands.branch("new branch");
        assertEquals(true,new File(KeyValueStore.getRefsPath()+"/main/").exists());
        assertEquals(true,new File(KeyValueStore.getRefsPath()+"/new branch/").exists());
    }


    @Test
    public void resetTest() throws IOException {
        //回滚的commitID不存在时
       Commands.reset("b23c18437afdfb5ba26d9ada5a5aa6d00392c2d");
       assertEquals("failure: Unknown revision to WizardVC. Use 'wvc log' to view commit history.\n",output.toString());
        //正常回滚
        Commands.setWorkingDirectory(testpath);
        Commands.initialize();
        Commands.add("Java-11.pdf");
        Commands.commit("message1");
        Commands.add("Java-09.pdf");
        Commands.commit("message2");
        Commands.reset("959fb135f3921fd3873d4d39d52d2403b6890775");
        //refs中工作分支中的信息应该更新
        assertEquals("959fb135f3921fd3873d4d39d52d2403b6890775\n",KeyValueStore.readFileContent(Head.getWorkingBranchPath()));
    }

}