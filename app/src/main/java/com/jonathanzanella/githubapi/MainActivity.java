package com.jonathanzanella.githubapi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageActivity;
import com.jonathanzanella.githubapi.language.LanguageAdapter;
import com.jonathanzanella.githubapi.sync.SyncService;

public class MainActivity extends AppCompatActivity implements LanguageAdapter.OnLanguageSelectedListener, ServiceConnection, SyncService.DataDownloadListener {
	private SyncService syncService;
	private TextView viewUpdateDataMessage;
	private LanguageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = new Intent(this, SyncService.class);
		bindService(intent, this, Context.BIND_AUTO_CREATE);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.act_main_language_list);
		viewUpdateDataMessage = (TextView) findViewById(R.id.act_main_updating_data_message);

		adapter = new LanguageAdapter(this);
		adapter.setOnLanguageSelectedListener(this);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(this);
	}

	@Override
	public void onLanguageSelected(Language language) {
		Intent i = new Intent(this, LanguageActivity.class);
		i.putExtra(LanguageActivity.KEY_LANGUAGE_ID, language.getId());
		startActivity(i);
	}

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
		Log.d(getClass().getSimpleName(), "onServiceConnected");
		SyncService.SyncServiceBinder binder = (SyncService.SyncServiceBinder) iBinder;
		syncService = binder.getService();
		syncService.setDataDownloadListener(this);

		if(syncService.isDownloadingData())
			viewUpdateDataMessage.setVisibility(View.VISIBLE);
	}

	@Override
	public void onServiceDisconnected(ComponentName componentName) {
		Log.d(getClass().getSimpleName(), "onServiceDisconnected");
		syncService.setDataDownloadListener(null);
		syncService = null;
	}

	@Override
	public void onDataDownloaded() {
		adapter.updateData(this);
		Toast.makeText(this, R.string.date_updated, Toast.LENGTH_SHORT).show();
		viewUpdateDataMessage.setVisibility(View.GONE);
	}

	@Override
	public void onErrorDownloadingData() {
		Toast.makeText(this, R.string.error_updating_date, Toast.LENGTH_SHORT).show();
	}
}
