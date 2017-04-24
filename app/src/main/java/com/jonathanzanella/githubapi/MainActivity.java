package com.jonathanzanella.githubapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		final GitHubService service = retrofit.create(GitHubService.class);
		service.listRepos("BearchInc").enqueue(new Callback<List<Repo>>() {
			@Override
			public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
				for (final Repo repo : response.body()) {
					Log.i("test", "repo=" + repo.name + " lang=" + repo.language);
				}
			}

			@Override
			public void onFailure(Call<List<Repo>> call, Throwable t) {
				Log.e("test", "Error repo=" + t.getMessage());
			}
		});
	}
}
