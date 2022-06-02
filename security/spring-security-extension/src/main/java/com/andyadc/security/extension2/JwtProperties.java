package com.andyadc.security.extension2;

//import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private CertInfo certInfo;
	private Claims claims;

	public static class CertInfo {
		/**
		 * certificate alias,required
		 */
		private String alias;
		/**
		 * certificate password,required
		 */
		private String keyPassword;
		/**
		 * certificate path,required
		 */
		private String certLocation;

		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public String getKeyPassword() {
			return keyPassword;
		}

		public void setKeyPassword(String keyPassword) {
			this.keyPassword = keyPassword;
		}

		public String getCertLocation() {
			return certLocation;
		}

		public void setCertLocation(String certLocation) {
			this.certLocation = certLocation;
		}
	}

	public static class Claims {
		/**
		 * jwt issuer HTTPS
		 */
		private String issuer;
		/**
		 * jwt subject
		 */
		private String subject;
		/**
		 * jwt expired instant
		 */
		private Integer expiresAt;

		public String getIssuer() {
			return issuer;
		}

		public void setIssuer(String issuer) {
			this.issuer = issuer;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public Integer getExpiresAt() {
			return expiresAt;
		}

		public void setExpiresAt(Integer expiresAt) {
			this.expiresAt = expiresAt;
		}
	}
}
