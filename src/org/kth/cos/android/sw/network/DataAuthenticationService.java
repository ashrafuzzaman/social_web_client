package org.kth.cos.android.sw.network;

import org.kth.cos.android.sw.data.Profile;

public class DataAuthenticationService extends AuthenticationService {

	public DataAuthenticationService() {
		super("http://social-web-data.heroku.com");
	}

	@Override
	protected void setAuthToken(Profile profile, String authToken) {
		profile.setDataAuthToken(authToken);
	}

}
