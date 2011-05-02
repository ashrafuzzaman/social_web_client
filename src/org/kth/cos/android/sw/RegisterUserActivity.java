package org.kth.cos.android.sw;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.network.UserAuthenticationService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterUserActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register);

		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(RegisterUserActivity.this, Welcome.class);
				RegisterUserActivity.this.startActivity(myIntent);
			}
		});

		Button button = (Button) findViewById(R.id.btnRegister);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String email = ((TextView) findViewById(R.id.txtEmail)).getText().toString();
				String pass = ((TextView) findViewById(R.id.txtPass)).getText().toString();
				String confirmPass = ((TextView) findViewById(R.id.txtConfimPass)).getText().toString();
				if (!pass.equals(confirmPass)) {
					Toast.makeText(getBaseContext(), "Password does not match [" + pass + "] with [" + confirmPass + "]", Toast.LENGTH_SHORT).show();
				} else {
					try {
						ResponseStatus responseStatus = new UserAuthenticationService().register(email, pass);
						Toast.makeText(getBaseContext(), responseStatus.getMessage(), Toast.LENGTH_SHORT).show();
					} catch (ClientProtocolException e) {
						Toast.makeText(getBaseContext(), "ClientProtocolException " + e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					} catch (IOException e) {
						Toast.makeText(getBaseContext(), "IOException " + e.toString(), Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					} catch (JSONException e) {

						Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
			}
		});
	}

}
