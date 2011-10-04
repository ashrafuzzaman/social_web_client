package org.kth.cos.android.sw.network;

import java.io.IOException;
import java.io.SequenceInputStream;
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

	public CommentService(String email, String auth_token, String dataServer) {
		super(dataServer, email, auth_token);
	}

	public Response postComment(String resourceType, int resourceId, String comment, String friendsEmail, int sqeunceNumber) throws ClientProtocolException,
			IOException, JSONException {
		HashMap<String, String> params = new HashMap<String, String>();
		putAuthHeader(params);
		params.put("resource_type", resourceType);
		params.put("resource_id", String.valueOf(resourceId));
		params.put("comment", comment);
		params.put("sequence_number", String.valueOf(sqeunceNumber));
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
		generateCommentList(response, email);
		return response;
	}

	public Response getFriendsComments(String resourceType, int resourceId, String friendsEmail, String dataServer, String sharedKey,
			String resourceBy) throws ClientProtocolException, IOException, JSONException, ParseException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", friendsEmail);
		params.put("resource_type", resourceType);
		params.put("resource_id", String.valueOf(resourceId));
		params.put("friends_email", email);
		params.put("resource_by", resourceBy);
		params.put("shared_key", sharedKey);
		setBaseUrl(dataServer);
		Response response = get("/comments/comments_of_friend", params);
		generateCommentList(response, friendsEmail);
		return response;
	}

	public void generateCommentList(Response response, String commentBy) throws JSONException, ParseException {
		if (response.isOk()) {
			List<HashMap<String, String>> commentMapList = createList(response.getResponseJson(), "comments", "comment", new String[] { "id",
					"comment", "created_at", "sequence_number" });

			List<Comment> commentList = new ArrayList<Comment>();
			for (HashMap<String, String> commentMap : commentMapList) {
				commentList.add(new Comment(commentMap.get("comment"), commentBy, commentMap.get("created_at"), commentMap.get("sequence_number")));
			}
			response.setResponse(commentList);

		}
	}

}