package com.andyadc.codeblocks.test.sensitive;

public enum SensitiveStrategy {

	/**
	 * Username sensitive strategy.
	 */
	USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),
	/**
	 * Id card sensitive type.
	 */
//	ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2")),
	ID_CARD(s -> s.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*")),
	/**
	 * Phone sensitive type.
	 */
	PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),
	/**
	 * Address sensitive type.
	 */
	ADDRESS(s -> s.replaceAll("(\\S{8})\\S{4}(\\S*)\\S{4}", "$1****$2****"));

	private final Desensitizer desensitizer;

	SensitiveStrategy(Desensitizer desensitizer) {
		this.desensitizer = desensitizer;
	}

	public Desensitizer getDesensitizer() {
		return desensitizer;
	}
}
