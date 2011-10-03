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

public class SelectDataserver extends BaseActivity {
	UserAccount account;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_data_server);
		setTitle("Select Data server");
		account = UserAccount.getAccount(SelectDataserver.this);
		((TextView) findViewById(R.id.txtDataServer)).setText("Current data server :: " + account.getDataStoreServer());
		attachBtnselect();
	}

	private void attachBtnselect() {
		Button btnPost = (Button) findViewById(R.id.btnSelect);
		btnPost.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(SelectDataserver.this);
				builder.setTitle("Dataservers");
				
				final String[] dataServers = DataHosts.DATA_SERVERS; 
				builder.setItems(dataServers, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						final String dataServer = dataServers[item];
						((TextView) findViewById(R.id.txtDataServer)).setText(dataServer);
						try {
							final Response responseStatus = new DataServerService(account.getEmail(), account.getAuthToken()).updateDataServiceHost(dataServer);
							if (responseStatus.isOk()) {
								account.setDataStoreServer(dataServer);
								account.save(SelectDataserver.this);
								
								//try creating a user in that server
								final Response dataAuthResponseStatus = new DataAuthenticationService(dataServer).register(account.getEmail(), account.getPassword());
							}
							startNewActivity(Welcome.class, true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				builder.create().show();
			}
		});
	}
}