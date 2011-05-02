package org.kth.cos.android.sw.data;

public class Response {
	private Status status;
	private String message;
	private Object response;

	public Response(Status status, String message) {
		this.status = status;
		this.message = message;
	}

	public Response(Status status, String message, Object response) {
		this.status = status;
		this.message = message;
		this.response = response;
	}

	public Status getStatus() {
		return status;
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

}
