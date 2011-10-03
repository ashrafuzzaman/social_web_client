package org.kth.cos.android.sw;

import java.util.ArrayList;
import java.util.List;

import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.DataHosts;
import org.kth.cos.android.sw.network.DataServerService;
import org.kth.cos.android.sw.network.FriendService;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FindFriendActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_friend);

		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switchToMainActivity();
			}
		});

		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DataServerService dataServerService = getDataServerService();
				try {
					final String email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();
					List<String> emails = new ArrayList<String>();
					emails.add(email);
					Response response = dataServerService.getDataServiceHost(emails);
					List<UserAccount> friends = (List<UserAccount>) response.getResponse();
					if (response.isOk() && friends.size() > 0) {
						promptForFriendReq(friends.get(0).getEmail(), friends.get(0).getDataStoreServer());
					} else {
						showMessage(response.getMessage());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void promptForFriendReq(final String friendsEmail, final String dataServer) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to be friend with " + friendsEmail + "?");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		}).setNeutralButton("Send Request", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					UserAccount account = UserAccount.getAccount(FindFriendActivity.this);
					Log.i("User", account.toString());
					Response response = new FriendService(account.getEmail(), account.getDataAuthToken(), account.getDataStoreServer()).doFriendRequest(friendsEmail, dataServer, account.getDataStoreServer());
					showMessage(response.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		builder.create().show();
	}

	private void switchToMainActivity() {
		Intent myIntent = new Intent(FindFriendActivity.this, Welcome.class);
		FindFriendActivity.this.startActivity(myIntent);
		FindFriendActivity.this.finish();
	}

	private DataServerService getDataServerService() {
		UserAccount account = UserAccount.getAccount(this);
		DataServerService dataServerService = new DataServerService(account.getEmail(), account.getAuthToken());
		return dataServerService;
	}

}
