package com.jonathanzanella.githubapi.projects;

import com.jonathanzanella.githubapi.database.Fields;
import com.jonathanzanella.githubapi.database.Repository;
import com.jonathanzanella.githubapi.database.Where;

import java.util.List;

public class ProjectRepository {
	private final Repository<Project> repository;
	private final ProjectTable table = new ProjectTable();

	public ProjectRepository(Repository<Project> repository) {
		this.repository = repository;
	}

	public Project findByName(String name) {
		List<Project> projects = repository.query(table, new Where(Fields.NAME).eq(name));
		if(projects.size() > 0)
			return projects.get(0);
		return null;
	}

	public long countProjectsOfLanguage(Long languageId) {
		return repository.count(table, new Where(Fields.LANGUAGE_ID).eq(languageId));
	}

	public void save(Project project) {
		repository.save(table, project);
	}
}