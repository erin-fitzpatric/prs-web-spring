package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.Request;

public interface RequestRepository extends CrudRepository<Request, Integer> {

}
