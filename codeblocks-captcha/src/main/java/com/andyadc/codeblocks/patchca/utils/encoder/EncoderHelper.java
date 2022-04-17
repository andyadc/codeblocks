package com.andyadc.codeblocks.patchca.utils.encoder;

import com.andyadc.codeblocks.patchca.service.Captcha;
import com.andyadc.codeblocks.patchca.service.CaptchaService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncoderHelper {

	public static String getChallangeAndWriteImage(CaptchaService service, String format, OutputStream os) throws IOException {
		Captcha captcha = service.getCaptcha();
		ImageIO.write(captcha.getImage(), format, os);
		return captcha.getChallenge();
	}

	public static String genCaptchaImage(Captcha captcha) {
		BufferedImage buf = captcha.getImage();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		String captchaPngImage = null;
		try {
			ImageIO.write(buf, "png", bao);
			bao.flush();
			byte[] imageBytes = bao.toByteArray();
			bao.close();
			captchaPngImage = new String(Base64.getEncoder().encode(imageBytes), StandardCharsets.UTF_8);
		} catch (Exception e) {
			// do nothing
		}
		return captchaPngImage;
	}
}

