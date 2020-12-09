import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

public class keyvalue {

    //计算文件hash值
    public static String SHA1ofFile(String path) throws Exception {
        FileInputStream is = new FileInputStream(path);
        byte[] buffer = new byte[1024];
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        int length = 0;
        do {
            length = is.read(buffer);
            if (length > 0) {
                md.update(buffer, 0, length);
            }
            //read方法读取结束之后返回-1
        } while (length != -1);
        //关闭输入流
        is.close();
        byte[] sha1Byte = md.digest();
        BigInteger BigInt = new BigInteger(1, sha1Byte);
        String key = BigInt.toString(16);
        return key;
    }

    //计算字符串hash值
    public static String SHA1ofString(String value) throws Exception {
        byte[] buffer = new byte[1024];
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(value.getBytes());
        byte[] sha1Byte = md.digest();
        BigInteger BigInt = new BigInteger(1, sha1Byte);
        String key = BigInt.toString(16);
        return key;
    }

    //创建文件
    public static String createFile(String value, String key) throws IOException {
        File temp = File.createTempFile(key, ".txt");
        String temppath=temp.getAbsolutePath();
        temp.deleteOnExit();
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        out.write(value);
        out.close();
        return temppath;

    }

    //给定key 读取文件value
    public static String read(String key, String path) throws IOException {
        if (path == null || path.trim().length() == 0) {
            return "找不到该文件";
        }
        File file = new File(path);
        File[] subfile = file.listFiles();
        for (File fileTemp : subfile) {
            String filename = fileTemp.getName();
            if (fileTemp.isFile()) {
                int lastindex = (filename.lastIndexOf(".") == -1 ? filename.length() : filename.lastIndexOf("."));
                filename = filename.substring(0, lastindex);
            }
            if(filename.contains(key)){
                return fileTemp.toString();
            }
        }

        StringBuffer sbf = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            String temp = br.readLine();
            while (temp != null) {
                sbf.append(temp);
            }
            br.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static void main(String[] args) {
        Scanner sc1 = new Scanner(System.in);
        String value = sc1.nextLine();
        System.out.println("value的key值为：");
        try {
            String key1 = SHA1ofString(value);
            System.out.println(key1);
            String path=createFile(value, key1);
            System.out.println("请输入要查找的key");
            Scanner sc2 = new Scanner(System.in);
            String key = sc2.nextLine();
            String valueofkey = read(key,path);
            System.out.println(valueofkey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


