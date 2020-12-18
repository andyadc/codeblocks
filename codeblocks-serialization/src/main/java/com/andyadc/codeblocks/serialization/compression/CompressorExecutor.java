package com.andyadc.codeblocks.serialization.compression;

import com.andyadc.codeblocks.serialization.compression.quicklz.QuickLz;
import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * @author andaicheng
 * @version 2017/2/15
 */
public class CompressorExecutor {

    public static byte[] compress(byte[] bytes) {
        return compress(bytes, CompressorFactory.getCompressorType());
    }

    public static byte[] decompress(byte[] bytes) {
        return decompress(bytes, CompressorFactory.getCompressorType());
    }

    public static byte[] compress(byte[] bytes, CompressorType type) {
        if (type == CompressorType.QUICK_LZ_COMPRESSOR) {
            return QuickLz.compress(bytes, 1);
        } else if (type == CompressorType.SNAPPY_COMPRESSOR) {
            try {
                return Snappy.compress(bytes);
            } catch (IOException e) {
                throw new CompressorException(e);
            }
        } else {
            throw new CompressorException("Invalid compressor type: " + type);
        }
    }

    public static byte[] decompress(byte[] bytes, CompressorType type) {
        if (type == CompressorType.QUICK_LZ_COMPRESSOR) {
            return QuickLz.decompress(bytes);
        } else if (type == CompressorType.SNAPPY_COMPRESSOR) {
            try {
                return Snappy.uncompress(bytes);
            } catch (IOException e) {
                throw new CompressorException(e);
            }
        } else {
			throw new CompressorException("Invalid compressor type: " + type);
        }
    }
}
