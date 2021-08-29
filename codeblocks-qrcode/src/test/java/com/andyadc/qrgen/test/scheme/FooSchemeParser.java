package com.andyadc.qrgen.test.scheme;

import com.andyadc.codeblocks.qrgen.core.scheme.Schema;
import com.andyadc.codeblocks.qrgen.core.scheme.parser.QRCodeSchemeParser;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Set;

public class FooSchemeParser implements QRCodeSchemeParser {

	@Override
	public Object parse(String qrCodeText) throws UnsupportedEncodingException {
		if (qrCodeText.startsWith("foo:")) {
			return new Foo();
		}
		throw new UnsupportedEncodingException("too bad");
	}

	@Override
	public Set<Class<? extends Schema>> getSupportedSchemes() {
		Set<Class<? extends Schema>> supportedSchemes = new LinkedHashSet<>();
		supportedSchemes.add(Foo.class);
		return supportedSchemes;
	}
}
