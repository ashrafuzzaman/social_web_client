package org.kth.cos.android.sw;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.DataAuthenticationService;
import org.kth.cos.android.sw.network.DataHosts;
import org.kth.cos.android.sw.network.DataServerService;
import org.kth.cos.android.sw.network.SigninService;
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
		Response response = new SigninService().signIn(email, pass, SigninUserActivity.this);
		if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
			try {
				new DataServerService(email, UserAccount.getAccount(this).getAuthToken()).updateDataServiceHost(DataHosts.DATA_SERVER);
			} catch (Exception e) {
				e.printStackTrace();
			}
			switchToMainActivity();
		} else {
			showMessage(response.getMessage());
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
