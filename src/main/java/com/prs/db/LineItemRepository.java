package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.LineItem;

public interface LineItemRepository extends CrudRepository<LineItem, Integer> {

}
