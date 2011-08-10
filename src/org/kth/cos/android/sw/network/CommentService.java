package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Comment;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;

public class CommentService extends AuthenticatedWebService {

	public CommentService(String email, String auth_token) {
		super(DataHosts.DATA_SERVER, email, auth_token);
	}

	public Response postComment(String resourceType, int resourceId, String comment, String friendsEmail) throws ClientProtocolException,
			IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("resource_type", resourceType);
		params.put("resource_id", String.valueOf(resourceId));
		params.put("comment", comment);
		if (!email.equals(friendsEmail)) {
			params.put("friends_email", friendsEmail);			
		}
		Response response = post("/comments/post_comment", params);
		if (response.isOk()) {
			response.setMessage("Comment posted");
		}
		return response;
	}

	public Response getMyComments(String resourceType, int resourceId, String friendsEmail) throws ClientProtocolException, IOException,
			JSONException, ParseException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("resource_type", resourceType);
		params.put("resource_id", String.valueOf(resourceId));
		if (!email.equals(friendsEmail)) {
			params.put("friends_email", friendsEmail);			
		}
		Response response = get("/comments", params);
		if (response.isOk()) {
			List<HashMap<String, String>> commentMapList = createList(response.getResponseJson(), "comments", "comment", new String[] { "id",
					"comment", "created_at" });

			List<Comment> commentList = new ArrayList<Comment>();
			for (HashMap<String, String> commentMap : commentMapList) {
				commentList.add(new Comment(commentMap.get("comment"), email, commentMap.get("created_at")));
			}
			response.setResponse(commentList);

		}
		return response;
	}

}