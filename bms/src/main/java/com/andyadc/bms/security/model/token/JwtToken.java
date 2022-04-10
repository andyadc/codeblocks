package com.andyadc.bms.security.model.token;

import java.io.Serializable;

public interface JwtToken extends Serializable {

	String getToken();
}
