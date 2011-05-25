package org.kth.cos.android.sw;

import java.util.ArrayList;
import java.util.List;

import org.kth.cos.android.sw.activities.BaseActivity;
import org.kth.cos.android.sw.activities.RegisterUserActivity;
import org.kth.cos.android.sw.activities.SigninUserActivity;
import org.kth.cos.android.sw.data.Profile;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.network.DataServerService;

import android.content.Intent;
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
		generateView();
	}

	private void generateView() {
		Profile profile = Profile.getProfile(this);
		if (!profile.isSignedIn()) {
			attachBtnRegister();
			attachBtnSignin();
			makeInvisible(R.id.btnClrCach);
			makeInvisible(R.id.btnFrndDataStore);
		} else {
			attachBtnClearCach(profile);
			//attachBtnFriendsDatastore(profile);
			makeInvisible(R.id.btnRegister);
			makeInvisible(R.id.btnSignin);
			showMessage("Signed in with token : " + profile.getAuthToken());
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

	private void attachBtnClearCach(final Profile profile) {
		Button btnClrCach = (Button) findViewById(R.id.btnClrCach);
		makeVisible(R.id.btnClrCach);
		btnClrCach.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showMessage("Clearing cach");
				profile.clearProfile(Welcome.this);
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

	private void attachBtnFriendsDatastore(final Profile profile) {
		Button btnFrndDataStore = (Button) findViewById(R.id.btnFrndDataStore);
		makeVisible(R.id.btnExit);

		final List<String> emails = new ArrayList<String>();
		emails.add("ashrafuzzaman.g2@gmail.com");
		emails.add("test@test.com");
		btnFrndDataStore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					Response response = new DataServerService().getDataServiceHost(profile.getEmail(), profile.getAuthToken(), emails);
					StringBuffer resposeStr = new StringBuffer();
					List<Profile> profiles = (List<Profile>) response.getResponse();
					for (Profile profileObj : profiles) {
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

}