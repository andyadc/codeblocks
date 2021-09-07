package com.andyadc.codeblocks.patchca.service;

import com.andyadc.codeblocks.patchca.background.SingleColorBackgroundFactory;
import com.andyadc.codeblocks.patchca.color.SingleColorFactory;
import com.andyadc.codeblocks.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.andyadc.codeblocks.patchca.font.RandomFontFactory;
import com.andyadc.codeblocks.patchca.text.renderer.BestFitTextRenderer;
import com.andyadc.codeblocks.patchca.word.AdaptiveRandomWordFactory;

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
