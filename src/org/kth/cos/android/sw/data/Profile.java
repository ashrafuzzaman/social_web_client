package org.kth.cos.android.sw.data;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class Profile {
	private String email;
	private String password;
	private String authToken;
	private String dataAuthToken;
	private String dataStoreServer;

	public static final String PREFS_NAME = "ProfilePreferrence";
	public static final String USER_EMAIL = "USER_EMAIL";
	public static final String USER_PASSWORD = "USER_PASSWORD";
	public static final String AUTH_TOKEN = "AUTH_TOKEN";
	public static final String DATA_AUTH_TOKEN = "DATA_AUTH_TOKEN";
	public static final String DATA_STORE_SERVER = "DATA_STORE_SERVER";

	public Profile() {
		this.email = "";
		this.password = "";
		this.authToken = "";
		this.dataAuthToken = "";
		this.dataStoreServer = "";
	}

	public Profile(String email, String password, String authToken,
			String dataStoreServer) {
		super();
		this.email = email;
		this.password = password;
		this.authToken = authToken;
		this.dataStoreServer = dataStoreServer;
	}

	public Profile(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Profile(String email, String password, String authToken) {
		this.email = email;
		this.password = password;
		this.authToken = authToken;
	}

	public static Profile getProfile(Activity currentActivity) {
		SharedPreferences settings = currentActivity.getSharedPreferences(
				PREFS_NAME, 0);
		return new Profile(settings.getString(USER_EMAIL, ""), settings
				.getString(USER_PASSWORD, ""), settings.getString(AUTH_TOKEN,
				""));
	}

	public boolean isSignedIn() {
		return getAuthToken() != null && !getAuthToken().equals("");
	}

	public boolean isUnregisteredUser() {
		return email == null || email.equals("");
	}

	public void clearProfile(Activity currentActivity) {
		email = "";
		password = "";
		authToken = "";
		save(currentActivity);
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getDataStoreServer() {
		return dataStoreServer;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setDataAuthToken(String dataAuthToken) {
		this.dataAuthToken = dataAuthToken;
	}

	public String getDataAuthToken() {
		return dataAuthToken;
	}

	public void save(Activity currentActivity) {
		SharedPreferences settings = currentActivity.getSharedPreferences(
				PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		if (!TextUtils.isEmpty(email))
			editor.putString(USER_EMAIL, email);
		if (!TextUtils.isEmpty(password))
			editor.putString(USER_PASSWORD, password);
		if (!TextUtils.isEmpty(authToken))
			editor.putString(AUTH_TOKEN, authToken);
		if (!TextUtils.isEmpty(dataAuthToken))
			editor.putString(DATA_AUTH_TOKEN, dataAuthToken);
		editor.commit();
	}

}
