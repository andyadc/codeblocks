package com.andyadc.codeblocks.serialization.compression;

/**
 * @author andaicheng
 * @version 2017/1/9
 */
public enum CompressorType {

    QUICK_LZ_COMPRESSOR("quickLzCompressor"),
    SNAPPY_COMPRESSOR("snappyCompressor");

    private String value;

    CompressorType(String value) {
        this.value = value;
    }

    public static CompressorType fromString(String value) {
        for (CompressorType type : CompressorType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
