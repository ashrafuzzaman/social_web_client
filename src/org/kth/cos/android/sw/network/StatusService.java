package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Friend;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.Status;

import android.util.Log;

public class StatusService extends AuthenticatedWebService {

	public StatusService(String email, String auth_token) {
		super(DataHosts.DATA_SERVER, email, auth_token);
	}

	public Response getMyStatusList() throws ClientProtocolException, IOException, JSONException, ParseException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		return getStatusList("/statuses.json",params);
	}

	public Response getAllFriendsStatus(List<Friend> friendList) throws ClientProtocolException, IOException, JSONException, ParseException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		for (Friend friend : friendList) {
			Log.i("Friend", String.format("email :: %s, shared_key :: %s, Data store :: %s", friend.getEmail(), friend.getSharedKey(), friend.getDataStore()));
			return getFriendsStatus(friend.getEmail(), friend.getSharedKey(), friend.getDataStore());
		}
		return null;
	}

	public Response getFriendsStatus(String friendsEmail, String sharedKey, String dataStore) throws ClientProtocolException, IOException,
			JSONException, ParseException {
		HashMap<String, String> params = new HashMap<String, String>();
		setBaseUrl(dataStore);
		params.put("email", friendsEmail);
		params.put("friends_email", email);
		params.put("shared_key", sharedKey);
		return getStatusList("/statuses/friends_status.json", params);
	}

	private Response getStatusList(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException, ParseException {
		Response response = get(path, params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Status list");
			List<HashMap<String, String>> statusMapList = createList(response.getResponseJson(), "statuses", "status", new String[] { "id", "value",
					"created_at", "profile_name" });

			List<Status> statusList = new ArrayList<Status>();
			for (HashMap<String, String> statusMap : statusMapList) {
				statusList.add(new Status(statusMap.get("value"), statusMap.get("profile_name"), statusMap.get("created_at")));
			}
			response.setResponse(statusList);
		}
		return response;
	}

	public Response postStatus(String status, int profileId) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("status[value]", status);
		params.put("status[profile_id]", String.valueOf(profileId));
		Response response = post("/statuses.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Status posted");
		}
		return response;
	}

}