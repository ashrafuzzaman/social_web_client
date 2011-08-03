package org.kth.cos.android.sw.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kth.cos.android.sw.network.DataHosts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileManager extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "social_web";
	public static final String TABLE_NAME = "profiles";

	public ProfileManager(Context context) {
		super(context, DATABASE_NAME, null, 1);
		//createDatabase(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDatabase(db);
	}

	private void createDatabase(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, profile_id INTEGER, name TEXT)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS  " + TABLE_NAME);
		onCreate(db);
	}

	public void addProfile(int profileId, String name) {
		ContentValues values = new ContentValues();
		if (fetchProfile(profileId) == null) {
			values.put("profile_id", profileId);
			values.put("name", name);
			SQLiteDatabase database = getWritableDatabase();
			database.insert(TABLE_NAME, null, values);
			database.close();
		}
	}

	public Profile fetchProfile(int profileId) {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[] { "profile_id", "name" }, "profile_id = ?",
				new String[] { String.valueOf(profileId) }, null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			return getProfile(cursor);
		}
		return null;
	}

	public List<Profile> fetchAllProfile() {
		Cursor cursor = fetchProfileCursor();
		cursor.moveToFirst();
		List<Profile> friendList = new ArrayList<Profile>();
		if (cursor.getCount() > 0) {
			do {
				friendList.add(getProfile(cursor));
			} while (cursor.moveToNext());
		}
		return friendList;
	}
	
	public static String[] fetchAllProfileName(List<Profile> profiles) {
		String[] profileNames = new String[profiles.size()];
		for (int i = 0; i < profileNames.length; i++) {
			profileNames[i] = profiles.get(i).getName();
		}
		return profileNames;
	}

	public Cursor fetchProfileCursor() {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[] { "profile_id", "name" }, null, null, null, null, null);
		return cursor;
	}

	private Profile getProfile(Cursor cursor) {
		return new Profile(cursor.getInt(cursor.getColumnIndex("profile_id")), cursor.getString(cursor.getColumnIndex("name")));
	}

}
