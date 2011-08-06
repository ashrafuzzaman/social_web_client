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

public class FriendManager extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "social_web";
	public static final String TABLE_NAME = "friends";

	public FriendManager(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
	}

	public void createTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, data_store TEXT, shared_key TEXT)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS  " + TABLE_NAME);
		onCreate(db);
	}

	public void refrashTable() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

	public void addFriend(String email, String data_store, String shared_key) {
		if (fetchFriend(email) == null) {
			ContentValues values = new ContentValues();
			values.put("email", email);
			values.put("data_store", data_store);
			values.put("shared_key", shared_key);
			SQLiteDatabase database = getWritableDatabase();
			database.insert(TABLE_NAME, null, values);
			database.close();
		}
	}

	public Friend fetchFriend(String email) {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[] { "email", "data_store", "shared_key" }, "email = ?", new String[] { email }, null,
				null, null);
		cursor.moveToFirst();
		Friend friend = getFriend(cursor);
		database.close();
		return friend;
	}

	public String getDatastore(String email) {
		return fetchFriend(email).getDataStore();
	}

	public List<Friend> fetchAllFriend() {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[] { "email", "data_store", "shared_key" }, null, null, null, null, null);
		cursor.moveToFirst();
		List<Friend> friendList = new ArrayList<Friend>();
		do {
			friendList.add(getFriend(cursor));
		} while (cursor.moveToNext());
		cursor.close();
		database.close();
		return friendList;
	}

	private Friend getFriend(Cursor cursor) {
		if (cursor.getCount() > 0) {
			return new Friend(cursor.getString(cursor.getColumnIndex("email")), cursor.getString(cursor.getColumnIndex("data_store")),
					cursor.getString(cursor.getColumnIndex("shared_key")));
		}
		return null;
	}

	public void addFriend(Friend friend) {
		addFriend(friend.getEmail(), friend.getDataStore(), friend.getSharedKey());
	}

}
