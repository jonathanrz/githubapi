package com.jonathanzanella.githubapi.database;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RepositoryImpl<T extends Model> implements Repository<T> {
	private DatabaseHelper databaseHelper;

	public RepositoryImpl(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	@Override
	public List<T> query(Table<T> table, Where where) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Select select = where.query();
		Cursor c = db.query(
				table.getName(),
				table.getProjection(),
				select.getWhere(),
				select.getParameters(),
				null,
				null,
				where.orderBy());

		try {
			List<T> sources = new ArrayList<>();
			c.moveToFirst();
			while (!c.isAfterLast()) {
				sources.add(table.fill(c));
				c.moveToNext();
			}
			return sources;
		} finally {
			db.close();
		}
	}

	@Override
	public long count(Table<T> table, Where where) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Select select = where.query();
		long count = DatabaseUtils.queryNumEntries(db, table.getName(), select.getWhere(), select.getParameters());
		db.close();
		return count;
	}

	@Override
	public void delete(Table<T> table, Where where) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		Select select = where.query();
		db.delete(table.getName(), select.getWhere(), select.getParameters());
	}

	@Override
	public void save(Table<T> table, T data) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		if (data.getId() == 0L) {
			try {
				Long newId = db.insertOrThrow(table.getName(), null, table.fillContentValues(data));
				data.setId(newId);
			} catch (SQLException e) {
				Log.e(getClass().getSimpleName(), "error inserting the record into the database, error=" + e.getMessage());
				throw e;
			}
		} else {
			Select select = new Where(Fields.ID).eq(data.getId()).query();
			db.update(table.getName(), table.fillContentValues(data), select.getWhere(), select.getParameters());
		}
		db.close();
	}
}
