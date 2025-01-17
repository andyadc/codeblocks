package com.andyadc.codeblocks.test.jdbi;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface UserJdbiDao {

	@SqlUpdate("CREATE TABLE IF NOT EXISTS jdbi_users (id INT PRIMARY KEY, name VARCHAR(50))")
	void createTable();

	@SqlUpdate("INSERT INTO jdbi_users (id, name) VALUES (:id, :name)")
	void insertPositional(int id, String name);

	@SqlUpdate("INSERT INTO jdbi_users (id, name) VALUES (:id, :name)")
	void insertNamed(@Bind("id") int id, @Bind("name") String name);

	@SqlUpdate("INSERT INTO jdbi_users (id, name) VALUES (:id, :name)")
	void insertBean(@BindBean User user);

	// Automatically retrieves the generated primary key
	@GetGeneratedKeys
	@SqlUpdate("INSERT INTO users (name) VALUES (:name)")
	long insertReturnKey(@Bind("name") String name);

	@RegisterBeanMapper(User.class)
	@SqlQuery("SELECT * FROM jdbi_users WHERE id = :id")
	User getUser(@Bind("id") int id);

	@RegisterBeanMapper(User.class)
	@SqlQuery("SELECT name FROM jdbi_users WHERE name like '%' || :name || '%'")
	List<User> getLike(@Bind("name") String name);

}
