package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Friend;
import org.kth.cos.android.sw.data.FriendManager;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;

import android.content.Context;

public class FriendService extends AuthenticatedWebService {

	public FriendService(String email, String auth_token) {
		super(DataHosts.DATA_SERVER, email, auth_token);
	}

	// http://localhost:3001/friends/friend_reqests.json?email=ashrafuzzaman.g2@gmail.com&auth_token=7w38pGIRZ1cS1fpVpP9C
	public Response getFriendRequestsList() throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		Response response = get("/friends/friend_reqests.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Friend requests");
			response.setResponse(createList(response.getResponseJson(), "friends", "friend", new String[] { "id", "email", "status", "shared_key", "data_store" }));
		}
		return response;
	}

	public void syncFriendList(Context context) {
		try {
			List<Friend> friendList = (List<Friend>)getFriendList().getResponse();
			FriendManager friendManager = new FriendManager(context);
			for (Friend friend : friendList) {
				friendManager.addFriend(friend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Response getFriendList() throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		Response response = get("/friends.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Friend List");
			List<HashMap<String, String>> friendMapList = createList(response.getResponseJson(), "friends", "friend", new String[] { "id", "email", "status", "shared_key", "data_store" });
			List<Friend> friendList = new ArrayList<Friend>();
			for (HashMap<String,String> friendMap : friendMapList) {
				friendList.add(new Friend(friendMap.get("email"), friendMap.get("data_store"), friendMap.get("shared_key")));
			}
			response.setResponse(friendList);
		}
		return response;
	}

	public Response doFriendRequest(String friendsEmail, String friendsDataServer, String myDataServer) throws ClientProtocolException, IOException,
			JSONException {
		String sharedKey = generateSharedKey();
		Response response = postFriendRequest(friendsEmail, friendsDataServer, sharedKey);
		if (response.isOk()) {
			response = saveFriendRequested(friendsEmail, sharedKey, myDataServer, friendsDataServer);
			if (response.isOk()) {
				response.setMessage("Friend request sent");
			}
		}
		return response;
	}

	public Response postFriendRequest(String friendsEmail, String friendsDataServer, String sharedKey) throws ClientProtocolException, IOException,
			JSONException {
		setBaseUrl(friendsDataServer);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("friend_with", friendsEmail);
		params.put("shared_key", sharedKey);
		params.put("data_store", DataHosts.DATA_SERVER);
		Response response = post("/friends/handle_friend_req.json", params);
		if (response.isOk()) {
			response.setMessage("Friend request sent");
		}
		return response;
	}
	
	
	public Response acceptFriendRequestProcess(String friendsEmail, String friendsDataServer, String sharedKey, Context context) throws ClientProtocolException, IOException, JSONException {
		acceptFriendRequest(friendsEmail);
		Response response = notifyAcceptedFriendRequest(friendsEmail, friendsDataServer, sharedKey);
		FriendManager friendManager = new FriendManager(context);
		friendManager.addFriend(friendsEmail, friendsDataServer, sharedKey);
		return response;
	}
		
	public Response acceptFriendRequest(String friendsEmail) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("friend_with", friendsEmail);
		Response response = post("/friends/accept_friend_req.json", params);
		if (response.isOk()) {
			response.setMessage("Friend request sent");
		}
		return response;
	}

	public Response attachProfile(String friendsEmail, int profileId) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("friends_email", friendsEmail);
		params.put("profile_id", String.valueOf(profileId));
		Response response = post("/friends/attach_profile.json", params);
		if (response.isOk()) {
			response.setMessage("Profile attached");
		}
		return response;
	}

	public Response notifyAcceptedFriendRequest(String friendsEmail, String friendsDataServer, String sharedKey) throws ClientProtocolException,
			IOException, JSONException {
		setBaseUrl(friendsDataServer);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("friend_with", friendsEmail);
		params.put("shared_key", sharedKey);
		Response response = post("/friends/accepted_friend_req.json", params);
		if (response.isOk()) {
			response.setMessage("Friend request sent");
		}
		return response;
	}

	public Response saveFriendRequested(String friendsEmail, String sharedKey, String myDataServer, String friendsDataServer) throws ClientProtocolException, IOException,
			JSONException {
		setBaseUrl(myDataServer);
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("friend_with", friendsEmail);
		params.put("shared_key", sharedKey);
		params.put("data_store", DataHosts.DATA_SERVER);
		Response response = post("/friends/save_friend_requested.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Friend request sent");
		}
		return response;
	}

	private String generateSharedKey() {
		String shared_key_param = Long.toString(Calendar.getInstance().getTimeInMillis());
		try {
			SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey sharedKey = kf.generateSecret(new PBEKeySpec(shared_key_param.toCharArray()));
			return new String(sharedKey.getEncoded().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shared_key_param;
	}

}