package org.kth.cos.android.sw;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.ProfileService;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;

public class ProfileListActivity extends ListActivity {
	private List<HashMap<String, String>> profileList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		generateProfileList();
	}

	protected void generateProfileList() {
		ProfileService profileService = getProfileService();
		try {
			Response response = profileService.getProfileList();
			if (response.getStatus() == Status.STATUS_SUCCESS) {
				profileList = (List<HashMap<String, String>>) (response.getResponse());
				//this.setListAdapter(new ArrayAdapter<String>(this, R.layout.profile_rowlayout, R.id.label, profileList));

				SimpleAdapter mSchedule = new SimpleAdapter(this, profileList, R.layout.profile_rowlayout, 
						new String[] { "name" }, new int[] { R.id.txtProfileName});
				this.setListAdapter(mSchedule);
				
				registerForContextMenu(getListView());
			} else {
				showMessage(response.getMessage());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected ProfileService getProfileService() {
		UserAccount profile = UserAccount.getAccount(this);
		ProfileService profileService = new ProfileService(profile.getEmail(), profile.getDataAuthToken());
		return profileService;
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
				if (response.getStatus() == Status.STATUS_SUCCESS) {
					generateProfileList();
				}
				showMessage(response.getMessage());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			showMessage("Other");
			return false;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
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

		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String prfileName = input.getText().toString();
				showMessage(prfileName);
				ProfileService profileService = getProfileService();
				try {
					Response response = profileService.createProfile(prfileName);
					if (response.getStatus() == Status.STATUS_SUCCESS) {
						generateProfileList();
					}
					showMessage(response.getMessage());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();
	}

	protected void showMessage(String message) {
		// Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		AlertDialog alert = builder.create();
		alert.show();
	}

}