package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.mask.MaskType;
import org.junit.Test;

/**
 * @author andaicheng
 * @since 2018/1/23
 */
public class MaskTest {

    @Test
    public void testMask() {
        System.out.println(MaskType.MOBILE.mask("13812345678"));
        System.out.println(MaskType.BANK_CARD.mask("6214830216049238"));
        System.out.println(MaskType.ID_CARD.mask("331012198001013030"));
        System.out.println(MaskType.NAME.mask("张三丰"));
        System.out.println(MaskType.CVV.mask("123"));
        System.out.println(MaskType.CREDIT_EXP.mask("12/18"));
        System.out.println(MaskType.PASSWORD.mask("12345678"));
        System.out.println(MaskType.CAPTCHA.mask("1234"));
        System.out.println(MaskType.ADDRESS.mask("上海市浦东新区浦建路100弄2号0501室"));
        System.out.println(MaskType.EMAIL.mask("826665031@qq.com"));
        System.out.println(MaskType.IP_ADDRESS.mask("192.168.0.1"));
    }
}
