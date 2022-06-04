package com.andyadc.bms.modules.log.repository;

import com.andyadc.bms.modules.log.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
}
