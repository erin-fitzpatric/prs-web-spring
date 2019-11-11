package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByUserNameAndPassword(String UserName, String Password);
}
