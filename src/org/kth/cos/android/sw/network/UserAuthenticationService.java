package org.kth.cos.android.sw.network;

import org.kth.cos.android.sw.data.Profile;



public class UserAuthenticationService extends AuthenticationService {

	public UserAuthenticationService() {
		super("http://social-web.heroku.com");
	}
	
	@Override
	protected void setAuthToken(Profile profile, String authToken) {
		profile.setAuthToken(authToken);
	}
}
