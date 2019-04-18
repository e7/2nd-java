package org.e7.s2nd;

import org.e7.s2nd.codec.SjsonbCodec;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;


public class SjsonbTest
{
    private ObjectPool<SjsonbCodec> objPool;

    @Before
    public void init() {
        GenericObjectPoolConfig pool_cnf = new GenericObjectPoolConfig();
        pool_cnf.setMaxTotal(10); // 最多10个对象
        pool_cnf.setMinIdle(1); // 至少一个空闲对象
        pool_cnf.setTestOnBorrow(true); // borrow前测试对象有效性

        objPool = new GenericObjectPool<>(
                new XauthConnectionFactory("localhost", 10002), pool_cnf
        );
    }

    /**
     * 从池中分配连接
     * @return
     */
    private SjsonbCodec allocConnection() throws Exception {
        int retry = 1;

        for (;;) {
            SjsonbCodec codec = objPool.borrowObject();

            try {
                codec.encodeToOutputStream("{\"api\":\"ping\"}".getBytes());
                codec.decodeFromInputStream();
                return codec;
            } catch (Exception e) {
                // 丢弃连接
                codec.close();
                objPool.invalidateObject(codec);
                if (retry < 1) {
                    throw e;
                }
                --retry;
            }
        }
    }

    /**
     * 归还连接
     * @param codec
     */
    private void freeConnection(SjsonbCodec codec) {
        if (null == codec) {
            return;
        }

        try {
            objPool.returnObject(codec);
        } catch (Exception e) {
            assert false;
        }
    }

    /**
     * 废弃连接
     * @param codec
     */
    private void dropConnection(SjsonbCodec codec) {
        if (null == codec) {
            return;
        }

        codec.close();
        try {
            objPool.invalidateObject(codec);
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    public void shouldAnswerWithTrue()
    {
        SjsonbCodec codec = null;

        try {
            codec = allocConnection();
            codec.encodeToOutputStream(("{" +
                    "\"api\":\"settoken\",\"area\":\"mophone-mini-app\"," +
                    "\"id\":\"1\",\"devid\":\"xxxx-xxxx-xxxx\"," +
                    "\"info\":\"eyJ1aWQiOjF9\"}").getBytes());
            System.out.println(new String(codec.decodeFromInputStream()));
            freeConnection(codec);
        } catch (Exception e) {
            if (codec != null) {
                dropConnection(codec);
            }
        }
    }
}
