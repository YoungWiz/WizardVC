package functions;

import java.security.MessageDigest;

/*
* 待完成任务：
* 1. SHA-1补全40位
*
* */
public class Hashcode {

    // 计算字符串hash值的方法
    public static String GetHashCode(String content) throws Exception {
        // 设定hash算法为SHA-1
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(content.getBytes());
        // 返回messageDigest对象的hash值
        byte[] sha256 = md.digest();
        // 将hash值转换为字符串
        String result = "";
        for (int i = 0; i < sha256.length; i++) {
            result += (Integer.toString((sha256[i] >> 4) & 0x0F, 16) +Integer.toString(sha256[i] & 0x0F, 16));
        }

        return result;
    }
}


