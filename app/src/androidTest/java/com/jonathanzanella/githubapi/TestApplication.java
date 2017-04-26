package com.jonathanzanella.githubapi;

import android.app.Application;

import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageRepository;

public class TestApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		new DatabaseHelper(this).recreateTables();

		generateLanguageTestData();
	}

	private void generateLanguageTestData() {
		LanguageRepository languageRepository = new LanguageRepository(new RepositoryImpl<Language>(new DatabaseHelper(this)));
		Language language = new Language();
		language.setName("Python");
		languageRepository.save(language);
		language = new Language();
		language.setName("Java");
		languageRepository.save(language);
	}
}
