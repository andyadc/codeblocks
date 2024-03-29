package com.andyadc.codeblocks.patchca.text.renderer;

import com.andyadc.codeblocks.patchca.color.ColorFactory;
import com.andyadc.codeblocks.patchca.font.FontFactory;

import java.awt.image.BufferedImage;

public interface TextRenderer {

    void setLeftMargin(int leftMargin);

    void setRightMargin(int rightMargin);

    void setTopMargin(int topMargin);

    void setBottomMargin(int bottomMargin);

    void draw(String text, BufferedImage canvas, FontFactory fontFactory, ColorFactory colorFactory);
}
