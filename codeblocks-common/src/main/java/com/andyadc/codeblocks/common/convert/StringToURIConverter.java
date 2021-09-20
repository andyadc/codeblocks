package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;

import java.net.URI;

/**
 * The class to convert {@link String} to {@link URI}
 */
public class StringToURIConverter implements StringConverter<URI> {

	@Override
	public URI convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		return URI.create(source);
	}
}
