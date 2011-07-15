package org.kth.cos.android.sw.network;

import java.util.HashMap;

public class AuthenticatedWebService extends WebServiceBase {
	protected String email;
	protected String auth_token;

	public AuthenticatedWebService(String baseUrl, String email, String auth_token) {
		super(baseUrl);
		this.email = email;
		this.auth_token = auth_token;
	}
	
	protected void putAuthHeader(HashMap<String, String> params) {
		putAuthHeader(params, email, auth_token);
	}

}
