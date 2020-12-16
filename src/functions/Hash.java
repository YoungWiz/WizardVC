package functions;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class Hash {

    // 计算字符串hash值的方法
    public static String stringHash(String content) throws Exception {
        // 设定hash算法为SHA-1
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(content.getBytes());
        // 返回messageDigest对象的hash值
        return digest(md);
    }

    // 计算File对象hash值的方法
    public static void fileHash(File file, MessageDigest md) throws Exception {
        FileInputStream input = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead;
        do {
            // 从输入流中读出numRead字节到buffer中
            numRead = input.read(buffer);
            // 当读出的字节数大于零时，根据读出的字节更新MessageDigest对象
            if (numRead > 0) {
                md.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        // 关闭输入流
        input.close();
    }

    public static void treeHash(File treeRoot, MessageDigest md) throws Exception {
        File[] files = treeRoot.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 若该对象为文件，则计算该文件的hash值，更新md
            if (files[i].isFile()) {
                fileHash(files[i], md);
            }
            // 若该对象为文件夹，则递归遍历
            if (files[i].isDirectory()) {
                treeHash(files[i], md);
            }
        }
    }

    public static String digest(MessageDigest md) {
        byte[] sha256 = md.digest();
        // 将hash值转换为字符串
        String result = "";
        for (byte i : sha256) {
            result += (Integer.toString((i >> 4) & 0x0F, 16) + Integer.toString(i & 0x0F, 16));
        }

        return result;
    }
}


