package org.kth.cos.android.sw;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.DataAuthenticationService;
import org.kth.cos.android.sw.network.UserAuthenticationService;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
		String email = ((TextView) findViewById(R.id.txtEmail)).getText().toString();
		String pass = ((TextView) findViewById(R.id.txtPass)).getText().toString();
		try {
			Response responseStatus = new UserAuthenticationService().signin(email, pass);
			if (responseStatus.getStatus() == Status.STATUS_SUCCESS) {
				UserAccount profile = (UserAccount) responseStatus.getResponse();
				profile.save(SigninUserActivity.this);

				responseStatus = new DataAuthenticationService().signin(email, pass);
				if (responseStatus.getStatus() == Status.STATUS_SUCCESS) {
					profile = (UserAccount) responseStatus.getResponse();
					profile.update(SigninUserActivity.this);
				} else {
					showMessage(responseStatus.getMessage());
				}
			} else {
				showMessage(responseStatus.getMessage());
			}
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

	private void attachSigninBtnEvent() {
		Button button = (Button) findViewById(R.id.btnSignin);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SigninAsyncTask task = new SigninAsyncTask();
				task.applicationContext = SigninUserActivity.this;
				task.execute();
			}

		});
	}

	class SigninAsyncTask extends AsyncTask {
		private ProgressDialog dialog;
		protected SigninUserActivity applicationContext;

		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(applicationContext, "Wait", "Signing ...", true);
		}

		@Override
		protected Object doInBackground(Object... params) {
			applicationContext.signin();
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			this.dialog.cancel();
		}
	}
}
