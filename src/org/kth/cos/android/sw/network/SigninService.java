package org.kth.cos.android.sw.network;

import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;
import org.kth.cos.android.sw.data.UserAccount;

import android.app.Activity;

public class SigninService {

	public Response signIn(final String email, final String pass, Activity currentActivity) {
		Response response = null;
		try {
			response = new UserAuthenticationService().signin(email, pass);
			if (response.getStatus() == Status.STATUS_SUCCESS) {
				UserAccount profile = (UserAccount) response.getResponse();
				profile.save(currentActivity);

				response = new DataAuthenticationService().signin(email, pass);
				if (response.getStatus() == Status.STATUS_SUCCESS) {
					profile = (UserAccount) response.getResponse();
					profile.update(currentActivity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
