# 2nd-java

#### Description
java二方库

#### Software Architecture
Software architecture description

#### Installation

1. xxxx
2. xxxx
3. xxxx

#### Instructions

1. xxxx
2. xxxx
3. xxxx

#### Contribution

1. Fork the project
2. Create Feat_xxx branch
3. Commit your code
4. Create Pull Request

#### 用法示例
```
GenericObjectPoolConfig pool_cnf = new GenericObjectPoolConfig();
pool_cnf.setMaxTotal(10); // 最多10个对象
pool_cnf.setMinIdle(1); // 至少一个空闲对象
pool_cnf.setTestOnBorrow(true); // borrow前测试对象有效性

// 创建连接池对象
ObjectPool<SjsonbCodec> objectPool = new GenericObjectPool<>(
    new XauthConnectionFactory("www.abc.com", 12345), pool_cnf
);

// 执行业务
String result;
SjsonbCodec codec;

try {
    codec = objectPool.borrowObject();
    try {
        codec.encodeToOutputStream("{\"api\":\"ping\"}".getBytes());
        result = new String(codec.decodeFromInputStream());
        assertTrue(XauthConnectionFactory.HB_RESPONSE.equals(result));
    } catch (IOException e) {
        codec.close();
        objectPool.invalidateObject(codec);
        codec = null;
        assertTrue(false);
    } finally {
        if (null != objectPool) {
            objectPool.returnObject(codec);
        }
    }
} catch (Exception e) {
    e.printStackTrace();
    assertTrue(false);
    return;
}
```

#### Gitee Feature

1. You can use Readme\_XXX.md to support different languages, such as Readme\_en.md, Readme\_zh.md
2. Gitee blog [blog.gitee.com](https://blog.gitee.com)
3. Explore open source project [https://gitee.com/explore](https://gitee.com/explore)
4. The most valuable open source project [GVP](https://gitee.com/gvp)
5. The manual of Gitee [https://gitee.com/help](https://gitee.com/help)
6. The most popular members  [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)