### class commit

Commit：
包含根目录tree对象的key
包含前一次commit的key
以上两行构成本次commit的value，本次commit的key就是以上两行内容的哈希
第一次commit没有前一次commit



父类：KVObject

type：commit

组成：tree+lastcommit

value：根目录tree对象的key、前一次commit的key

key；hash（value）



- 实现思路：

  知道需要tree对象需要添加的lastcommit的位置

  更新HEAD的指向：读出HEAD的内容，然后更新其内容为新的commit

  

- 总结

每一次commit对应一个tree，这个tree又记录了整个文档的目录结构，文件的每一次修改又会生成一个blob，blob信息记录在tree下面



### class KVObject

抽象类，commit、tree、blob的父类

成员变量：

​	类型：protected String objectType

​	对象hash值：protected String key

​	文件名称：protected String filename



方法：

​	获取文件类型：public String getType(){}

​	获取key：public String getKey(){}

​	获取文件名称：public String getFileName(){}

​	文件存储：public void store() {}



### class Tree



父类：KVObject

type：tree

value：根目录tree对象的key，包含文件夹内的子文件的名称、子文件夹的名称；包含每个子文件blob的key；包含每个子文件夹tree的key。

key；hash（value）



成员变量：

​	文件路径：private final String filepath;

​	子文件夹：private final File[] files;



构造方法：

​	根据路径创建tree对象：public Tree(String path){}

​	根据File对象创建tree对象：public Tree(File file){}





### class Blob



父类：KVObject

type：blob

value：文件内容

key；hash（value）



成员变量：

​	文件路径：private final String filepath;

​	

构造方法：

​	根据路径创建tree对象：public Blob(String path){}

​	根据File对象创建tree对象：public Blob(File file){}



### class KeyValueStore

方法：

```
 初始化时，在当前目录新建用于保存文件的objects文件夹：public static void initialize() {}
 创建文件夹的方法：public static void makeDirs(String path) {}
 创建文件的方法：public static void createFile(String path) throws IOException {}
 读取文件内容的方法：public static String readFileContent(String filepath) throws IOException {}
 给定String类型的文件内容，进行Key-Value存储，并返回文件内容的hash值：
    public static String storeValue(String value) {}
 根据文件的hash值(key)返回文件内容：public static String returnValue(String key) {}
    
```



### class Hash

```
计算字符串hash值的方法
    public static String stringHash(String content) throws Exception {}
计算File对象hash值的方法
    public static void fileHash(File file, MessageDigest md) throws Exception {}
计算tree的hash值
	public static void treeHash(File treeRoot, MessageDigest md) throws Exception {}
计算commit的hash值
	public static void commitHash(Tree TreeRoot,Commit lastcommit, MessageDigest md) throws Exception {}
转换成16进制的方法
	public static String digest(MessageDigest md) {}
```

