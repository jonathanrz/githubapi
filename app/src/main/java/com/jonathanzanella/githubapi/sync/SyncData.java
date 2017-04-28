package com.jonathanzanella.githubapi.sync;

import com.jonathanzanella.githubapi.github.GitHubRepository;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageRepository;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import java.util.List;

class SyncData {
	private LanguageRepository languageRepository;
	private ProjectRepository projectRepository;

	SyncData(LanguageRepository languageRepository, ProjectRepository projectRepository) {
		this.languageRepository = languageRepository;
		this.projectRepository = projectRepository;
	}

	void syncData(List<GitHubRepository> repositories) {
		invalidateLanguages();
		invalidateProjects();

		for (final GitHubRepository gitHubRepository : repositories) {
			Language language = languageRepository.findByName(gitHubRepository.getLanguage());
			if(language == null) {
				language = new Language();
				language.setName(gitHubRepository.getLanguage());
			}
			language.setValid(true);
			languageRepository.save(language);

			Project project = projectRepository.findByName(gitHubRepository.getName());
			if(project == null) {
				project = new Project();
				project.setName(gitHubRepository.getName());
			}

			project.setCreatedAt(gitHubRepository.getCreatedAt());
			project.setUpdatedAt(gitHubRepository.getUpdatedAt());
			project.setOpenIssues(gitHubRepository.getOpenIssues());
			project.setLanguageId(language.getId());
			project.setValid(true);
			projectRepository.save(project);
		}
		languageRepository.deleteInvalid();
		projectRepository.deleteInvalid();
	}

	private void invalidateLanguages() {
		List<Language> languages = languageRepository.all();

		for (int i = 0; i < languages.size(); i++) {
			Language language = languages.get(i);
			language.setValid(false);
			languageRepository.save(language);
		}
	}

	private void invalidateProjects() {
		List<Project> projects = projectRepository.all();

		for (int i = 0; i < projects.size(); i++) {
			Project project = projects.get(i);
			project.setValid(false);
			projectRepository.save(project);
		}
	}
}