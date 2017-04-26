package com.jonathanzanella.githubapi.repo;

import com.jonathanzanella.githubapi.database.Fields;
import com.jonathanzanella.githubapi.database.Repository;
import com.jonathanzanella.githubapi.database.Where;

import java.util.List;

public class RepoRepository {
	private final Repository<Repo> repository;
	private final RepoTable table = new RepoTable();

	public RepoRepository(Repository<Repo> repository) {
		this.repository = repository;
	}

	public Repo findByName(String name) {
		List<Repo> repos = repository.query(table, new Where(Fields.NAME).eq(name));
		if(repos.size() > 0)
			return repos.get(0);
		return null;
	}

	public long countReposOfLanguage(Long languageId) {
		return repository.count(table, new Where(Fields.LANGUAGE_ID).eq(languageId));
	}

	public void save(Repo repo) {
		repository.save(table, repo);
	}
}