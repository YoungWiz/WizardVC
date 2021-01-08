package utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class HashTest {

    @Test
    public void stringHashTest() throws Exception {
        String s1="hello world";
        String s2="hello world";
        assertEquals(s1,s2);
        assertEquals(Hash.stringHash(s1),Hash.stringHash(s2));
    }


    @Test
    public void digestTest() throws Exception {
        //检查得到的key是不是40位
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA-1");
        Hash.fileHash(new File("/Users/wangshiranwang/Documents/课件/java的副本/Java-11.pdf"),messageDigest);
        String key =Hash.digest(messageDigest);
        assertEquals(40,key.length());
    }
}