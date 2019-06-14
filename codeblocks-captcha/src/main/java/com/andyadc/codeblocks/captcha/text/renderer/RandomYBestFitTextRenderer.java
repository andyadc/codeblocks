package com.andyadc.codeblocks.captcha.text.renderer;

import java.util.concurrent.ThreadLocalRandom;

public class RandomYBestFitTextRenderer extends AbstractTextRenderer {

    @Override
    protected void arrangeCharacters(int width, int height, TextString ts) {
        double widthRemaining = (width - ts.getWidth() - leftMargin - rightMargin) / ts.getCharacters().size();
        double vmiddle = height / 2D;
        double x = leftMargin + widthRemaining / 2;
        ThreadLocalRandom r = ThreadLocalRandom.current();
        height -= topMargin + bottomMargin;
        for (TextCharacter tc : ts.getCharacters()) {
            double heightRemaining = height - tc.getHeight();
            double y = vmiddle + 0.35 * tc.getAscent() + (1 - 2 * r.nextDouble()) * heightRemaining;
            tc.setX(x);
            tc.setY(y);
            x += tc.getWidth() + widthRemaining;
        }
    }

}
