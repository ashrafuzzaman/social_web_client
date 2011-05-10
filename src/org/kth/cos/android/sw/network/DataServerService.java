package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kth.cos.android.sw.data.Profile;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;

public class DataServerService extends WebServiceBase {

	public DataServerService(String baseUrl) {
		super("http://social-web.heroku.com");
	}

	public Response updateDataServiceHost(String email, String auth_token, String data_service_host) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("auth_token", auth_token);
		params.put("data_service_host", data_service_host);
		Response response = post("/api/update_data_service_host.json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
			response.setMessage("Data service updated");
		}
		return response;
	}

	public Response getDataServiceHost(String myEmail, String auth_token, List<String> emails) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", myEmail);
		params.put("auth_token", auth_token);
		params.put("emails", join(emails));
		Response response = post("/api/update_data_service_host.json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
			response.setMessage("Data servers found");
		}
		return response;
	}

	private String join(List<String> emails) {
		StringBuffer strEmails = new StringBuffer();
		if (emails.size() > 0) {
			strEmails.append(emails.get(0));
		}
		for (String email : emails) {
			strEmails.append(',');
			strEmails.append(email);
		}
		return strEmails.toString();
	}
	
	private List<Profile> createProfileList(JSONObject rootJsonObject) throws JSONException {
		JSONArray userArray = rootJsonObject.getJSONArray("users");
		List<Profile> profiles = new ArrayList<Profile>();
		for (int i = 0; i < userArray.length(); i++) {
			JSONObject userObject = userArray.getJSONObject(i);
			profiles.add(new Profile(userObject.getString("email"), "", "", userObject.getString("data_service_host")));
		}
		return profiles;
	}
	

}
