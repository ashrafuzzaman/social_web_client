package org.kth.cos.android.sw.network;

import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.UserAccount;

import android.app.Activity;

public class SigninService {

	public Response signIn(final String email, final String pass, Activity currentActivity, String dataServer) {
		Response response = null;
		try {
			response = new UserAuthenticationService().signin(email, pass);
			if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
				UserAccount account = (UserAccount) response.getResponse();
				account.save(currentActivity);

				response = new DataAuthenticationService(dataServer).signin(email, pass);
				if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
					account = (UserAccount) response.getResponse();
					account.setDataStoreServer(dataServer);
					account.update(currentActivity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
