package com.jonathanzanella.githubapi;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jonathanzanella.githubapi.database.DatabaseHelper;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		Stetho.initializeWithDefaults(this);
		new DatabaseHelper(this).runMigrations();
	}
}