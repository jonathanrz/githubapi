package com.jonathanzanella.githubapi;

import android.app.Application;

import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageRepository;
import com.jonathanzanella.githubapi.repo.Repo;
import com.jonathanzanella.githubapi.repo.RepoRepository;

public class TestApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

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

		RepoRepository repoRepository = new RepoRepository(new RepositoryImpl<Repo>(databaseHelper));
		Repo python1 = new Repo();
		python1.setName("Python1");
		python1.setLanguageId(python.getId());
		repoRepository.save(python1);
		Repo python2 = new Repo();
		python2.setName("Python2");
		python1.setLanguageId(python.getId());
		repoRepository.save(python2);
		Repo java1 = new Repo();
		java1.setName("Java1");
		java1.setLanguageId(java.getId());
		repoRepository.save(java1);
	}
}
