package com.andyadc.security.test;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

//TODO
@Profile("test")
@Configuration(proxyBeanMethods = false)
public class JwtConfiguration {

	private static final KeyStore JKS_STORE;

	static {
		try {
			JKS_STORE = KeyStore.getInstance("jks");
		} catch (KeyStoreException e) {
			throw new RuntimeException("Can not obtain jks keystore instance!");
		}
	}

	@Bean
	@ConditionalOnMissingBean
	public JWKSource<SecurityContext> jwkSource() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, JOSEException {
		String path = "jwt.jks";
		String alias = "jwt";
		String password = "123465";

		ClassPathResource resource = new ClassPathResource(path);
		char[] pwd = password.toCharArray();
		JKS_STORE.load(resource.getInputStream(), pwd);

		RSAKey rsaKey = RSAKey.load(JKS_STORE, alias, pwd);

		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	@Bean
	@ConditionalOnMissingBean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}
}
