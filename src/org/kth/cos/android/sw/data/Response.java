package org.kth.cos.android.sw.data;

import org.json.JSONObject;

public class Response {
	private Status status;
	private String message;
	private Object response;
	private JSONObject responseJson;

	public Response(Status status, String message) {
		this.status = status;
		this.message = message;
	}

	public Response(Status status, String message, Object response) {
		this.status = status;
		this.message = message;
		this.response = response;
	}

	public Response(Status status, JSONObject responseJson) {
		this.status = status;
		this.responseJson = responseJson;
	}

	public Status getStatus() {
		return status;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
	
	public JSONObject getResponseJson() {
		return responseJson;
	}

}
