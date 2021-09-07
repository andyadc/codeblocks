package com.andyadc.codeblocks.patchca.filter.predefined;

import com.andyadc.codeblocks.patchca.filter.library.DiffuseImageOp;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

public class DiffuseRippleFilterFactory extends RippleFilterFactory {

    protected DiffuseImageOp diffuse = new DiffuseImageOp();

    @Override
    protected List<BufferedImageOp> getPreRippleFilters() {
        List<BufferedImageOp> list = new ArrayList<>(4);
        list.add(diffuse);
        return list;
    }
}
