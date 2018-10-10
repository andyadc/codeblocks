package com.andyadc.codeblocks.kit.idgen;

/**
 * @author andy.an
 * @since 2018/10/10
 */
public final class IDGen {

    public static String uuid() {
        return new com.eaio.uuid.UUID().toString();
    }
}
