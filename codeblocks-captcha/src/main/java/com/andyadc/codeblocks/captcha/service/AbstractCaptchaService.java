package com.andyadc.codeblocks.captcha.service;

import com.andyadc.codeblocks.captcha.background.BackgroundFactory;
import com.andyadc.codeblocks.captcha.color.ColorFactory;
import com.andyadc.codeblocks.captcha.filter.FilterFactory;
import com.andyadc.codeblocks.captcha.font.FontFactory;
import com.andyadc.codeblocks.captcha.text.renderer.TextRenderer;
import com.andyadc.codeblocks.captcha.word.WordFactory;

import java.awt.image.BufferedImage;

public abstract class AbstractCaptchaService implements CaptchaService {

    protected FontFactory fontFactory;
    protected WordFactory wordFactory;
    protected ColorFactory colorFactory;
    protected BackgroundFactory backgroundFactory;
    protected TextRenderer textRenderer;
    protected FilterFactory filterFactory;
    protected int width;
    protected int height;

    public FontFactory getFontFactory() {
        return fontFactory;
    }

    public void setFontFactory(FontFactory fontFactory) {
        this.fontFactory = fontFactory;
    }

    public WordFactory getWordFactory() {
        return wordFactory;
    }

    public void setWordFactory(WordFactory wordFactory) {
        this.wordFactory = wordFactory;
    }

    public ColorFactory getColorFactory() {
        return colorFactory;
    }

    public void setColorFactory(ColorFactory colorFactory) {
        this.colorFactory = colorFactory;
    }

    public BackgroundFactory getBackgroundFactory() {
        return backgroundFactory;
    }

    public void setBackgroundFactory(BackgroundFactory backgroundFactory) {
        this.backgroundFactory = backgroundFactory;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public void setTextRenderer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    public FilterFactory getFilterFactory() {
        return filterFactory;
    }

    public void setFilterFactory(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public Captcha getCaptcha() {
        BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        backgroundFactory.fillBackground(bufImage);
        String word = wordFactory.getNextWord();
        textRenderer.draw(word, bufImage, fontFactory, colorFactory);
        bufImage = filterFactory.applyFilters(bufImage);
        return new Captcha(word, bufImage);
    }
}
