package com.andyadc.bms.modules.log.service;

import com.andyadc.bms.modules.log.entity.RequestLog;
import com.andyadc.bms.modules.log.repository.RequestLogRepository;
import com.andyadc.codeblocks.kit.net.IPUtil;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

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

			String url = Objects.toString(request.getRequestURL());
			String page = request.getServletPath();

			String referer = request.getHeader("Referer");
			referer = (referer != null) ? referer : request.getHeader("referer");

			String userAgent = request.getHeader("User-Agent");
			userAgent = (userAgent != null) ? userAgent : request.getHeader("user-agent");

			String ip = IPUtil.getClientIpAddress(request);

			RequestLog log = new RequestLog();
			log.setUser("");
			log.setIp(ip);
			log.setMethod(request.getMethod());
			log.setUrl(url);
			log.setPage(page);
			log.setQueryString(request.getQueryString());
			log.setRefererPage(referer);
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
