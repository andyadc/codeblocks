package com.andyadc.bms.test.repository;

import com.andyadc.bms.modules.log.entity.RequestLog;
import com.andyadc.bms.modules.log.repository.RequestLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.time.LocalDateTime;

@SpringBootTest
public class RepositoryTests {

	@Inject
	private RequestLogRepository requestLogRepository;

	@Test
	public void testRequestLogRepository() {
		RequestLog log = new RequestLog();
		log.setMethod("POST");
		log.setUrl("/logout");
		log.setLoggedTime(LocalDateTime.now());
		requestLogRepository.save(log);
	}
}
