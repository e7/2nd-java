package com.tls.sigcheck;

import org.junit.Test;

public class SigCheckTest {
    @Test
    public void test() {
        tls_sigcheck sigcheck = new tls_sigcheck();

        sigcheck.tls_gen_signature_ex2(
                "1400127022", "xxxxx",
                "-----BEGIN PRIVATE KEY-----\n" +
                        "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgnJ+NkinOqIL2R4vs\n"+
                        "UeSFQ3YbbVspVDGQTG5rfTCEx3ahRANCAAQgsVQgrS/wEz0PynuK6E+IJMZG1ac1\n"+
                        "EdPRgQNoiuO8hs/N1B75RwFaOId108fSMd44B/CWjOMH4Q67adiuSBkq\n"+
                        "-----END PRIVATE KEY-----"
        );
        System.out.println(sigcheck.getSig());
    }
}
