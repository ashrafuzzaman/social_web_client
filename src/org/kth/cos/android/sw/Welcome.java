package org.kth.cos.android.sw;

import org.kth.cos.android.sw.data.FriendManager;
import org.kth.cos.android.sw.data.ProfileManager;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.FriendService;
import org.kth.cos.android.sw.network.ProfileService;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Welcome extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("Welcome");
		syncFriendList();
		syncProfileList();
		generateView();
	}

	private void generateView() {
		UserAccount profile = UserAccount.getAccount(this);
		Log.i("INFO", String.format("Profile:: %s [%s]", profile.getEmail(), profile.getAuthToken()));
		makeInvisible(R.id.loggedInLayout);
		makeInvisible(R.id.loggedOutLayout);
		if (!profile.isSignedIn()) {
			makeVisible(R.id.loggedOutLayout);
			attachButton(R.id.btnRegister, RegisterUserActivity.class, true);
			attachButton(R.id.btnSignin, SigninUserActivity.class, true);
		} else {
			makeVisible(R.id.loggedInLayout);
			attachBtnClearCach();
			attachButton(R.id.btnMyStatus, MyStatusActivity.class, false);
			attachButton(R.id.btnProfileList, ProfileListActivity.class, false);
			attachButton(R.id.btnFindFriend, FindFriendActivity.class, false);
			attachButton(R.id.btnNotification, NotificationActivity.class, false);
			attachButton(R.id.btnPostStatus, PostStatus.class, false);
			attachButton(R.id.btnFriendsStatus, FriendsStatusActivity.class, false);
			attachButton(R.id.btnManageFriends, ManageFriendActivity.class, false);
		}

		attachBtnExit();
	}

	private void attachBtnExit() {
		Button btnExit = (Button) findViewById(R.id.btnExit);
		makeVisible(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Welcome.this.finish();
			}
		});
	}

	private void attachBtnClearCach() {
		Button btnClrCach = (Button) findViewById(R.id.btnClrCach);
		btnClrCach.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UserAccount profile = UserAccount.getAccount(Welcome.this);
				profile.clear(Welcome.this);
				profile = UserAccount.getAccount(Welcome.this);
				Log.i("INFO", String.format("Profile After clear:: %s [%s]", profile.getEmail(), profile.getAuthToken()));
				new FriendManager(Welcome.this).refrashTable();
				new ProfileManager(Welcome.this).refrashTable();
				generateView();
			}
		});
	}
	
	private void syncFriendList() {
		new Thread() {
			@Override
			public void run() {
				UserAccount account = UserAccount.getAccount(Welcome.this);
				FriendService friendService = new FriendService(account.getEmail(), account.getDataAuthToken());
				friendService.syncFriendList(Welcome.this);
			}
		}.start();
	}

	private void syncProfileList() {
		new Thread() {
			@Override
			public void run() {
				UserAccount account = UserAccount.getAccount(Welcome.this);
				ProfileService profileService = new ProfileService(account.getEmail(), account.getDataAuthToken());
				profileService.syncProfileList(Welcome.this);
			}
		}.start();
	}



}