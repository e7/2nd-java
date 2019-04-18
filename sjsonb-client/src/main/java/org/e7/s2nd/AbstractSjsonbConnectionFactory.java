package org.e7.s2nd;

import org.e7.s2nd.codec.SjsonbCodec;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * @author e7
 */
public abstract class AbstractSjsonbConnectionFactory implements PooledObjectFactory<SjsonbCodec> {
    /**
     * read time out, in milliseconds
     */
    private int timeout;
    private InetSocketAddress targetAddr;

    protected AbstractSjsonbConnectionFactory(String host, int port, int timeout) {
        this.timeout = timeout * 1000;
        this.targetAddr = new InetSocketAddress(host, port);
    }

    @Override
    public PooledObject makeObject() throws Exception {
        Socket sock = new Socket();
        sock.connect(targetAddr, timeout);
        // 设置read超时时间
        sock.setSoTimeout(timeout);
        sock.setTcpNoDelay(true);
        sock.setKeepAlive(false);

        return new DefaultPooledObject<>(new SjsonbCodec(sock));
    }

    @Override
    public void destroyObject(PooledObject<SjsonbCodec> obj) throws Exception {
        SjsonbCodec codec = obj.getObject();
        codec.getBoundSocket().close();
    }

    @Override
    public boolean validateObject(PooledObject<SjsonbCodec> obj) {
        return obj.getObject().isClosed();
    }

    @Override
    public void activateObject(PooledObject<SjsonbCodec> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<SjsonbCodec> pooledObject) throws Exception {

    }
}
