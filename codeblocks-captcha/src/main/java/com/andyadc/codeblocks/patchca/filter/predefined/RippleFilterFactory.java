package com.andyadc.codeblocks.patchca.filter.predefined;

import com.andyadc.codeblocks.patchca.filter.AbstractFilterFactory;
import com.andyadc.codeblocks.patchca.filter.library.RippleImageOp;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

public class RippleFilterFactory extends AbstractFilterFactory {

    protected List<BufferedImageOp> filters;
    protected RippleImageOp ripple;

    public RippleFilterFactory() {
        ripple = new RippleImageOp();
    }

    protected List<BufferedImageOp> getPreRippleFilters() {
        return new ArrayList<>();
    }

    protected List<BufferedImageOp> getPostRippleFilters() {
        return new ArrayList<>();
    }

    @Override
    public List<BufferedImageOp> getFilters() {
        if (filters == null) {
            filters = new ArrayList<>();
            filters.addAll(getPreRippleFilters());
            filters.add(ripple);
            filters.addAll(getPostRippleFilters());
        }
        return filters;
    }
}
