package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;

public class DataServerService extends AuthenticatedWebService {

	public DataServerService(String email, String auth_token) {
		super(DataHosts.AUTH_SERVER, email, auth_token);
	}

	public Response updateDataServiceHost(String data_service_host) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("data_service_host", data_service_host);
		Response response = post("/api/update_data_service_host.json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
			response.setMessage("Data service updated");
		}
		return response;
	}

	public Response getDataServiceHost(List<String> emails) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("emails", join(emails));
		Response response = post("/api/data_service_hosts.json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
			response.setMessage("Data servers found");
			response.setResponse(createProfileList(response.getResponseJson()));
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
	
	private List<UserAccount> createProfileList(JSONObject rootJsonObject) throws JSONException {
		JSONArray userArray = rootJsonObject.getJSONArray("users");
		List<UserAccount> profiles = new ArrayList<UserAccount>();
		for (int i = 0; i < userArray.length(); i++) {
			JSONObject userObject = userArray.getJSONObject(i).getJSONObject("user");
			profiles.add(new UserAccount(userObject.getString("email"), "", "", userObject.getString("data_service_host")));
		}
		return profiles;
	}
	

}
