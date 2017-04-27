package com.jonathanzanella.githubapi;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.github.GitHubService;
import com.jonathanzanella.githubapi.github.GithubRepository;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageRepository;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

	private OkHttpClient buildHttpClient() {
		return new OkHttpClient.Builder()
				.addInterceptor(new Interceptor() {
					@Override
					public okhttp3.Response intercept(Chain chain) throws IOException {
						Request request = chain.request();
						request = request.newBuilder()
								.addHeader("Accept", "application/vnd.github.v3+json")
								.build();
						return chain.proceed(request);
					}
				}).build();
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		final GsonBuilder builder = new GsonBuilder()
				.registerTypeAdapter(DateTime.class, new DateTimeDeserializer());

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create(builder.create()))
				.client(buildHttpClient())
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
						project.setCreatedAt(githubRepository.getCreatedAt());
						project.setUpdatedAt(githubRepository.getUpdatedAt());
						project.setOpenIssues(githubRepository.getOpenIssues());
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
