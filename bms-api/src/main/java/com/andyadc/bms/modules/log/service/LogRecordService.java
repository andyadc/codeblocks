package com.andyadc.bms.modules.log.service;

import com.andyadc.bms.modules.log.entity.RequestLog;
import com.andyadc.bms.modules.log.repository.RequestLogRepository;
import com.andyadc.bms.utils.HttpRequestResponseUtils;
import com.andyadc.codeblocks.kit.net.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class LogRecordService {

	private static final Logger logger = LoggerFactory.getLogger(LogRecordService.class);

	private RequestLogRepository requestLogRepository;
	private ThreadPoolTaskExecutor executor;

	@Inject
	public void setRequestLogRepository(RequestLogRepository requestLogRepository) {
		this.requestLogRepository = requestLogRepository;
	}

	@Inject
	public void setExecutor(ThreadPoolTaskExecutor executor) {
		this.executor = executor;
	}

	public void saveReqeustLog(HttpServletRequest request) {
		executor.execute(() -> {
			final String ip = IPUtil.getClientIpAddress(request);
			final String url = request.getRequestURL().toString();
			final String page = request.getRequestURI();
			String referer = request.getHeader("Referer");
			String refererPage = referer != null ? referer : request.getHeader("referer");
			final String queryString = request.getQueryString();
			String userAgent = request.getHeader("User-Agent");
			userAgent = userAgent != null ? userAgent : request.getHeader("user-agent");
			final String requestMethod = request.getMethod();

			RequestLog log = new RequestLog();
			log.setUser(HttpRequestResponseUtils.getLoggedInUser());
			log.setIp(ip);
			log.setMethod(requestMethod);
			log.setUrl(url);
			log.setPage(page);
			log.setQueryString(queryString);
			log.setRefererPage(refererPage);
			log.setUserAgent(userAgent);
			log.setLoggedTime(LocalDateTime.now());
			log.setUniqueVisit(true);

			if (logger.isDebugEnabled()) {
				logger.debug("{}", log);
			}
			requestLogRepository.save(log);
		});
	}
}
