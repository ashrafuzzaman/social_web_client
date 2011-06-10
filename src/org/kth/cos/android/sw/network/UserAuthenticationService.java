package org.kth.cos.android.sw.network;

import org.kth.cos.android.sw.data.UserAccount;



public class UserAuthenticationService extends AuthenticationService {

	public UserAuthenticationService() {
		super(DataHosts.AUTH_SERVER);
	}
	
	@Override
	protected void setAuthToken(UserAccount profile, String authToken) {
		profile.setAuthToken(authToken);
	}
}
