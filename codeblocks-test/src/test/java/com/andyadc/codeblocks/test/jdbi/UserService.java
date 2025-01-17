package com.andyadc.codeblocks.test.jdbi;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserJdbiDao dao;

	public UserService(Jdbi jdbi) {
		this.dao = jdbi.onDemand(UserJdbiDao.class);
	}

	public void createUserTable() {
		dao.createTable();
	}

}
