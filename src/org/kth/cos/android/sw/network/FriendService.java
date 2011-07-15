package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;

import android.util.Log;

public class FriendService extends AuthenticatedWebService {

	public FriendService(String email, String auth_token) {
		super(DataHosts.DATA_SERVER, email, auth_token);
	}

	public Response doFriendRequest(String friendsEmail, String friendsDataServer, String myDataServer) throws ClientProtocolException, IOException,
			JSONException {
		String sharedKey = generateSharedKey();
		Response response = postFriendRequest(friendsEmail, friendsDataServer, sharedKey);
		if (response.isOk()) {
			response = saveFriendRequested(friendsEmail, sharedKey, myDataServer);
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
		Response response = post("/friends/handle_friend_req.json", params);
		if (response.isOk()) {
			response.setMessage("Friend request sent");
		}
		return response;
	}

	public Response saveFriendRequested(String friendsEmail, String sharedKey, String myDataServer) throws ClientProtocolException, IOException,
			JSONException {
		setBaseUrl(myDataServer);
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("friend_with", friendsEmail);
		params.put("shared_key", sharedKey);
		Response response = post("/friends/save_friend_requested.json", params);
		if (response.getStatus() == Status.STATUS_SUCCESS) {
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