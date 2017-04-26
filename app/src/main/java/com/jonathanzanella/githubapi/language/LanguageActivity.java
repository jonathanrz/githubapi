package com.jonathanzanella.githubapi.language;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.jonathanzanella.githubapi.R;
import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.projects.ProjectAdapter;

public class LanguageActivity extends AppCompatActivity {
	public static final String KEY_LANGUAGE_ID = "KeyLanguageId";
	private RecyclerView projectsList;
	private Language language;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);

		storeBundle(savedInstanceState);
		storeBundle(getIntent().getExtras());

		if(language == null) {
			Log.e(getClass().getSimpleName(), "language not found, aborting");
			finish();
			return;
		}

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		displayHomeAsUp();
		setTitle(language.getName());

		projectsList = (RecyclerView) findViewById(R.id.act_language_project_list);
		projectsList.setAdapter(new ProjectAdapter(this, language));
		projectsList.setLayoutManager(new LinearLayoutManager(this));
	}

	private void displayHomeAsUp() {
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			if(toolbar != null)
				toolbar.setNavigationOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		}
	}

	private void storeBundle(Bundle bundle) {
		if(bundle == null)
			return;

		if(bundle.containsKey(KEY_LANGUAGE_ID)) {
			long languageId = bundle.getLong(KEY_LANGUAGE_ID);
			LanguageRepository repository = new LanguageRepository(new RepositoryImpl<Language>(new DatabaseHelper(this)));
			language = repository.findById(languageId);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(KEY_LANGUAGE_ID, language.getId());
	}
}
