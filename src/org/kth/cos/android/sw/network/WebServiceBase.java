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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;

import android.util.Log;

public class WebServiceBase {
	private String baseUrl;
	
	public WebServiceBase(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public Response post(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = createHttpPost(baseUrl + path, params);
		HttpResponse response = httpclient.execute(httpPost);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has("error")) {
			return new Response(Status.STATUS_ERROR, rootJson.getJSONArray("error").getString(0));
		} else {
			return new Response(Status.STATUS_SUCCESS, rootJson);
		}
	}

	public Response get(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = createHttpGet(baseUrl + path, params);
		HttpResponse response = httpclient.execute(httpGet);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has("error")) {
			return new Response(Status.STATUS_ERROR, rootJson.getJSONArray("error").getString(0));
		} else {
			return new Response(Status.STATUS_SUCCESS, rootJson);
		}
	}

	private HttpPost createHttpPost(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		for (Entry<String, String> param : params.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return httpPost;
	}

	private HttpGet createHttpGet(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
		HttpGet httpGet = new HttpGet(url);
		for (Entry<String, String> param : params.entrySet()) {
			httpGet.addHeader(param.getKey(), param.getValue());
		}
		return httpGet;
	}

	private JSONObject getJsonResponse(HttpResponse response) throws IOException, JSONException {
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		response.getEntity().writeTo(ostream);
		Log.d("Json Response", ostream.toString());
		return new JSONObject(ostream.toString());
	}

}
