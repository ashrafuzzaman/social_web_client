package org.kth.cos.android.sw.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

public class UserRegistrationService {
	public boolean register(String email, String password) throws ClientProtocolException, IOException, JSONException {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://social-web.heroku.com/api/users");

		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Execute HTTP Post Request
		HttpResponse response = httpclient.execute(httppost);

		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		response.getEntity().writeTo(ostream);

		//JSONObject jObject  = new JSONObject(ostream.toString());
		return true;
	}

}
