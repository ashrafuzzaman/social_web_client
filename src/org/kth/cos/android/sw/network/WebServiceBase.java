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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;

import android.util.Log;

public class WebServiceBase {
	private static final String ERROR_ROOT = "error";
	private String baseUrl;

	public WebServiceBase(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public Response post(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = createHttpPost(baseUrl + path, params);
		HttpResponse response = httpclient.execute(httpPost);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has(ERROR_ROOT)) {
			return new Response(Status.STATUS_ERROR, rootJson.getString(ERROR_ROOT));
		} else {
			return new Response(Status.STATUS_SUCCESS, rootJson);
		}
	}

	public Response get(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = createHttpGet(baseUrl + path, params);
		HttpResponse response = httpclient.execute(httpGet);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has(ERROR_ROOT)) {
			return new Response(Status.STATUS_ERROR, rootJson.getString(ERROR_ROOT));
		} else {
			return new Response(Status.STATUS_SUCCESS, rootJson);
		}
	}

	public Response delete(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpDelete httpDelete = createHttpDelete(baseUrl + path, params);
		HttpResponse response = httpclient.execute(httpDelete);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has(ERROR_ROOT)) {
			return new Response(Status.STATUS_ERROR, rootJson.getString(ERROR_ROOT));
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
		HttpGet httpGet = new HttpGet(createUrlWithParam(url, params));
		Log.i("Http get url :: ", httpGet.getURI().toString());
		return httpGet;
	}

	private HttpDelete createHttpDelete(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
		HttpDelete httpDelete = new HttpDelete(createUrlWithParam(url, params));
		Log.i("HTTP Delete url :: ", httpDelete.getURI().toString());
		return httpDelete;
	}

	private JSONObject getJsonResponse(HttpResponse response) throws IOException, JSONException {
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		response.getEntity().writeTo(ostream);
		Log.d("Json Response", ostream.toString());
		return new JSONObject(ostream.toString());
	}

	private String createUrlWithParam(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuffer urlString = new StringBuffer(url);
		urlString.append("?");

		for (Entry<String, String> param : params.entrySet()) {
			urlString.append(param.getKey() + "=" + param.getValue() + "&");
		}
		return urlString.substring(0, urlString.length() - 1);
	}

	protected void putAuthHeader(HashMap<String, String> params, String email, String auth_token) {
		params.put("email", email);
		params.put("auth_token", auth_token);
	}

}
