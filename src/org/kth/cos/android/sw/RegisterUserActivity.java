package org.kth.cos.android.sw;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.DataAuthenticationService;
import org.kth.cos.android.sw.network.DataHosts;
import org.kth.cos.android.sw.network.UserAuthenticationService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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
		String email = new AccountHelper().getDefaultEmailName(this);
		if (!TextUtils.isEmpty(email)) {
			((TextView) findViewById(R.id.txtEmail)).setText(email);
		}
	}

	private void attachBtnCancel() {
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(RegisterUserActivity.this, Dashboard.class);
				RegisterUserActivity.this.startActivity(myIntent);
				RegisterUserActivity.this.finish();
			}
		});
	}

	private void attachBtnRegister() {
		Button button = (Button) findViewById(R.id.btnRegister);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String email = ((TextView) findViewById(R.id.txtEmail)).getText().toString();
				final String pass = ((TextView) findViewById(R.id.txtPass)).getText().toString();
				String confirmPass = ((TextView) findViewById(R.id.txtConfimPass)).getText().toString();
				if (!pass.equals(confirmPass)) {
					showMessage("Password does not match with confirm password");
					return;
				} else {
					// fetching data host
					AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
					builder.setTitle("Dataservers");
					final String[] dataServers = DataHosts.DATA_SERVERS;
					builder.setItems(dataServers, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							final String dataServer = dataServers[item];
							//new RegisterAsyncTask(RegisterUserActivity.this, email, pass, dataServer).execute();
							register(email, pass, dataServer);
							dialog.dismiss();
						}
					});
					builder.create().show();
				}
			}

		});
	}

	public void register(final String email, final String pass, final String dataServer) {
		try {
			final Response responseStatus = new UserAuthenticationService().register(email, pass, dataServer);
			if (responseStatus.getStatus() == ResponseStatus.STATUS_SUCCESS) {
				UserAccount account = new UserAccount(email, pass);
				account.setDataStoreServer(dataServer);
				account.save(RegisterUserActivity.this);
				Log.i("dataserver", dataServer);
				Response dataAuthResponseStatus = new DataAuthenticationService(dataServer).register(email, pass);
				if (responseStatus.getStatus() == ResponseStatus.STATUS_SUCCESS) {

				}
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
		String dataServer;

		public RegisterAsyncTask(RegisterUserActivity applicationContext, String email, String pass, String dataServer) {
			super();
			this.applicationContext = applicationContext;
			this.email = email;
			this.pass = pass;
			this.dataServer = dataServer;
		}

		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(applicationContext, "Wait", "Registering ...", true);
		}

		@Override
		protected Object doInBackground(Object... params) {
			applicationContext.register(email, pass, dataServer);
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			this.dialog.cancel();
		}
	}
}
