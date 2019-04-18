package org.e7.s2nd;

import org.e7.s2nd.codec.SjsonbCodec;
import org.apache.commons.pool2.PooledObject;

/**
 * @author e7
 */
public class XauthConnectionFactory extends AbstractSjsonbConnectionFactory {
    public XauthConnectionFactory(String host, int port) {
        this(host, port, 5);
    }

    public XauthConnectionFactory(String host, int port, int timeout) {
        super(host, port, timeout);
    }

    @Override
    public boolean validateObject(PooledObject<SjsonbCodec> obj) {
        SjsonbCodec codec = obj.getObject();

        try {
            codec.encodeToOutputStream("{\"api\":\"ping\"}".getBytes());
            if (codec.decodeFromInputStream().length > 0) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }
}
