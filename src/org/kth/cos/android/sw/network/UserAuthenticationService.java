package org.kth.cos.android.sw.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.Status;

public class UserAuthenticationService {
	public ResponseStatus register(String email, String password) throws ClientProtocolException, IOException, JSONException {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		// create http post
		HttpPost httppost = createHttpPostWithUserNameAndPass("http://social-web.heroku.com/api/users.json", email, password);

		// Execute HTTP Post Request
		HttpResponse response = httpclient.execute(httppost);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has("error")) {
			return new ResponseStatus(Status.STATUS_ERROR, rootJson.getJSONArray("error").getString(0));
		} else {
			return new ResponseStatus(Status.STATUS_SUCCESS, "User created");
		}
	}

	public ResponseStatus signin(String email, String password) throws ClientProtocolException, IOException, JSONException {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		// create http post
		HttpPost httppost = createHttpPostWithUserNameAndPass("http://social-web.heroku.com/api/sign_in.json", email, password);

		// Execute HTTP Post Request
		HttpResponse response = httpclient.execute(httppost);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has("error")) {
			return new ResponseStatus(Status.STATUS_ERROR, rootJson.getString("error"));
		} else {
			return new ResponseStatus(Status.STATUS_SUCCESS, "User Signed in");
		}
	}

	private HttpPost createHttpPostWithUserNameAndPass(String url, String email, String password) throws UnsupportedEncodingException {
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return httppost;
	}

	private JSONObject getJsonResponse(HttpResponse response) throws IOException, JSONException {
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		response.getEntity().writeTo(ostream);
		return new JSONObject(ostream.toString());
	}

}
