package com.andyadc.codeblocks.serialization.compression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class CompressorFactory {

	private static final Logger logger = LoggerFactory.getLogger(CompressorFactory.class);

	private static CompressorType compressorType = CompressorType.QUICK_LZ_COMPRESSOR;
	private static boolean compress;

	public static void initialize(Properties properties) {
		String compressor = properties.getProperty("compressor");
		try {
			compressorType = CompressorType.fromString(compressor);
		} catch (Exception e) {
			logger.warn("Invalid compressor={}, use default={}", compressor, compressorType, e);
		}
		compress = Boolean.parseBoolean(properties.getProperty("compress"));
		logger.info("CompressorType is {}, Compress is {}", compressorType, compress);
	}

	public static CompressorType getCompressorType() {
		return compressorType;
	}

	public static void setCompressorType(CompressorType compressorType) {
		CompressorFactory.compressorType = compressorType;
	}

	public static boolean isCompress() {
		return compress;
	}

	public static void setCompress(boolean compress) {
		CompressorFactory.compress = compress;
	}
}
