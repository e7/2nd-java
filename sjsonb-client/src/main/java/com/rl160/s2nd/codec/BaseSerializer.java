package com.rl160.s2nd.codec;

/**
 * @author e7
 */
public class BaseSerializer {
    public static byte[] shortToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static short byteArrayToShort(byte[] b) {
        return  (short)((b[1] & 0xFF) | (b[0] & 0xFF) << 8);
    }

    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static int byteArrayToInt(byte[] b) {
        return  (b[3] & 0xFF) |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] sliceByteArray(byte[] b, int from, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(b, from, bs, 0, count);
        return bs;
    }
}
