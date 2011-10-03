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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;

import android.util.Log;

public class WebServiceBase {
	private static final String ERROR_ROOT = "error";
	private String baseUrl;

	public WebServiceBase(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	protected Response post(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = createHttpPost(baseUrl + path, params);
		HttpResponse response = httpclient.execute(httpPost);
		
		Log.i("Posting with", String.format("[%s] :", httpPost.getURI()));
		for (Entry<String, String> param : params.entrySet()) {
			Log.i("params", String.format("[%s] : [%s]", param.getKey(), param.getValue()));			
		}

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has(ERROR_ROOT)) {
			return new Response(ResponseStatus.STATUS_ERROR, rootJson.getString(ERROR_ROOT));
		} else {
			return new Response(ResponseStatus.STATUS_SUCCESS, rootJson);
		}
	}

	protected Response put(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpPost = createHttpPut(baseUrl + path, params);
		HttpResponse response = httpclient.execute(httpPost);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has(ERROR_ROOT)) {
			return new Response(ResponseStatus.STATUS_ERROR, rootJson.getString(ERROR_ROOT));
		} else {
			return new Response(ResponseStatus.STATUS_SUCCESS, rootJson);
		}
	}

	protected Response get(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = createHttpGet(baseUrl + path, params);
		HttpResponse response = httpclient.execute(httpGet);

		Log.i("Posting with", String.format("[%s] :", httpGet.getURI()));
		for (Entry<String, String> param : params.entrySet()) {
			Log.i("params", String.format("[%s] : [%s]", param.getKey(), param.getValue()));			
		}

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has(ERROR_ROOT)) {
			return new Response(ResponseStatus.STATUS_ERROR, rootJson.getString(ERROR_ROOT));
		} else {
			return new Response(ResponseStatus.STATUS_SUCCESS, rootJson);
		}
	}

	protected Response delete(String path, HashMap<String, String> params) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpDelete httpDelete = createHttpDelete(baseUrl + path, params);
		HttpResponse response = httpclient.execute(httpDelete);

		JSONObject rootJson = getJsonResponse(response);
		if (rootJson.has(ERROR_ROOT)) {
			return new Response(ResponseStatus.STATUS_ERROR, rootJson.getString(ERROR_ROOT));
		} else {
			return new Response(ResponseStatus.STATUS_SUCCESS, rootJson);
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

	private HttpPut createHttpPut(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
		HttpPut httpPut = new HttpPut(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		for (Entry<String, String> param : params.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}
		httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return httpPut;
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
		Log.i("Json Response", ostream.toString());
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
	
	final protected List<HashMap<String, String>> createList(JSONObject rootJsonObject, String rootElement, String eachRoot, String []properties) throws JSONException {
		JSONArray resourceArray = rootJsonObject.getJSONArray(rootElement);
		List<HashMap<String, String>> resources = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < resourceArray.length(); i++) {
			HashMap<String, String> resourceMap = createMap(resourceArray.getJSONObject(i), eachRoot, properties);
			resources.add(resourceMap);
		}
		return resources;
	}

	protected HashMap<String, String> createMap(JSONObject object, String elementRoot, String[] properties) throws JSONException {
		JSONObject resourceObject = object.getJSONObject(elementRoot);
		HashMap<String, String> resourceMap = new HashMap<String, String>();
		for (String property: properties) {
			resourceMap.put(property, resourceObject.getString(property));
		}
		return resourceMap;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

}
