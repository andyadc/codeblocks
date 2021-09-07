package com.andyadc.codeblocks.patchca.filter.predefined;

import com.andyadc.codeblocks.patchca.filter.library.MarbleImageOp;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

public class MarbleRippleFilterFactory extends RippleFilterFactory {

    protected MarbleImageOp marble = new MarbleImageOp();

    @Override
    protected List<BufferedImageOp> getPreRippleFilters() {
        List<BufferedImageOp> list = new ArrayList<>(4);
        list.add(marble);
        return list;
    }
}

