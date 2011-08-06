package org.kth.cos.android.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.kth.cos.android.sw.data.Friend;
import org.kth.cos.android.sw.data.FriendManager;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.AttributeService;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class FriendsProfileActivity extends ListActivity {
	ArrayList<HashMap<String, String>> attributeList;
	final Handler mHandler = new Handler();
	Dialog progressDialog;
	private String friendsEmail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Profile");
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			friendsEmail = extras.getString("email");
			startLoadingList();
		}
	}

	private void generateList() {
		AttributeService attributeService = getAttributeService();
		try {
			Friend friend = new FriendManager(FriendsProfileActivity.this).fetchFriend(friendsEmail);
			Response response = attributeService.getFriendsAttributeList(friend.getDataStore(), friendsEmail, friend.getSharedKey());
			if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
				attributeList = (ArrayList<HashMap<String, String>>) (response.getResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AttributeService getAttributeService() {
		UserAccount account = UserAccount.getAccount(this);
		AttributeService attributeService = new AttributeService(account.getEmail(), account.getDataAuthToken());
		return attributeService;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.attribute_menu, menu);
		return true;
	}

	private void updateAttributeListInUI() {
		this.setListAdapter(new AttributeListAdapter(this, attributeList));
		registerForContextMenu(getListView());
		this.progressDialog.dismiss();
	}

	final Runnable resultUpdater = new Runnable() {
		public void run() {
			updateAttributeListInUI();
		}
	};

	protected void startLoadingList() {
		this.progressDialog = ProgressDialog.show(FriendsProfileActivity.this, "Wait", "Loading attributes...", true);
		Thread t = new Thread() {
			public void run() {
				generateList();
				mHandler.post(resultUpdater);
			}
		};
		t.start();
	}

	class AttributeListAdapter extends ArrayAdapter<HashMap<String, String>> {

		public AttributeListAdapter(Context context, List<HashMap<String, String>> objects) {
			super(context, R.id.txtAttributeName, objects);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.profile_attribute, null);
			}
			final HashMap<String, String> attributeMap = getItem(position);
			if (attributeMap != null) {
				((TextView) v.findViewById(R.id.txtName)).setText(attributeMap.get("name"));
				((TextView) v.findViewById(R.id.txtValue)).setText(attributeMap.get("value"));
			}
			return v;
		}

	}
}
