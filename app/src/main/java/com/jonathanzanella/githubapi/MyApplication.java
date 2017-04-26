package com.jonathanzanella.githubapi;

import android.app.Application;
import android.content.Intent;

import com.facebook.stetho.Stetho;
import com.jonathanzanella.githubapi.database.DatabaseHelper;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		Stetho.initializeWithDefaults(this);
		new DatabaseHelper(this).runMigrations();

		startService(new Intent(this, SyncService.class));
	}
}