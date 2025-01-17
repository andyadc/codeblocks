package com.andyadc.codeblocks.test.jdbi;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserJdbiDao {

	@SqlUpdate("CREATE TABLE IF NOT EXISTS jdbi_users (id INT PRIMARY KEY, name VARCHAR(50))")
	void createTable();

	@SqlUpdate("INSERT INTO jdbi_users (id, name) VALUES (:id, :name)")
	void insertUser(@Bind("id") int id, @Bind("name") String name);

	@RegisterBeanMapper(User.class)
	@SqlQuery("SELECT * FROM jdbi_users WHERE id = :id")
	User getUser(@Bind("id") int id);

}
