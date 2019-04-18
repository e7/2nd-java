package com.rl160.s2nd.codec;

public class SjsonbConsts {
    public static final int MAGIC_LEN = 4;
    public static final int HEADER_VERSION_LEN = 4;
    public static final int HEADER_CONTTYPE_LEN = 2;
    public static final int HEADER_CONTOFST_LEN = 2;
    public static final int HEADER_CONTLEN_LEN = 4;
    public static final int HEADER_CHECKSUM_LEN = 4;
    public static final int HEADER_LEN = (
            HEADER_VERSION_LEN +
                    HEADER_CONTTYPE_LEN + HEADER_CONTOFST_LEN +
                    HEADER_CONTLEN_LEN + HEADER_CHECKSUM_LEN
    );

    public static final int PROTO_VERSION = 1000;
    public static final int MAX_PACKAGE_SIZE = 65535-HEADER_LEN;

    public static final String ETOO_LARGE_CONTENT = "too large content";
}
