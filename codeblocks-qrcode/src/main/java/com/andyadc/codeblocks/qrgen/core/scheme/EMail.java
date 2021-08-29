package com.andyadc.codeblocks.qrgen.core.scheme;

import java.util.Map;

import static com.andyadc.codeblocks.qrgen.core.scheme.util.SchemeUtil.getParameters;

/**
 * Encodes a e-mail address, format is: <code>mailto:mail@address.com</code>
 */
public class EMail extends Schema {

	private static final String MAILTO = "mailto";
	private String email;

	/**
	 * Default constructor to construct new e-mail object.
	 */
	public EMail() {
		super();
	}

	public EMail(String email) {
		super();
		this.email = email;
	}

	public static EMail parse(final String emailCode) {
		EMail mail = new EMail();
		mail.parseSchema(emailCode);
		return mail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Schema parseSchema(String code) {
		if (code == null || !code.toLowerCase().startsWith(MAILTO)) {
			throw new IllegalArgumentException("this is not a valid email code: " + code);
		}
		Map<String, String> parameters = getParameters(code.toLowerCase());
		if (parameters.containsKey(MAILTO)) {
			setEmail(parameters.get(MAILTO));
		}
		return this;
	}

	@Override
	public String generateString() {
		return MAILTO + ":" + email;
	}

	@Override
	public String toString() {
		return generateString();
	}
}
