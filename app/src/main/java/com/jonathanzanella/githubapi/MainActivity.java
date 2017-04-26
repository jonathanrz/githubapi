package com.jonathanzanella.githubapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageActivity;
import com.jonathanzanella.githubapi.language.LanguageAdapter;

public class MainActivity extends AppCompatActivity implements LanguageAdapter.OnLanguageSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.act_main_language_list);
		LanguageAdapter adapter = new LanguageAdapter(this);
		adapter.setOnLanguageSelectedListener(this);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
	}

	@Override
	public void onLanguageSelected(Language language) {
		Intent i = new Intent(this, LanguageActivity.class);
		i.putExtra(LanguageActivity.KEY_LANGUAGE_ID, language.getId());
		startActivity(i);
	}
}
