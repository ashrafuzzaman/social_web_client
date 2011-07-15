package org.kth.cos.android.sw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

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

}