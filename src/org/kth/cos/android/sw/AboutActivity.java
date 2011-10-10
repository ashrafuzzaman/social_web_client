package org.kth.cos.android.sw;

import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.DataAuthenticationService;
import org.kth.cos.android.sw.network.DataHosts;
import org.kth.cos.android.sw.network.DataServerService;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {
	UserAccount account;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_me);
		setTitle("About me");
	}

}