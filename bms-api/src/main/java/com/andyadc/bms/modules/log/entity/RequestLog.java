package com.andyadc.bms.modules.log.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity(name = "RequestLog")
@Table(name = "request_log")
public class RequestLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String user;
	private String ip;
	private String method;
	private String url;
	private String page;

	@Column(name = "query_string")
	private String queryString;

	@Column(name = "referer_page")
	private String refererPage;

	@Column(name = "user_agent")
	private String userAgent;

	@Column(name = "logged_time")
	private LocalDateTime loggedTime;

	@Column(name = "unique_visit")
	private Boolean uniqueVisit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getRefererPage() {
		return refererPage;
	}

	public void setRefererPage(String refererPage) {
		this.refererPage = refererPage;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public LocalDateTime getLoggedTime() {
		return loggedTime;
	}

	public void setLoggedTime(LocalDateTime loggedTime) {
		this.loggedTime = loggedTime;
	}

	public Boolean getUniqueVisit() {
		return uniqueVisit;
	}

	public void setUniqueVisit(Boolean uniqueVisit) {
		this.uniqueVisit = uniqueVisit;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", RequestLog.class.getSimpleName() + "[", "]")
			.add("id=" + id)
			.add("user=" + user)
			.add("ip=" + ip)
			.add("method=" + method)
			.add("url=" + url)
			.add("page=" + page)
			.add("queryString=" + queryString)
			.add("refererPage=" + refererPage)
			.add("userAgent=" + userAgent)
			.add("loggedTime=" + loggedTime)
			.add("uniqueVisit=" + uniqueVisit)
			.toString();
	}
}
