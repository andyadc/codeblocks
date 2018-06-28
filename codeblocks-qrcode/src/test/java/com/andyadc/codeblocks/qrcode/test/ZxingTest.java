package com.andyadc.codeblocks.qrcode.test;

import com.andyadc.codeblocks.qrcode.ZxingDecoder;
import com.andyadc.codeblocks.qrcode.ZxingEncoder;
import com.andyadc.codeblocks.qrcode.ZxingEntity;
import com.andyadc.codeblocks.qrcode.ZxingUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import org.junit.Test;

import java.io.File;

/**
 * @author andy.an
 * @since 2018/6/28
 */
public class ZxingTest {

    @Test
    public void testEncode() throws Exception {
        // 条形码内容
        String text = "https://mvnrepository.com/artifact/commons-io/commons-io/2.5";
        // 条形码图片导出路径
        File file = new File("E:/条形码.jpg");

        ZxingEntity entity = new ZxingEntity();
        entity.withDefaultLogo();
        entity.setBarcodeFormat(BarcodeFormat.QR_CODE);
        entity.setText(text);
        entity.setOutputFile(file);
        entity.setWidth(200);
        entity.setHeight(200);

        ZxingEncoder encoder = new ZxingEncoder();
        byte[] bytes = encoder.encode2Bytes(entity);

        ZxingUtil.createFile(bytes, file);

        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByBytes(bytes, entity.getEncoding());
        System.out.println(result.getText());
    }
}
