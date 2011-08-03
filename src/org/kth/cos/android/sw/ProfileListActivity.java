package org.kth.cos.android.sw;

import java.util.ArrayList;
import java.util.HashMap;

import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.ResponseStatus;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.ProfileService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ProfileListActivity extends ListActivity {
	ArrayList<HashMap<String, String>> profileList;
	final Handler mHandler = new Handler();
	Dialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Profile list");
		startLoadingList();
	}

	private void generateList() {
		UserAccount profile = UserAccount.getAccount(this);
		ProfileService profileService = new ProfileService(profile.getEmail(), profile.getDataAuthToken());
		try {
			Response response = profileService.getProfileList(ProfileListActivity.this);
			if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
				profileList = (ArrayList<HashMap<String, String>>) (response.getResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveNewProfile(String profileName) {
		ProfileService profileService = getProfileService();
		try {
			Response response = profileService.createProfile(profileName);
			if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
				startLoadingList();
			} else {
				showMessage(response.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateProfileListInUI() {
		SimpleAdapter mSchedule1 = new SimpleAdapter(this, profileList, R.layout.profile_rowlayout, new String[] { "name" }, new int[] { R.id.txtProfileName });
		this.setListAdapter(mSchedule1);
		registerForContextMenu(getListView());
		progressDialog.dismiss();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		HashMap<String, String> profile = profileList.get(position);
		Intent attributeActIntent = new Intent(ProfileListActivity.this, AttributeListActivity.class);
		attributeActIntent.putExtra("name", profile.get("name"));
		attributeActIntent.putExtra("id", profile.get("id"));
		ProfileListActivity.this.startActivity(attributeActIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.profile_menu, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Profile action");
		menu.add(0, v.getId(), 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		if (item.getTitle() == "Delete") {
			int profileId = Integer.parseInt(profileList.get(menuInfo.position).get("id"));
			ProfileService profileService = getProfileService();
			Response response;
			try {
				response = profileService.deleteProfile(profileId);
				if (response.getStatus() == ResponseStatus.STATUS_SUCCESS) {
					startLoadingList();
				}
				showMessage(response.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_profile:
			attachNewProfileMenuAction();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void attachNewProfileMenuAction() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("New Profile");
		alert.setMessage("Type the name of the new profile");
		final EditText input = new EditText(this);
		input.setSingleLine();
		alert.setView(input);

		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// progressDialog =
				// ProgressDialog.show(ProfileListActivity.this, "Wait",
				// "Creating new profile...", true);
				new Thread(new Runnable() {
					public void run() {
						Looper.prepare();
						String profileName = input.getText().toString();
						saveNewProfile(profileName);
						// progressDialog.dismiss();
						//startLoadingList();
						Looper.loop();
					}
				}).start();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	final Runnable resultUpdater = new Runnable() {
		public void run() {
			updateProfileListInUI();
		}
	};

	protected void startLoadingList() {
		progressDialog = ProgressDialog.show(ProfileListActivity.this, "Wait", "Loading profile list...", true);
		Thread t = new Thread() {
			public void run() {
				Looper.prepare();
				generateList();
				mHandler.post(resultUpdater);
				Looper.loop();
			}
		};
		t.start();
	}

	private ProfileService getProfileService() {
		UserAccount profile = UserAccount.getAccount(this);
		ProfileService profileService = new ProfileService(profile.getEmail(), profile.getDataAuthToken());
		return profileService;
	}

	protected void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

}