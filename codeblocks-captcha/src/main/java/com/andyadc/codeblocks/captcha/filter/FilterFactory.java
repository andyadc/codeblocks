package com.andyadc.codeblocks.captcha.filter;

import java.awt.image.BufferedImage;

public interface FilterFactory {

    BufferedImage applyFilters(BufferedImage source);
}
