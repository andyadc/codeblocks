package com.andyadc.ssm.service;

import com.andyadc.ssm.persistence.entity.Demo;
import com.andyadc.ssm.persistence.mapper.DemoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andy.an
 * @since 2018/9/29
 */
@Service
public class DemoService {

	private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

	@Autowired
	private DemoMapper demoMapper;

	@Transactional(rollbackFor = Exception.class)
	public void add(Demo demo) {
		demoMapper.insertSelective(demo);
		logger.info("demo id: {}", demo.getId());
		int i = 1 / 0;
	}

}
