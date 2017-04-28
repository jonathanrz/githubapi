package com.jonathanzanella.githubapi.sync;

import com.jonathanzanella.githubapi.github.GitHubRepository;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageRepository;
import com.jonathanzanella.githubapi.mock.RepositoryMock;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SyncDataTest {
	private LanguageRepository languageRepository;
	private ProjectRepository projectRepository;
	private SyncData syncData;
	private DateTime localCreatedAt = new DateTime(2017, 4, 27, 20, 0, 0, DateTimeZone.UTC);
	private DateTime serverCreatedAt = new DateTime(2017, 4, 27, 20, 40, 0, DateTimeZone.UTC);

	@Before
	public void setUp() throws Exception {
		languageRepository = new LanguageRepository(new RepositoryMock<Language>());
		projectRepository = new ProjectRepository(new RepositoryMock<Project>());
		syncData = new SyncData(languageRepository, projectRepository);

		generateData(languageRepository, projectRepository);
	}

	private void generateData(LanguageRepository languageRepository, ProjectRepository projectRepository) {
		Language python = new Language();
		python.setName("Python");
		languageRepository.save(python);

		Project pythonProject1 = new Project();
		pythonProject1.setName("PythonProject1");
		pythonProject1.setLanguageId(python.getId());
		pythonProject1.setCreatedAt(localCreatedAt);
		projectRepository.save(pythonProject1);

		Project pythonProject2 = new Project();
		pythonProject2.setName("PythonProject2");
		pythonProject2.setLanguageId(python.getId());
		projectRepository.save(pythonProject2);
	}

	private List<GitHubRepository> generateGitHubData() {
		List<GitHubRepository> repositories = new ArrayList<>();
		GitHubRepository repository = new GitHubRepository();
		repository.setName("PythonProject1");
		repository.setLanguage("Python");
		repository.setCreatedAt(serverCreatedAt);
		repositories.add(repository);
		repository = new GitHubRepository();
		repository.setName("PythonProject3");
		repository.setLanguage("Python");
		repositories.add(repository);
		repository = new GitHubRepository();
		repository.setName("PythonProject4");
		repository.setLanguage("Python");
		repositories.add(repository);
		return repositories;
	}

	@Test
	public void syncDataSuccessfully() throws Exception {
		syncData.syncData(generateGitHubData());

		List<Language> languages = languageRepository.all();
		assertThat(languages.size(), is(1));
		assertThat(languages.get(0).getName(), is("Python"));

		Long languageId = languages.get(0).getId();

		List<Project> projects = projectRepository.all();
		assertThat(projects.size(), is(3));
		assertThat(projects.get(0).getName(), is("PythonProject1"));
		assertThat(projects.get(0).getLanguageId(), is(languageId));
		assertThat(projects.get(0).getCreatedAt(), is(serverCreatedAt));
		assertThat(projects.get(1).getName(), is("PythonProject3"));
		assertThat(projects.get(1).getLanguageId(), is(languageId));
		assertThat(projects.get(2).getName(), is("PythonProject4"));
		assertThat(projects.get(2).getLanguageId(), is(languageId));
	}
}