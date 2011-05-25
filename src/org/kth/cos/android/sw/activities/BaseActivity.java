package org.kth.cos.android.sw.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

public class BaseActivity extends Activity {

	protected void showMessage(String message) {
		// Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		AlertDialog alert = builder.create();
		alert.show();
	}

	protected void makeInvisible(int viewId) {
		findViewById(viewId).setVisibility(View.GONE);
	}

	protected void makeVisible(int viewId) {
		findViewById(viewId).setVisibility(View.VISIBLE);
	}

}