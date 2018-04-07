package com.andyadc.codeblocks.captcha.utils.encoder;

import com.andyadc.codeblocks.captcha.service.Captcha;
import com.andyadc.codeblocks.captcha.service.CaptchaService;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;

public class EncoderHelper {

    public static String getChallangeAndWriteImage(CaptchaService service, String format, OutputStream os) throws IOException {
        Captcha captcha = service.getCaptcha();
        ImageIO.write(captcha.getImage(), format, os);
        return captcha.getChallenge();
    }

}

