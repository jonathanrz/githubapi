package com.jonathanzanella.githubapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jonathanzanella.githubapi.language.LanguageAdapter;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.act_main_language_list);
		recyclerView.setAdapter(new LanguageAdapter(this));
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
	}
}
