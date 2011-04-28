package org.kth.cos.android.sw.data;

import android.app.Activity;
import android.content.SharedPreferences;

public class Profile {
	private String email;
	private String password;

	public static final String PREFS_NAME = "ProfilePreferrence";
	public static final String USER_EMAIL = "USER_EMAIL";
	public static final String USER_PASSWORD = "USER_PASSWORD";

	private Profile(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public static Profile getProfile(Activity currentActivity) {
		SharedPreferences settings = currentActivity.getSharedPreferences(
				PREFS_NAME, 0);
		return new Profile(settings.getString(USER_EMAIL, ""),
				settings.getString(USER_PASSWORD, ""));
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

	public void setEmail(String email, Activity currentActivity) {
		SharedPreferences settings = currentActivity.getSharedPreferences(
				PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(USER_EMAIL, email);
		editor.commit();
		this.email = email;
	}

	public void setPassword(String password, Activity currentActivity) {
		SharedPreferences settings = currentActivity.getSharedPreferences(
				PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(USER_PASSWORD, password);
		editor.commit();
		this.password = password;
	}

}
