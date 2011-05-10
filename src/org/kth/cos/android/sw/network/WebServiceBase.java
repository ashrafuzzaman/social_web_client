package org.kth.cos.android.sw.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;

public class WebServiceBase {
	private String baseUrl;
	
	public WebServiceBase(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public Response post(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		// create http post
		HttpPost httppost = createHttpPost(baseUrl + path, params);

		// Execute HTTP Post Request
		HttpResponse response = httpclient.execute(httppost);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has("error")) {
			return new Response(Status.STATUS_ERROR, rootJson.getJSONArray("error").getString(0));
		} else {
			return new Response(Status.STATUS_SUCCESS, rootJson);
		}
	}

	private HttpPost createHttpPost(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		for (Entry<String, String> param : params.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return httppost;
	}

	private JSONObject getJsonResponse(HttpResponse response) throws IOException, JSONException {
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		response.getEntity().writeTo(ostream);
		return new JSONObject(ostream.toString());
	}

}
