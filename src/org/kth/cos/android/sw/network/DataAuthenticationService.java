package org.kth.cos.android.sw.network;

import org.kth.cos.android.sw.data.UserAccount;

public class DataAuthenticationService extends AuthenticationService {

	public DataAuthenticationService(String dataServer) {
		super(dataServer);
	}

	@Override
	protected void setAuthToken(UserAccount profile, String authToken) {
		profile.setDataAuthToken(authToken);
	}

}
