package org.kth.cos.android.sw;

import java.util.List;

import org.kth.cos.android.sw.data.Profile;
import org.kth.cos.android.sw.data.ProfileManager;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.Status;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.StatusService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyStatusActivity extends BaseActivity {
	List<Status> statusList;
	private List<Profile> profiles;
	private ListView lstView;

	final Handler mHandler = new Handler();
	Dialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_list);
		lstView = (ListView) findViewById(R.id.lstView);
		setTitle("My Status");
		profiles = new ProfileManager(MyStatusActivity.this).fetchAllProfile();
		startLoadingList();
	}

	private void generateList() {
		StatusService friendService = getStatusService();
		try {
			Response response = friendService.getMyStatusList();
			if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
				statusList = (List<Status>) (response.getResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private StatusService getStatusService() {
		UserAccount account = UserAccount.getAccount(this);
		StatusService statusService = new StatusService(account.getEmail(), account.getDataAuthToken(), account.getDataStoreServer());
		return statusService;
	}

	private void updateStatusListInUI() {
		lstView.setAdapter(new StatusListAdapter(this, statusList));
		registerForContextMenu(lstView);
		this.progressDialog.dismiss();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.my_status_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_status:
			promptStatus();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void promptStatus() {
		final Dialog dialog = new Dialog(MyStatusActivity.this);
		dialog.setContentView(R.layout.post_status);
		((Button) dialog.findViewById(R.id.btnCancel)).setOnClickListener((new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		}));
		((Button) dialog.findViewById(R.id.btnPost)).setOnClickListener((new View.OnClickListener() {
			public void onClick(View v) {
				final String status = ((TextView) dialog.findViewById(R.id.txtStatus)).getText().toString();
				AlertDialog.Builder builder = new AlertDialog.Builder(MyStatusActivity.this);
				builder.setTitle("Profile to share status");
				builder.setItems(ProfileManager.fetchAllProfileName(profiles), new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	UserAccount account = UserAccount.getAccount(MyStatusActivity.this);
				    	try {
							new StatusService(account.getEmail(), account.getDataAuthToken(), account.getDataStoreServer()).postStatus(status, profiles.get(item).getProfileId());
							startLoadingList();
						} catch (Exception e) {
							e.printStackTrace();
						}
				    }
				});
				builder.create().show();
				dialog.dismiss();
			}
		}));

		dialog.show();
	}


	
	
	final Runnable resultUpdater = new Runnable() {
		public void run() {
			updateStatusListInUI();
		}
	};

	protected void startLoadingList() {
		this.progressDialog = ProgressDialog.show(MyStatusActivity.this, "Wait", "Loading Status...", true);
		Thread t = new Thread() {
			public void run() {
				generateList();
				mHandler.post(resultUpdater);
			}
		};
		t.start();
	}

	class StatusListAdapter extends ArrayAdapter<Status> {

		public StatusListAdapter(Context context, List<Status> objects) {
			super(context, R.id.txtStatus, objects);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.status_rowlayout, null);
			}
			final Status status = statusList.get(position);
			((TextView) v.findViewById(R.id.txtStatus)).setText(status.getValue());
			((TextView) v.findViewById(R.id.txtPostedAt)).setText(status.getPostedAtStr());
			((TextView) v.findViewById(R.id.txtPostedBy)).setText(status.getPostedBy());
			v.findViewById(R.id.statusContent).setOnClickListener((new View.OnClickListener() {
				public void onClick(View v) {
					Intent myIntent = new Intent(MyStatusActivity.this, StatusDetails.class);
					myIntent.putExtra("status", status);
					MyStatusActivity.this.startActivity(myIntent);
				}
			}));
			return v;
		}
	}

}
