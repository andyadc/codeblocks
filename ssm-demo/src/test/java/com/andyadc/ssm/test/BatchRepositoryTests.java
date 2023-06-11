package com.andyadc.ssm.test;

import com.andyadc.ssm.persistence.entity.SimpleTableRecord;
import com.andyadc.ssm.persistence.repository.BatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class BatchRepositoryTests {

	@Autowired
	private BatchRepository batchRepository;

	@Test
	public void testInsert() {
		List<SimpleTableRecord> recordList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			recordList.add(new SimpleTableRecord(UUID.randomUUID().toString()));
		}
		batchRepository.batchInsert(recordList);
	}
}
