package functions;

import java.io.*;

public class KeyValueStore {

    //初始化时，在当前目录新建用于保存文件的objects文件夹
    public static void Initialize() {
        File root = new File("./objects");
        if (!root.exists())
            root.mkdir();
    }

    //给定String类型的文件内容，进行Key-Value存储，并返回文件内容的hash值
    public static String StoreValue(String value) {
        // 初始化objects文件夹
        KeyValueStore.Initialize();
        String hashcode = null;

        try {
            String filename = null;
            //以txt格式保存文件内容
            hashcode = Hashcode.GetHashCode(value);
            filename = hashcode + ".txt";
            String filepath = "./objects/" + filename;

            //创建文件
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }

            //写入文件内容
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
            out.write(value);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashcode;
    }

    //根据文件的hash值(key)返回文件内容
    public static String ReturnValue(String key) {
        String filepath = "./objects/" + key + ".txt";
        StringBuffer sb = new StringBuffer();

        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            String temp = null;

            //读入文件内容
            temp = in.readLine();
            while (temp != null) {
                sb.append(temp + " ");
                temp = in.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    public static void main(String[] args) {
        String test = "测试12345";
        String key = KeyValueStore.StoreValue(test);
        System.out.println(KeyValueStore.ReturnValue(key));
    }

}
