package org.kth.cos.android.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.kth.cos.android.sw.data.FriendManager;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.FriendService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotificationActivity extends ListActivity {
	ArrayList<HashMap<String, String>> notificationList;

	final Handler mHandler = new Handler();
	Dialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Notifications");
		startLoadingList();
	}

	private void generateList() {
		FriendService friendService = getFriendService();
		try {
			Response response = friendService.getFriendRequestsList();
			if (response.getStatus() == Status.STATUS_SUCCESS) {
				notificationList = (ArrayList<HashMap<String, String>>) (response.getResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private FriendService getFriendService() {
		UserAccount account = UserAccount.getAccount(this);
		FriendService friendService = new FriendService(account.getEmail(), account.getDataAuthToken());
		return friendService;
	}

	private void updateAttributeListInUI() {
		this.setListAdapter(new NotificationListAdapter(this, notificationList));
		registerForContextMenu(getListView());
		this.progressDialog.dismiss();
	}

	final Runnable resultUpdater = new Runnable() {
		public void run() {
			updateAttributeListInUI();
		}
	};

	protected void startLoadingList() {
		this.progressDialog = ProgressDialog.show(NotificationActivity.this, "Wait", "Loading notifications...", true);
		Thread t = new Thread() {
			public void run() {
				generateList();
				mHandler.post(resultUpdater);
			}
		};
		t.start();
	}

	protected void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void promptAcceptFrndReq(final String email, final String sharedKey) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to be friend with " + email);
		builder.setNeutralButton("Accept", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				FriendService friendService = getFriendService();
				try {
					friendService.acceptFriendRequest(email);
					friendService.notifyAcceptedFriendRequest(email, FriendManager.lookupDatastore(email), sharedKey);
					startLoadingList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		}).setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	class NotificationListAdapter extends ArrayAdapter<HashMap<String, String>> {

		public NotificationListAdapter(Context context, List<HashMap<String, String>> objects) {
			super(context, R.id.txtAttributeName, objects);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.notification_rowlayout, null);
			}
			final HashMap<String, String> notificationMap = getItem(position);
			if (notificationMap != null) {
				TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
				String title = notificationMap.get("email") + " wants to be friend with you.";
				txtTitle.setText(title);
				txtTitle.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						promptAcceptFrndReq(notificationMap.get("email"), notificationMap.get("shared_key"));
					}
				});
			}
			return v;
		}
	}

}
