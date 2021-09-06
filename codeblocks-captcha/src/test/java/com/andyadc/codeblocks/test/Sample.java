package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.captcha.color.SingleColorFactory;
import com.andyadc.codeblocks.captcha.filter.predefined.CurvesRippleFilterFactory;
import com.andyadc.codeblocks.captcha.service.ConfigurableCaptchaService;
import com.andyadc.codeblocks.captcha.utils.encoder.EncoderHelper;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * sample code
 * Created by shijinkui on 15/3/15.
 */
public class Sample {

	public static void main(String[] args) throws IOException {
		ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
		cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
		cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));

		FileOutputStream fos = new FileOutputStream("patcha_demo.png");
		EncoderHelper.getChallangeAndWriteImage(cs, "png", fos);
		fos.close();
	}
}
