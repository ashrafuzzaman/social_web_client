package org.kth.cos.android.sw;

import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.DataAuthenticationService;
import org.kth.cos.android.sw.network.UserAuthenticationService;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SigninUserActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);

		UserAccount profile = UserAccount.getAccount(this);
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

	public void signin() {
		final String email = ((TextView) findViewById(R.id.txtEmail)).getText().toString();
		final String pass = ((TextView) findViewById(R.id.txtPass)).getText().toString();
		Response responseStatus;
		try {
			responseStatus = new UserAuthenticationService().signin(email, pass);
			if (responseStatus.getStatus() == Status.STATUS_SUCCESS) {
				UserAccount profile = (UserAccount) responseStatus.getResponse();
				profile.save(SigninUserActivity.this);

				responseStatus = new DataAuthenticationService().signin(email, pass);
				if (responseStatus.getStatus() == Status.STATUS_SUCCESS) {
					profile = (UserAccount) responseStatus.getResponse();
					profile.update(SigninUserActivity.this);
					switchToMainActivity();
				} else {
					showMessage(responseStatus.getMessage());
				}
			} else {
				showMessage(responseStatus.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void attachSigninBtnEvent() {
		Button button = (Button) findViewById(R.id.btnSignin);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				signin();
			}
		});
	}
}
