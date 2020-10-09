package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.crypto.DSASignature;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @author andy.an
 * @since 2018/5/22
 */
public class DSASignatureTest {

    @Test
    public void testSign() throws Exception {
        String data = "Hello";
        String privateKeyStr = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUPKsU1ajHqczCFLT0eIcoqLgSwYU=";

		byte[] sign = DSASignature.sign(StandardCharsets.UTF_8.encode(data).array(), Base64.getDecoder().decode(privateKeyStr));
        System.out.println("sign:\n\t" + Base64.getEncoder().encodeToString(sign));
    }

    @Test
    public void testVerify() throws Exception {
        String data = "Hello";
        String publicKeyStr = "MIIBuDCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYUAAoGBALUOlzCt3oM/AL1zF5ZnT9KhYnh4MVjMXycUn427blMj5I0hUsA8PMAk/IgE9rcNxiVbDV0vWV0t+d7wETgcPZkzgTHTnrUt0crjGauow4H4iBpSFDXVArnQfh0/wsBIqjG/pEMUhaZ1eNl6wM18B1MJkKSoTU5xQJ2jt3svrz9c";

		byte[] sign = Base64.getDecoder().decode("MCwCFCgZw4KAki24MxSdUzodtQ0TRNkKAhRwSDPtNdG8/GO5fptlTi4RrXchQg==");
		boolean flag = DSASignature.verify(StandardCharsets.UTF_8.encode(data).array(), Base64.getDecoder().decode(publicKeyStr), sign);
        System.out.println("verify:\n\t" + flag);
    }

    @Test
    public void createKeys() throws Exception {
        Map<String, Object> keyMap = DSASignature.initKey();

        byte[] publicKey = DSASignature.getPublicKey(keyMap);
        byte[] privateKey = DSASignature.getPrivateKey(keyMap);

        System.out.println("publicKey:\n\t" + Base64.getEncoder().encodeToString(publicKey));
        System.out.println("privateKey:\n\t" + Base64.getEncoder().encodeToString(privateKey));
    }
}
