package com.andyadc.codeblocks.framework.idgen;

/**
 * andy.an
 */
public class ZeroIDGen implements IDGen {

    @Override
    public Result gen(String key) {
        return new Result("0", Status.SUCCESS);
    }

    @Override
    public boolean init() {
        return true;
    }
}
