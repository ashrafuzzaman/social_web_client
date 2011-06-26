package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;

import android.util.Log;

public class AttributeService extends AuthenticatedWebService {

	public AttributeService(String email, String auth_token) {
		super(DataHosts.DATA_SERVER, email, auth_token);
	}
	
	//http://localhost:3000/profiles/3/all_attributes.json?email=ashrafuzzaman.g2@gmail.com&auth_token=7w38pGIRZ1cS1fpVpP9C
	public Response getAttributeList(int profileId) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		Response response = get("/profiles/" + profileId +"/all_attributes.json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
			response.setMessage("Profiles found");
			response.setResponse(createList(response.getResponseJson(), "profile_attributes", "profile_attribute", new String[] {"id", "name", "value", "attribute_type", "selected"}));
		}
		return response;
	}

	public Response createProfile(String profileName) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("profile[name]", profileName);
		Log.i("Profile create", "Creating Profile with :: " + profileName);
		Response response = post("/profiles.json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
			response.setMessage("Profile [" + profileName + "] created");
		}
		return response;
	}

	public Response deleteProfile(Integer profileId) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		Response response = delete("/profiles/" + profileId + ".json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
			response.setMessage("Profiles deleted");
		}
		return response;
	}

}
