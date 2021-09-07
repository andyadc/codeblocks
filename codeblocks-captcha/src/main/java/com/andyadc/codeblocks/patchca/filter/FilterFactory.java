package com.andyadc.codeblocks.patchca.filter;

import java.awt.image.BufferedImage;

public interface FilterFactory {

    BufferedImage applyFilters(BufferedImage source);
}
