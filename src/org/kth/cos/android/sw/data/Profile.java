package org.kth.cos.android.sw.data;

import android.app.Activity;
import android.content.SharedPreferences;

public class Profile {
	private String email;
	private String password;
	private String authToken;

	public static final String PREFS_NAME = "ProfilePreferrence";
	public static final String USER_EMAIL = "USER_EMAIL";
	public static final String USER_PASSWORD = "USER_PASSWORD";
	public static final String AUTH_TOKEN = "AUTH_TOKEN";

	public Profile() {
	}
	
	public Profile(String email, String password, String authToken) {
		this.email = email;
		this.password = password;
		this.authToken = authToken;
	}

	public static Profile getProfile(Activity currentActivity) {
		SharedPreferences settings = currentActivity.getSharedPreferences(
				PREFS_NAME, 0);
		return new Profile(settings.getString(USER_EMAIL, ""),
				settings.getString(USER_PASSWORD, ""),
				settings.getString(AUTH_TOKEN, ""));
	}

	public boolean isUnregisteredUser() {
		return email == null || email.equals("");
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

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public void save(Activity currentActivity) {
		SharedPreferences settings = currentActivity.getSharedPreferences(
				PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(USER_EMAIL, email);
		editor.putString(USER_PASSWORD, password);
		editor.putString(AUTH_TOKEN, authToken);
		editor.commit();
	}

}
