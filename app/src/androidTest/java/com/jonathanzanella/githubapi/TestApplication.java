package com.jonathanzanella.githubapi;

import android.app.Application;

import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageRepository;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import net.danlew.android.joda.JodaTimeAndroid;

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
		Language python = new Language();
		python.setName("Python");
		languageRepository.save(python);
		Language java = new Language();
		java.setName("Java");
		languageRepository.save(java);

		ProjectRepository projectRepository = new ProjectRepository(new RepositoryImpl<Project>(databaseHelper));
		Project python1 = new Project();
		python1.setName("Python1");
		python1.setLanguageId(python.getId());
		projectRepository.save(python1);
		Project python2 = new Project();
		python2.setName("Python2");
		python1.setLanguageId(python.getId());
		projectRepository.save(python2);
		Project java1 = new Project();
		java1.setName("Java1");
		java1.setLanguageId(java.getId());
		projectRepository.save(java1);
	}
}
