package org.kth.cos.android.sw.data;

public class ResponseStatus {
	private Status status;
	private String message;

	public ResponseStatus(Status status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}
