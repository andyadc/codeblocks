package com.andyadc.codeblocks.captcha.filter.predefined;

import com.andyadc.codeblocks.captcha.filter.library.WobbleImageOp;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

public class WobbleRippleFilterFactory extends RippleFilterFactory {

    protected WobbleImageOp wobble;

    public WobbleRippleFilterFactory() {
        wobble = new WobbleImageOp();
    }

    @Override
    protected List<BufferedImageOp> getPreRippleFilters() {
        List<BufferedImageOp> list = new ArrayList<>(4);
        list.add(wobble);
        return list;
    }

}
