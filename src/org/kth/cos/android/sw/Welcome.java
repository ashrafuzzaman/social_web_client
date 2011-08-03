package org.kth.cos.android.sw;

import java.util.Map;

import org.kth.cos.android.sw.data.FriendManager;
import org.kth.cos.android.sw.data.UserAccount;

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
		generateView();
	}

	private void generateView() {
		UserAccount profile = UserAccount.getAccount(this);
		Log.i("INFO", String.format("Profile:: %s [%s]", profile.getEmail(), profile.getAuthToken()));
		makeInvisible(R.id.btnClrCach);
		makeInvisible(R.id.btnFrndDataStore);
		if (!profile.isSignedIn()) {
			attachButton(R.id.btnRegister, RegisterUserActivity.class, true);
			attachButton(R.id.btnSignin, SigninUserActivity.class, true);
			makeInvisible(R.id.btnProfileList);
		} else {
			makeInvisible(R.id.btnRegister);
			makeInvisible(R.id.btnSignin);
			//attachBtnClearCach();
			attachButton(R.id.btnMyStatus, MyStatusActivity.class, false);
			attachButton(R.id.btnProfileList, ProfileListActivity.class, false);
			attachButton(R.id.btnFindFriend, FindFriendActivity.class, false);
			attachButton(R.id.btnNotification, NotificationActivity.class, false);
			attachButton(R.id.btnPostStatus, PostStatus.class, false);
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
		makeVisible(R.id.btnClrCach);
		btnClrCach.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UserAccount profile = UserAccount.getAccount(Welcome.this);
				profile.clear(Welcome.this);
				profile = UserAccount.getAccount(Welcome.this);
				Log.i("INFO", String.format("Profile After clear:: %s [%s]", profile.getEmail(), profile.getAuthToken()));
				generateView();
			}
		});
	}

}