package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;

public class AttributeService extends AuthenticatedWebService {

	public AttributeService(String email, String auth_token, String dataServer) {
		super(dataServer, email, auth_token);
	}
	
	//http://localhost:3000/profiles/3/all_attributes.json?email=ashrafuzzaman.g2@gmail.com&auth_token=7w38pGIRZ1cS1fpVpP9C
	public Response getAttributeList(int profileId) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		Response response = get("/profiles/" + profileId +"/all_attributes.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Attributes list");
			//{"profile_attributes":[{"name":"First name","id":1,"selected":true,"value":null,"attribute_type":"string"},{"name":"Last name","id":2,"selected":true,"value":null,"attribute_type":"string"},{"name":"Sex","id":3,"selected":true,"value":null,"attribute_type":"string"},{"name":"Phone","id":4,"selected":true,"value":null,"attribute_type":"string"},{"name":"email","id":5,"selected":true,"value":null,"attribute_type":"string"},{"name":"Born on","id":6,"selected":true,"value":null,"attribute_type":"date"},{"name":"Current city","id":7,"selected":true,"value":null,"attribute_type":"date"}]}
			response.setResponse(createList(response.getResponseJson(), "profile_attributes", "profile_attribute", new String[] {"id", "name", "value", "attribute_type", "selected"}));
		}
		return response;
	}

	public Response getFriendsAttributeList(String friendsDatastore,String friendsEmail, String sharedKey) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		setBaseUrl(friendsDatastore);
		params.put("friends_email", email);
		params.put("email", friendsEmail);
		params.put("shared_key", sharedKey);
		Response response = get("/profile_attributes/friends_profile_attribute.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Attributes list");
			response.setResponse(createList(response.getResponseJson(), "profile_attributes", "profile_attribute", new String[] {"id", "name", "value", "attribute_type"}));
		}
		return response;
	}

	public Response updateAttributeList(int profileId, List<String> attributes) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		String attributeIds = join(attributes, ",");
		params.put("ids", attributeIds);
		Response response = post("/profiles/" + profileId +"/create_attributes.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Attributes updated");
		}
		return response;
	}
	
	public Response updateAttributeValue(int attrId, String value) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("profile_attribute[value]", value);
		Response response = put("/profile_attributes/" + attrId +".json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Value updated");
		}
		return response;
	}
	
	public Response deleteAttribute(int attrId) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		Response response = delete("/profile_attributes/" + attrId +".json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Value deleted");
		}
		return response;
	}
	
	public Response createAttribute(int profileId, String name, String value) throws ClientProtocolException, IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("profile_id", Integer.toString(profileId));
		params.put("profile_attribute[name]", name);
		params.put("profile_attribute[value]", value);
		Response response = post("/profile_attributes.json", params);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			response.setMessage("Attribute created");
			Map<String, String> objMap = createMap(response.getResponseJson(), "profile_attribute", new String[] {"id", "name", "value", "attribute_type"});
			objMap.put("selected", "true");
			response.setResponse(objMap);
		}
		return response;
	}
	
	public static String join(List<String> ids, String separator) {
	    StringBuffer sb = new StringBuffer();
	    for (int i=0; i < ids.size(); i++) {
	        if (i != 0) sb.append(separator);
	  	    sb.append(ids.get(i));
	  	}
	  	return sb.toString();
	}

}