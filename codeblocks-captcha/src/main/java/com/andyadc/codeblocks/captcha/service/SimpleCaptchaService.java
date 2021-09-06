package com.andyadc.codeblocks.captcha.service;

import com.andyadc.codeblocks.captcha.background.SingleColorBackgroundFactory;
import com.andyadc.codeblocks.captcha.color.SingleColorFactory;
import com.andyadc.codeblocks.captcha.filter.FilterFactory;
import com.andyadc.codeblocks.captcha.font.RandomFontFactory;
import com.andyadc.codeblocks.captcha.text.renderer.BestFitTextRenderer;
import com.andyadc.codeblocks.captcha.word.AdaptiveRandomWordFactory;

import java.awt.*;

public class SimpleCaptchaService extends AbstractCaptchaService {

    public SimpleCaptchaService(int width, int height, Color textColor, Color backgroundColor, int fontSize, FilterFactory ff) {
        backgroundFactory = new SingleColorBackgroundFactory(backgroundColor);
        wordFactory = new AdaptiveRandomWordFactory();
        fontFactory = new RandomFontFactory();
        textRenderer = new BestFitTextRenderer();
        colorFactory = new SingleColorFactory(textColor);
        filterFactory = ff;
        this.width = width;
        this.height = height;
    }

    public SimpleCaptchaService(int width, int height, Color textColor, Color backgroundColor, int fontSize, String[] fontNames, FilterFactory ff) {
        backgroundFactory = new SingleColorBackgroundFactory(backgroundColor);
        wordFactory = new AdaptiveRandomWordFactory();
        fontFactory = new RandomFontFactory(fontNames);
        textRenderer = new BestFitTextRenderer();
        colorFactory = new SingleColorFactory(textColor);
        filterFactory = ff;
        this.width = width;
        this.height = height;
    }

    @Override
    public Captcha getCaptcha() {
        return null;
    }
}
