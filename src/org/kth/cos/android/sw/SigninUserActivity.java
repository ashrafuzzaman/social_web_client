package org.kth.cos.android.sw;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Profile;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.network.UserAuthenticationService;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SigninUserActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);

		Profile profile = Profile.getProfile(this);
		if (profile.isSignedIn()) {
			switchToMainActivity();
		} else {
			String email = profile.getEmail();
			if (TextUtils.isEmpty(email))
				email = new AccountHelper().getDefaultEmailName(this);
			((TextView) findViewById(R.id.txtEmail)).setText(email);
			attachSigninBtnEvent();
			attachBtnCancel();
		}
	}

	private void attachBtnCancel() {
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switchToMainActivity();
			}
		});
	}

	private void switchToMainActivity() {
		Intent myIntent = new Intent(SigninUserActivity.this, Welcome.class);
		SigninUserActivity.this.startActivity(myIntent);
		SigninUserActivity.this.finish();
	}

	private void attachSigninBtnEvent() {
		Button button = (Button) findViewById(R.id.btnSignin);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String email = ((TextView) findViewById(R.id.txtEmail)).getText().toString();
				String pass = ((TextView) findViewById(R.id.txtPass)).getText().toString();
				try {
					Response responseStatus = new UserAuthenticationService().signin(email, pass);
					Profile profile = (Profile) responseStatus.getResponse();
					profile.save(SigninUserActivity.this);
					switchToMainActivity();
				} catch (ClientProtocolException e) {
					showMessage("ClientProtocolException " + e.getStackTrace().toString());
					e.printStackTrace();
				} catch (IOException e) {
					showMessage("IOException " + e.getStackTrace().toString());
					e.printStackTrace();
				} catch (JSONException e) {
					showMessage("JSONException " + e.getStackTrace().toString());
					e.printStackTrace();
				}
			}
		});
	}

}
