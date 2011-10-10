package org.kth.cos.android.sw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setTitle("Login");
		generateView();
	}

	private void generateView() {
		attachButton(R.id.btnRegister, RegisterUserActivity.class, true);
		attachButton(R.id.btnSignin, SigninUserActivity.class, true);
		attachBtnExit();
	}

	private void attachBtnExit() {
		Button btnExit = (Button) findViewById(R.id.btnExit);
		makeVisible(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});
	}
}