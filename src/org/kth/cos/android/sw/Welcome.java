package org.kth.cos.android.sw;

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

		Button btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Welcome.this, RegisterUserActivity.class);
				Welcome.this.startActivity(myIntent);
				Welcome.this.finish();
			}
		});

		Button btnSignin = (Button) findViewById(R.id.btnSignin);
		btnSignin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Welcome.this, SigninUserActivity.class);
				Welcome.this.startActivity(myIntent);
				Welcome.this.finish();
			}
		});

		Button btnExit = (Button) findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Exiting", Toast.LENGTH_SHORT).show();
				Welcome.this.finish();
			}
		});

	}
}