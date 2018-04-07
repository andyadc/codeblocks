package com.andyadc.codeblocks.captcha.filter.predefined;

import com.andyadc.codeblocks.captcha.filter.library.MarbleImageOp;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

public class MarbleRippleFilterFactory extends RippleFilterFactory {

    protected MarbleImageOp marble = new MarbleImageOp();

    @Override
    protected List<BufferedImageOp> getPreRippleFilters() {
        List<BufferedImageOp> list = new ArrayList<BufferedImageOp>();
        list.add(marble);
        return list;
    }

}

