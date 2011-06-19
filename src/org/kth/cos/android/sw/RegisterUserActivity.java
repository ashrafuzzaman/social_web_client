package org.kth.cos.android.sw;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.DataAuthenticationService;
import org.kth.cos.android.sw.network.UserAuthenticationService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterUserActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.register);
			loadEmail();
			attachBtnCancel();
			attachBtnRegister();
		} catch (Exception e) {
			Log.d("Error", "", e);
		}
	}

	private void loadEmail() {
		((TextView) findViewById(R.id.txtEmail)).setText(new AccountHelper().getDefaultEmailName(this));
	}

	private void attachBtnCancel() {
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(RegisterUserActivity.this, Welcome.class);
				RegisterUserActivity.this.startActivity(myIntent);
				RegisterUserActivity.this.finish();
			}
		});
	}

	private void attachBtnRegister() {
		Button button = (Button) findViewById(R.id.btnRegister);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String email = ((TextView) findViewById(R.id.txtEmail)).getText().toString();
				String pass = ((TextView) findViewById(R.id.txtPass)).getText().toString();
				String confirmPass = ((TextView) findViewById(R.id.txtConfimPass)).getText().toString();
				if (!pass.equals(confirmPass)) {
					showMessage("Password does not match [" + pass + "] with [" + confirmPass + "]");
					return;
				} else {
					new RegisterAsyncTask(RegisterUserActivity.this, email, pass).execute();
					// register(email, pass);
				}
			}

		});
	}

	public void register(String email, String pass) {
		try {
			Response responseStatus = new UserAuthenticationService().register(email, pass);
			if (responseStatus.getStatus() == Status.STATUS_SUCCESS) {
				UserAccount profile = new UserAccount(email, pass);
				profile.save(RegisterUserActivity.this);
				responseStatus = new DataAuthenticationService().register(email, pass);
				switchToSigninActivity();
			} else {
				Toast.makeText(getBaseContext(), responseStatus.getMessage(), Toast.LENGTH_LONG).show();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("Register", e.getMessage(), e);
		}
	}

	private void switchToSigninActivity() {
		Intent myIntent = new Intent(RegisterUserActivity.this, SigninUserActivity.class);
		RegisterUserActivity.this.startActivity(myIntent);
		RegisterUserActivity.this.finish();
	}

	class RegisterAsyncTask extends AsyncTask {
		private ProgressDialog dialog;
		protected RegisterUserActivity applicationContext;

		String email;
		String pass;

		public RegisterAsyncTask(RegisterUserActivity applicationContext, String email, String pass) {
			super();
			this.applicationContext = applicationContext;
			this.email = email;
			this.pass = pass;
		}

		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(applicationContext, "Wait", "Registering ...", true);
		}

		@Override
		protected Object doInBackground(Object... params) {
			applicationContext.register(email, pass);
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			this.dialog.cancel();
		}
	}
}
