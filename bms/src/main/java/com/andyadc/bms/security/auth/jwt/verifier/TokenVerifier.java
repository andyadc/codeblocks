package com.andyadc.bms.security.auth.jwt.verifier;

public interface TokenVerifier {

	boolean verify(String jti);
}
