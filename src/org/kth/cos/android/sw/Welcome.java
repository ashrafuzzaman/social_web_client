package org.kth.cos.android.sw;

import org.kth.cos.android.sw.data.Profile;

import android.content.Intent;
import android.os.Bundle;
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
		} else {
			attachBtnClearCach(profile);
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

}