package com.jonathanzanella.githubapi;

import android.app.Application;
import android.content.Intent;

import com.facebook.stetho.Stetho;
import com.jonathanzanella.githubapi.database.DatabaseHelper;

import net.danlew.android.joda.JodaTimeAndroid;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		JodaTimeAndroid.init(this);
		Stetho.initializeWithDefaults(this);
		new DatabaseHelper(this).runMigrations();

		startService(new Intent(this, SyncService.class));
	}
}