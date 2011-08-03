package org.kth.cos.android.sw.data;

import org.json.JSONObject;

public class Response {
	private ResponseStatus status;
	private String message;
	private Object response;
	private JSONObject responseJson;

	public Response(ResponseStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public Response(ResponseStatus status, String message, Object response) {
		this.status = status;
		this.message = message;
		this.response = response;
	}

	public Response(ResponseStatus status, JSONObject responseJson) {
		this.status = status;
		this.responseJson = responseJson;
	}

	public ResponseStatus getStatus() {
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
	
	public boolean isOk() {
		return status == ResponseStatus.STATUS_SUCCESS;
	} 

	public boolean hasError() {
		return status == ResponseStatus.STATUS_ERROR;
	} 

}
