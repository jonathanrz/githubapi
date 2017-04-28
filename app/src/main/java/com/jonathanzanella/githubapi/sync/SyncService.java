package com.jonathanzanella.githubapi.sync;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.jonathanzanella.githubapi.helper.DateTimeDeserializer;
import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.github.GitHubRepository;
import com.jonathanzanella.githubapi.github.GitHubService;
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
	public static final String GITHUB_USER = "BearchInc";

	public class SyncServiceBinder extends Binder {
		public SyncService getService() {
			return SyncService.this;
		}
	}

	public interface DataDownloadListener {
		void onDataDownloaded();
		void onErrorDownloadingData();
	}

	private final IBinder binder = new SyncServiceBinder();
	private boolean downloadingData;
	private DataDownloadListener listener;
	private SyncData syncData;

	public SyncService() {
		DatabaseHelper databaseHelper = new DatabaseHelper(this);
		syncData = new SyncData(new LanguageRepository(new RepositoryImpl<Language>(databaseHelper)),
				new ProjectRepository(new RepositoryImpl<Project>(databaseHelper)));
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

		final GitHubService service = buildRetrofitClient().create(GitHubService.class);
		service.listGitHubRepositories(GITHUB_USER).enqueue(new Callback<List<GitHubRepository>>() {
			@Override
			public void onResponse(Call<List<GitHubRepository>> call, Response<List<GitHubRepository>> response) {
				syncData.syncData(response.body());

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

	private Retrofit buildRetrofitClient() {
		final GsonBuilder builder = new GsonBuilder()
				.registerTypeAdapter(DateTime.class, new DateTimeDeserializer());

		return new Retrofit.Builder()
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create(builder.create()))
				.client(buildHttpClient())
				.build();
	}
}
