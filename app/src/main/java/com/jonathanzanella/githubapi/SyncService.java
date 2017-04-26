package com.jonathanzanella.githubapi;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.github.GitHubService;
import com.jonathanzanella.githubapi.github.GithubRepository;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageRepository;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncService extends IntentService {
	private LanguageRepository languageRepository;
	private ProjectRepository projectRepository;

	public SyncService() {
		super("SyncService");

		DatabaseHelper databaseHelper = new DatabaseHelper(this);
		languageRepository = new LanguageRepository(new RepositoryImpl<Language>(databaseHelper));
		projectRepository = new ProjectRepository(new RepositoryImpl<Project>(databaseHelper));
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		final GitHubService service = retrofit.create(GitHubService.class);
		service.listRepos("BearchInc").enqueue(new Callback<List<GithubRepository>>() {
			@Override
			public void onResponse(Call<List<GithubRepository>> call, Response<List<GithubRepository>> response) {
				for (final GithubRepository githubRepository : response.body()) {
					Language language = languageRepository.findByName(githubRepository.getLanguage());
					if(language == null) {
						language = new Language();
						language.setName(githubRepository.getLanguage());
						languageRepository.save(language);
					}
					Project project = projectRepository.findByName(githubRepository.getName());
					if(project == null) {
						project = new Project();
						project.setName(githubRepository.getName());
						project.setLanguageId(language.getId());
						projectRepository.save(project);
					}
				}
			}

			@Override
			public void onFailure(Call<List<GithubRepository>> call, Throwable t) {
				Log.e(SyncService.class.getSimpleName(), "Error in github request=" + t.getMessage());
			}
		});
	}
}
