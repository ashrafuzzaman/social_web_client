package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.kth.cos.android.sw.data.Profile;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;

public abstract class AuthenticationService extends WebServiceBase {

	public AuthenticationService(String baseUrl) {
		super(baseUrl);
	}

	public Response register(String email, String password)
			throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("password", password);
		Response response = post("/api/users.json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
			response.setResponse(new Profile(email, password));
		}
		return response;
	}

	public Response signin(String email, String password)
			throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("password", password);
		Response response = post("/api/sign_in.json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
			JSONObject json = response.getResponseJson().getJSONObject("user");
			Profile profile = new Profile(json.getString("email"), password);
			setAuthToken(profile, json.getString("authentication_token"));
			response.setMessage("User Signed in");
			response.setResponse(profile);
		}
		return response;
	}
	
	protected abstract void setAuthToken(Profile profile, String authToken);

}