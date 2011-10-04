package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.UserAccount;

public class DataAuthenticationService extends AuthenticationService {

	public DataAuthenticationService(String dataServer) {
		super(dataServer);
	}
	
	public Response register(String email, String password)
			throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("password", password);
		Response response = post("/api/users.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setResponse(new UserAccount(email, password, ""));
		}
		return response;
	}

	@Override
	protected void setAuthToken(UserAccount profile, String authToken) {
		profile.setDataAuthToken(authToken);
	}

}
