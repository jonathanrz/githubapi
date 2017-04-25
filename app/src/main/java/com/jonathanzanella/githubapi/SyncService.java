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
import com.jonathanzanella.githubapi.repo.Repo;
import com.jonathanzanella.githubapi.repo.RepoRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncService extends IntentService {
	private LanguageRepository languageRepository;
	private RepoRepository repoRepository;

	public SyncService() {
		super("SyncService");

		languageRepository = new LanguageRepository(new RepositoryImpl<Language>(new DatabaseHelper(this)));
		repoRepository = new RepoRepository(new RepositoryImpl<Repo>(new DatabaseHelper(this)));
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
					Repo repo = repoRepository.findByName(githubRepository.getName());
					if(repo == null) {
						repo = new Repo();
						repo.setName(githubRepository.getName());
						repo.setLanguageId(language.getId());
						repoRepository.save(repo);
					}
				}
			}

			@Override
			public void onFailure(Call<List<GithubRepository>> call, Throwable t) {
				Log.e("test", "Error repo=" + t.getMessage());
			}
		});
	}
}