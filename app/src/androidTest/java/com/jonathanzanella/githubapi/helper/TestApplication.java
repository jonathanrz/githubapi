package com.jonathanzanella.githubapi.helper;

import android.app.Application;

import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageRepository;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

public class TestApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		JodaTimeAndroid.init(this);
		new DatabaseHelper(this).recreateTables();

		generateTestData();
	}

	private void generateTestData() {
		DatabaseHelper databaseHelper = new DatabaseHelper(this);

		LanguageRepository languageRepository = new LanguageRepository(new RepositoryImpl<Language>(databaseHelper));
		ProjectRepository projectRepository = new ProjectRepository(new RepositoryImpl<Project>(databaseHelper));

		generatePythonData(languageRepository, projectRepository);
		generateJavaData(languageRepository, projectRepository);
	}

	private void generatePythonData(LanguageRepository languageRepository, ProjectRepository projectRepository) {
		Language python = new Language();
		python.setName("Python");
		languageRepository.save(python);

		Project python1 = buildProject("Python1", 2);
		python1.setLanguageId(python.getId());
		projectRepository.save(python1);

		Project python2 = buildProject("Python2", 3);
		python2.setLanguageId(python.getId());
		projectRepository.save(python2);
	}

	private void generateJavaData(LanguageRepository languageRepository, ProjectRepository projectRepository) {
		Language java = new Language();
		java.setName("Java");
		languageRepository.save(java);

		Project java1 = buildProject("Java1", 3);
		java1.setLanguageId(java.getId());
		projectRepository.save(java1);
	}

	private Project buildProject(String name, int openIssues) {
		Project project = new Project();
		project.setName(name);
		project.setCreatedAt(new DateTime(2017, 4, 27, 21, 50, 0));
		project.setUpdatedAt(new DateTime(2017, 4, 27, 22, 50, 0));
		project.setOpenIssues(openIssues);
		return project;
	}
}
