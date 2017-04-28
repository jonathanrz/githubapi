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

	public List<Project> all() {
		return repository.query(table, new Where(null));
	}

	public Project findByName(String name) {
		List<Project> projects = repository.query(table, new Where(Fields.NAME).eq(name));
		if(projects.size() > 0)
			return projects.get(0);
		return null;
	}

	public List<Project> projectsOfLanguage(Long languageId) {
		return repository.query(table, new Where(Fields.LANGUAGE_ID).eq(languageId).orderByDesc(Fields.OPEN_ISSUES));
	}

	public long countProjectsOfLanguage(Long languageId) {
		return repository.count(table, new Where(Fields.LANGUAGE_ID).eq(languageId));
	}

	public void deleteInvalid() {
		repository.delete(table, new Where(Fields.VALID).eq(false));
	}

	public void save(Project project) {
		repository.save(table, project);
	}
}