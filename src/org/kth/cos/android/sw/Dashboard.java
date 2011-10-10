package org.kth.cos.android.sw;

import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.FriendService;
import org.kth.cos.android.sw.network.ProfileService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dashboard extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		setTitle("Welcome");
		syncFriendList();
		syncProfileList();
		generateView();
	}

	private void generateView() {
		UserAccount account = UserAccount.getAccount(this);
		if (!account.isSignedIn()) {
			startNewActivity(LoginActivity.class, false);
		} else {
			((TextView) findViewById(R.id.txtLogin)).setText(account.getEmail());
//			attachBtnClearCach();
		}
//		attachBtnExit();
	}

	private void attachBtnExit() {
		Button btnExit = (Button) findViewById(R.id.btnExit);
		makeVisible(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Dashboard.this.finish();
			}
		});
	}

	private void attachBtnClearCach() {
//		Button btnClrCach = (Button) findViewById(R.id.btnClrCach);
//		btnClrCach.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				UserAccount profile = UserAccount.getAccount(Welcome.this);
//				profile.clear(Welcome.this);
//				profile = UserAccount.getAccount(Welcome.this);
//				Log.i("INFO", String.format("Profile After clear:: %s [%s]", profile.getEmail(), profile.getAuthToken()));
//				new FriendManager(Welcome.this).refrashTable();
//				new ProfileManager(Welcome.this).refrashTable();
//				generateView();
//			}
//		});
	}

	private void syncFriendList() {
		new Thread() {
			@Override
			public void run() {
				UserAccount account = UserAccount.getAccount(Dashboard.this);
				FriendService friendService = new FriendService(account.getEmail(), account.getAuthToken(), account.getDataAuthToken(),
						account.getDataStoreServer());
				friendService.syncFriendList(Dashboard.this);
			}
		}.start();
	}

	private void syncProfileList() {
		new Thread() {
			@Override
			public void run() {
				UserAccount account = UserAccount.getAccount(Dashboard.this);
				ProfileService profileService = new ProfileService(account.getEmail(), account.getDataAuthToken(), account.getDataStoreServer());
				profileService.syncProfileList(Dashboard.this);
			}
		}.start();
	}

	public void onClickFeature(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnNotification:
			startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
			break;
		case R.id.btnFriendsStatus:
			startActivity(new Intent(getApplicationContext(), FriendsStatusActivity.class));
			break;
		case R.id.btnMyStatus:
			startActivity(new Intent(getApplicationContext(), MyStatusActivity.class));
			break;
		case R.id.btnProfileList:
			startActivity(new Intent(getApplicationContext(), ProfileListActivity.class));
			break;
		case R.id.btnManageFriends:
			startActivity(new Intent(getApplicationContext(), ManageFriendActivity.class));
			break;
		case R.id.btnSelectDataserver:
			startActivity(new Intent(getApplicationContext(), SelectDataserver.class));
			break;
		default:
			break;
		}
		Dashboard.this.finish();
	}

}