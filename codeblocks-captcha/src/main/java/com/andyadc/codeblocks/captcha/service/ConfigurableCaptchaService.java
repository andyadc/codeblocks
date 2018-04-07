package com.andyadc.codeblocks.captcha.service;

import com.andyadc.codeblocks.captcha.background.SingleColorBackgroundFactory;
import com.andyadc.codeblocks.captcha.color.SingleColorFactory;
import com.andyadc.codeblocks.captcha.filter.predefined.CurvesRippleFilterFactory;
import com.andyadc.codeblocks.captcha.font.RandomFontFactory;
import com.andyadc.codeblocks.captcha.text.renderer.BestFitTextRenderer;
import com.andyadc.codeblocks.captcha.word.AdaptiveRandomWordFactory;

public class ConfigurableCaptchaService extends AbstractCaptchaService {

    public ConfigurableCaptchaService() {
        backgroundFactory = new SingleColorBackgroundFactory();
        wordFactory = new AdaptiveRandomWordFactory();
        fontFactory = new RandomFontFactory();
        textRenderer = new BestFitTextRenderer();
        colorFactory = new SingleColorFactory();
        filterFactory = new CurvesRippleFilterFactory(colorFactory);
        textRenderer.setLeftMargin(10);
        textRenderer.setRightMargin(10);
        width = 160;
        height = 70;
    }

}
