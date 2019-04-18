package org.e7.s2nd.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author e7
 */
public class SjsonbCodec {
    private Socket skt;

    /**
     * 解码状态机枚举
     */
    enum Status {
        EXP_MAGIC,
        EXP_VERSION, EXP_CONTTYPE, EXP_CONTOFST, EXP_CONTLEN, EXP_CHECKSUM,
        EXP_BODY
    }
    private Status currentStatus = Status.EXP_MAGIC;

    private void readful(InputStream is, byte[] buf) throws IOException {
        int nBeenRead = 0;

        while (nBeenRead < buf.length) {
            int n = is.read(buf, nBeenRead, buf.length - nBeenRead);
            if (-1 == n) {
                is.close();
                throw new IOException("connection closed");
            }
            nBeenRead += n;
        }
    }

    public SjsonbCodec(Socket skt) {
        this.skt = skt;
    }

    public Socket getBoundSocket() {
        return this.skt;
    }

    public void close() {
        try { this.skt.close(); } catch (IOException e) {}
    }
    public boolean isClosed() {
        return this.skt.isClosed();
    }

    public byte[] decodeFromInputStream() throws SjsonbProtocolException, IOException {
        boolean done = false;
        byte[] buf2 = new byte[2];
        byte[] buf4= new byte[4];
        int contLen = 0;
        byte[] bufBody = null;
        InputStream is = skt.getInputStream();

        while (! done) {
            switch (currentStatus) {
                case EXP_MAGIC:
                    readful(is, buf4);
                    long magic = BaseSerializer.byteArrayToInt(buf4) & 0x00000000ffffffffL;
                    if (magic == 0x00000000e78f8a9dL) {
                        currentStatus = Status.EXP_VERSION;
                    }
                    break;

                case EXP_VERSION:
                    readful(is, buf4);
                    currentStatus = Status.EXP_CONTTYPE;
                    break;

                case EXP_CONTTYPE:
                    readful(is, buf2);
                    currentStatus = Status.EXP_CONTOFST;
                    break;

                case EXP_CONTOFST:
                    readful(is, buf2);
                    currentStatus = Status.EXP_CONTLEN;
                    break;

                case EXP_CONTLEN:
                    readful(is, buf4);
                    contLen = (int)(BaseSerializer.byteArrayToInt(buf4) & 0x00000000ffffffffL);
                    if (contLen > SjsonbConsts.MAX_PACKAGE_SIZE) {
                        is.close();
                        throw new SjsonbProtocolException(SjsonbConsts.ETOO_LARGE_CONTENT);
                    }
                    currentStatus = Status.EXP_CHECKSUM;
                    break;

                case EXP_CHECKSUM:
                    readful(is, buf4);
                    currentStatus = Status.EXP_BODY;
                    break;

                // read body
                case EXP_BODY:
                default:
                    bufBody = new byte[contLen];
                    readful(is, bufBody);
                    done = true;
                    currentStatus = Status.EXP_MAGIC;
                    break;
            }
        }

        return bufBody;
    }

    public void encodeToOutputStream(byte[] cargo) throws IOException {
        OutputStream os = skt.getOutputStream();
        byte[] sendBuf = new byte[SjsonbConsts.MAGIC_LEN + SjsonbConsts.HEADER_LEN + cargo.length];

        System.arraycopy(
                BaseSerializer.intToByteArray(0xE78F8A9D), 0, sendBuf, 0, 4
        );
        System.arraycopy(
                BaseSerializer.intToByteArray(SjsonbConsts.PROTO_VERSION), 0, sendBuf, 4, 4
        );
        System.arraycopy(
                BaseSerializer.shortToByteArray(3), 0, sendBuf, 8, 2
        );
        System.arraycopy(
                BaseSerializer.shortToByteArray(20), 0, sendBuf, 10, 2
        );
        System.arraycopy(
                BaseSerializer.intToByteArray(cargo.length), 0, sendBuf, 12, 4
        );
        System.arraycopy(BaseSerializer.intToByteArray(0), 0, sendBuf, 16, 4
        );
        System.arraycopy(cargo, 0, sendBuf, 20, cargo.length);

        os.write(sendBuf);
    }
}
