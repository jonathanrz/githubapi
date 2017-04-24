package com.jonathanzanella.githubapi.database;

import java.util.List;

public interface Repository<T> {
	List<T> query(Table<T> table, Where where);
	void save(Table<T> table, T data);
}
