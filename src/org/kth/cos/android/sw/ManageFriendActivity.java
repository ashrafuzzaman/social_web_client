package org.kth.cos.android.sw;

import java.util.List;

import org.kth.cos.android.sw.data.Friend;
import org.kth.cos.android.sw.data.FriendManager;
import org.kth.cos.android.sw.data.Profile;
import org.kth.cos.android.sw.data.ProfileManager;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.FriendService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ManageFriendActivity extends BaseActivity {
	private List<Friend> friendList;
	private List<Profile> profiles;
	private ListView lstView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_list);
		setTitle("Friend List");
		friendList = new FriendManager(this).fetchAllFriend();
		lstView = (ListView) findViewById(R.id.lstView);
		lstView.setAdapter(new FriendListAdapter(this, friendList));
		profiles = new ProfileManager(ManageFriendActivity.this).fetchAllProfile();
	}

	class FriendListAdapter extends ArrayAdapter<Friend> {

		public FriendListAdapter(Context context, List<Friend> objects) {
			super(context, R.id.txtStatus, objects);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.friend_list_rowlayout, null);
			}
			final Friend friend = friendList.get(position);
			final TextView txtEmail = (TextView) v.findViewById(R.id.txtEmail);
			if (friendList == null || friendList.size() <= 0)
				return v;
			txtEmail.setText(friend.getEmail());
			txtEmail.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(ManageFriendActivity.this);
					builder.setMessage("Options           ");
					builder.setNegativeButton("View", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Intent myIntent = new Intent(ManageFriendActivity.this, FriendsProfileActivity.class);
							myIntent.putExtra("email", friend.getEmail());
							ManageFriendActivity.this.startActivity(myIntent);
							ManageFriendActivity.this.finish();

							dialog.dismiss();
						}
					}).setNeutralButton("Share", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							promtShareProfile(friend);
							dialog.dismiss();
						}
					});
					builder.create().show();
				}

				public void promtShareProfile(final Friend friend) {
					AlertDialog.Builder builder = new AlertDialog.Builder(ManageFriendActivity.this);
					builder.setTitle("Profile to share status");
					builder.setItems(ProfileManager.fetchAllProfileName(profiles), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int profileIndex) {
							UserAccount account = UserAccount.getAccount(ManageFriendActivity.this);
							try {
								new FriendService(account.getEmail(), account.getDataAuthToken(), account.getDataStoreServer()).attachProfile(
										friend.getEmail(), profiles.get(profileIndex).getProfileId());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					builder.create().show();
				}
			});
			return v;
		}
	}

}
