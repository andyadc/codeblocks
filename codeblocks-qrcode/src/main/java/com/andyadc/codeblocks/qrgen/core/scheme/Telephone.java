package com.andyadc.codeblocks.qrgen.core.scheme;

import java.util.Map;

import static com.andyadc.codeblocks.qrgen.core.scheme.util.SchemeUtil.getParameters;

/**
 * Encodes a telephone number, format is: <code>tel:+1-212-555-1212</code>
 */
public class Telephone extends Schema {

	private static final String TEL = "tel";
	private String telephone;

	/**
	 * Default constructor to construct new telephone object.
	 */
	public Telephone() {
		super();
	}

	public static Telephone parse(final String telephone) {
		Telephone tel = new Telephone();
		tel.parseSchema(telephone);
		return tel;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public Schema parseSchema(String code) {
		if (code == null || !code.trim().toLowerCase().startsWith(TEL)) {
			throw new IllegalArgumentException("this is not a valid telephone code: " + code);
		}
		Map<String, String> parameters = getParameters(code.trim().toLowerCase());
		if (parameters.containsKey(TEL)) {
			setTelephone(parameters.get(TEL));
		}
		return this;
	}

	@Override
	public String generateString() {
		return TEL + ":" + telephone;
	}

	@Override
	public String toString() {
		return generateString();
	}
}
