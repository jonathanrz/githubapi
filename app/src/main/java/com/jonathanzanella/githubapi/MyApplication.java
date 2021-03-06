package com.jonathanzanella.githubapi;

import android.app.Application;
import android.content.Intent;

import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.sync.SyncService;

import net.danlew.android.joda.JodaTimeAndroid;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		JodaTimeAndroid.init(this);
		new DatabaseHelper(this).runMigrations();

		startService(new Intent(this, SyncService.class));
	}
}