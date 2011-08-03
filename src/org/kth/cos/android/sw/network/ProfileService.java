package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.ProfileManager;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;

import android.content.Context;
import android.util.Log;

public class ProfileService extends AuthenticatedWebService {

	public ProfileService(String email, String auth_token) {
		super(DataHosts.DATA_SERVER, email, auth_token);
	}

	// http://192.168.0.10:3001/profiles.json?email=ashrafuzzaman.g2@gmail.com&auth_token=uUKDt0bGxyOTGbUwxWne
	public Response getProfileList(Context context) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		Response response = get("/profiles.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Profiles found");
			List<HashMap<String, String>> profileList = createList(response.getResponseJson(), "profiles", "profile", new String[] { "id", "name" });
			response.setResponse(profileList);
			ProfileManager profileManager = new ProfileManager(context);
			for (HashMap<String, String> profileMap : profileList) {
				profileManager.addProfile(Integer.parseInt(profileMap.get("id")), profileMap.get("name"));
			}
		}
		return response;
	}

	public Response createProfile(String profileName) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("profile[name]", profileName);
		Log.i("Profile create", "Creating Profile with :: " + profileName);
		Response response = post("/profiles.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Profile [" + profileName + "] created");
		}
		return response;
	}

	public Response deleteProfile(Integer profileId) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		Response response = delete("/profiles/" + profileId + ".json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Profiles deleted");
		}
		return response;
	}

}
