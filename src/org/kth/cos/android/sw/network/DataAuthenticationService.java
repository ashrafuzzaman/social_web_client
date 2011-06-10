package org.kth.cos.android.sw.network;

import org.kth.cos.android.sw.data.UserAccount;

public class DataAuthenticationService extends AuthenticationService {

	public DataAuthenticationService() {
		super(DataHosts.DATA_SERVER);
	}

	@Override
	protected void setAuthToken(UserAccount profile, String authToken) {
		profile.setDataAuthToken(authToken);
	}

}
