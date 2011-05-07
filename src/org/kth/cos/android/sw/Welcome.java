package org.kth.cos.android.sw;

import org.kth.cos.android.sw.data.Profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Welcome extends Activity {
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
			Toast.makeText(getBaseContext(), "Signed in with token : " + profile.getAuthToken(), Toast.LENGTH_SHORT).show();
		}

		attachBtnExit();
	}

	private void attachBtnExit() {
		Button btnExit = (Button) findViewById(R.id.btnExit);
		makeVisible(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Exiting", Toast.LENGTH_SHORT).show();
				Welcome.this.finish();
			}
		});
	}

	private void attachBtnClearCach(final Profile profile) {
		Button btnClrCach = (Button) findViewById(R.id.btnClrCach);
		makeVisible(R.id.btnClrCach);
		btnClrCach.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Clearing cach", Toast.LENGTH_SHORT).show();
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

	private void makeInvisible(int viewId) {
		findViewById(viewId).setVisibility(View.GONE);
	}

	private void makeVisible(int viewId) {
		findViewById(viewId).setVisibility(View.VISIBLE);
	}

}