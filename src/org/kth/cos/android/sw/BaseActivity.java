package org.kth.cos.android.sw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class BaseActivity extends Activity {

	protected void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	protected void makeInvisible(int viewId) {
		findViewById(viewId).setVisibility(View.GONE);
	}

	protected void makeVisible(int viewId) {
		findViewById(viewId).setVisibility(View.VISIBLE);
	}

	protected void attachButton(int btnID, final Class activityClass, final boolean closeThisActivity) {
		View button = (View) findViewById(btnID);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startNewActivity(activityClass, closeThisActivity);
			}

		});
	}

	public void startNewActivity(final Class activityClass, final boolean closeThisActivity) {
		Intent myIntent = new Intent(BaseActivity.this, activityClass);
		BaseActivity.this.startActivity(myIntent);
		if (closeThisActivity) {
			BaseActivity.this.finish();
		}
	}

	public void onClickHome(View v) {
		startNewActivity(Dashboard.class, true);
	}

	public void onClickSearch(View v) {
		startNewActivity(FindFriendActivity.class, true);
	}

	public void onClickAbout(View v) {
		startNewActivity(AboutActivity.class, true);
	}

}