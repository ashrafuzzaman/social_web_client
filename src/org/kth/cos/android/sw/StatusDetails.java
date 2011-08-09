package org.kth.cos.android.sw;

import java.util.List;

import org.kth.cos.android.sw.data.Comment;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.Status;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.CommentService;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StatusDetails extends BaseActivity {
	private Status status;
	private List<Comment> comments;
	final Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status_with_comments);
		setTitle("Status");
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			status = (Status) extras.getParcelable("status");
			((TextView) findViewById(R.id.txtStatus)).setText(status.getValue());
			((TextView) findViewById(R.id.txtPostedBy)).setText(status.getPostedBy());
			((TextView) findViewById(R.id.txtPostedAt)).setText(status.getPostedAtStr());
			startLoadingList();
		}
	}

	private void generateList() {
		CommentService statusService = getCommentService();
		// FriendManager friendManager = new
		// FriendManager(FriendsStatusActivity.this);
		// List<Friend> friendList = friendManager.fetchAllFriend();
		try {
			String friendsEmail = status.getPostedBy();
			Response response = statusService.getMyComments("Status", status.getId(), friendsEmail);
			if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
				comments = (List<Comment>) (response.getResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void promptComment() {
		final Dialog dialog = new Dialog(StatusDetails.this);
		dialog.setContentView(R.layout.post_comment);
		((Button) dialog.findViewById(R.id.btnCancel)).setOnClickListener((new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		}));
		((Button) dialog.findViewById(R.id.btnPost)).setOnClickListener((new View.OnClickListener() {
			public void onClick(View v) {
				CommentService commentService = getCommentService();
				String comment = ((TextView) dialog.findViewById(R.id.txtComment)).getText().toString();
				try {
					commentService.postComment("Status", status.getId(), comment, status.getPostedBy());
					startLoadingList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		}));

		dialog.show();
	}

	private void updateStatusListInUI() {
		((ListView) StatusDetails.this.findViewById(R.id.lstComments)).setAdapter(new CommentListAdapter(this, comments));
	}

	final Runnable resultUpdater = new Runnable() {
		public void run() {
			updateStatusListInUI();
		}
	};

	protected void startLoadingList() {
		Thread t = new Thread() {
			public void run() {
				generateList();
				mHandler.post(resultUpdater);
			}
		};
		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.status_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_comment:
			promptComment();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private CommentService getCommentService() {
		UserAccount account = UserAccount.getAccount(this);
		CommentService service = new CommentService(account.getEmail(), account.getDataAuthToken());
		return service;
	}

	class CommentListAdapter extends ArrayAdapter<Comment> {

		public CommentListAdapter(Context context, List<Comment> objects) {
			super(context, R.id.txtStatus, objects);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.comment_rowlayout, null);
			}
			final Comment comment = comments.get(position);
			((TextView) v.findViewById(R.id.txtComment)).setText(comment.getText());
			((TextView) v.findViewById(R.id.txtPostedBy)).setText(comment.getPostedBy());
			((TextView) v.findViewById(R.id.txtPostedAt)).setText(comment.getPostedAtStr());
			return v;
		}
	}

}