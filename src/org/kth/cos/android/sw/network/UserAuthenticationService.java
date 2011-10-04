package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.UserAccount;



public class UserAuthenticationService extends AuthenticationService {
	public UserAuthenticationService() {
		super(DataHosts.AUTH_SERVER);
	}
	
	public Response register(String email, String password, String dataServer)
			throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("password", password);
		params.put("data_service_host", dataServer);
		Response response = post("/api/users.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setResponse(new UserAccount(email, password, ""));
		}
		return response;
	}

	@Override
	protected void setAuthToken(UserAccount profile, String authToken) {
		profile.setAuthToken(authToken);
	}
}
