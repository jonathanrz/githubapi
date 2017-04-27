package com.jonathanzanella.githubapi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.github.GitHubService;
import com.jonathanzanella.githubapi.github.GitHubRepository;
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

public class SyncService extends Service {
	class SyncServiceBinder extends Binder {
		SyncService getService() {
			return SyncService.this;
		}
	}

	interface DataDownloadListener {
		void onDataDownloaded();
		void onErrorDownloadingData();
	}

	private final IBinder binder = new SyncServiceBinder();
	private LanguageRepository languageRepository;
	private ProjectRepository projectRepository;
	private boolean downloadingData;
	private DataDownloadListener listener;

	public SyncService() {
		DatabaseHelper databaseHelper = new DatabaseHelper(this);
		languageRepository = new LanguageRepository(new RepositoryImpl<Language>(databaseHelper));
		projectRepository = new ProjectRepository(new RepositoryImpl<Project>(databaseHelper));
	}

	public boolean isDownloadingData() {
		return downloadingData;
	}

	public void setDataDownloadListener(DataDownloadListener listener) {
		this.listener = listener;
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

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		downloadGitHubData();
	}

	protected void downloadGitHubData() {
		downloadingData = true;
		final GsonBuilder builder = new GsonBuilder()
				.registerTypeAdapter(DateTime.class, new DateTimeDeserializer());

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create(builder.create()))
				.client(buildHttpClient())
				.build();

		final GitHubService service = retrofit.create(GitHubService.class);
		service.listGitHubRepositories("BearchInc").enqueue(new Callback<List<GitHubRepository>>() {
			@Override
			public void onResponse(Call<List<GitHubRepository>> call, Response<List<GitHubRepository>> response) {
				for (final GitHubRepository gitHubRepository : response.body()) {
					Language language = languageRepository.findByName(gitHubRepository.getLanguage());
					if(language == null) {
						language = new Language();
						language.setName(gitHubRepository.getLanguage());
						languageRepository.save(language);
					}
					Project project = projectRepository.findByName(gitHubRepository.getName());
					if(project == null) {
						project = new Project();
						project.setName(gitHubRepository.getName());
						project.setCreatedAt(gitHubRepository.getCreatedAt());
						project.setUpdatedAt(gitHubRepository.getUpdatedAt());
						project.setOpenIssues(gitHubRepository.getOpenIssues());
						project.setLanguageId(language.getId());
						projectRepository.save(project);
					}
				}
				downloadingData = false;
				if(listener != null)
					listener.onDataDownloaded();
			}

			@Override
			public void onFailure(Call<List<GitHubRepository>> call, Throwable t) {
				Log.e(SyncService.class.getSimpleName(), "Error in github request=" + t.getMessage());
				downloadingData = false;
				if(listener != null)
					listener.onErrorDownloadingData();
			}
		});
	}
}
