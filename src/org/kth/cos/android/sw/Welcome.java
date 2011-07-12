package org.kth.cos.android.sw;

import java.util.ArrayList;
import java.util.List;

import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.DataServerService;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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
			attachBtnRegister();
			attachBtnSignin();
			makeInvisible(R.id.btnProfileList);
		} else {
			attachBtnClearCach();
			attachBtnProfileList();
			// attachBtnFriendsDatastore(profile);
			makeInvisible(R.id.btnRegister);
			makeInvisible(R.id.btnSignin);
			// showMessage("Signed in with token : " + profile.getAuthToken());
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
				// showMessage("Clearing cach");
				UserAccount profile = UserAccount.getAccount(Welcome.this);
				profile.clear(Welcome.this);
				profile = UserAccount.getAccount(Welcome.this);
				Log.i("INFO", String.format("Profile After clear:: %s [%s]", profile.getEmail(), profile.getAuthToken()));
				generateView();
			}
		});
	}

	private void attachBtnSignin() {
		Button btnSignin = (Button) findViewById(R.id.btnSignin);
		makeVisible(R.id.btnSignin);
		btnSignin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Welcome.this, SigninUserActivity.class);
				Welcome.this.startActivity(myIntent);
				Welcome.this.finish();
			}
		});
	}

	private void attachBtnRegister() {
		Button btnRegister = (Button) findViewById(R.id.btnRegister);
		makeVisible(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Welcome.this, RegisterUserActivity.class);
				Welcome.this.startActivity(myIntent);
				Welcome.this.finish();
			}
		});
	}

	private void attachBtnProfileList() {
		Button btnProfileList = (Button) findViewById(R.id.btnProfileList);
		makeVisible(R.id.btnProfileList);
		btnProfileList.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Welcome.this, ProfileListActivity.class);
				Welcome.this.startActivity(myIntent);
				// Welcome.this.finish();
			}
		});
	}

	private void attachBtnFriendsDatastore(final UserAccount profile) {
		Button btnFrndDataStore = (Button) findViewById(R.id.btnFrndDataStore);
		makeVisible(R.id.btnExit);

		final List<String> emails = new ArrayList<String>();
		// TODO: Need to change this
		emails.add("ashrafuzzaman.g2@gmail.com");
		emails.add("test@test.com");
		btnFrndDataStore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					Response response = new DataServerService(profile.getEmail(), profile.getAuthToken()).getDataServiceHost(emails);
					StringBuffer resposeStr = new StringBuffer();
					@SuppressWarnings("unchecked")
					List<UserAccount> profiles = (List<UserAccount>) response.getResponse();
					for (UserAccount profileObj : profiles) {
						Log.i("FrndList", profileObj.getEmail());
						resposeStr.append(profileObj.getEmail() + " " + profileObj.getDataStoreServer() + "\n");
					}
					showMessage(resposeStr.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("FrindList", e.getMessage(), e);
				}
			}
		});
	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();

	}

}