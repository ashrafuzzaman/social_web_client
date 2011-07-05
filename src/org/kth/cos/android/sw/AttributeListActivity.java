package org.kth.cos.android.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.kth.cos.android.sw.data.Response;
import org.kth.cos.android.sw.data.Status;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.AttributeService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AttributeListActivity extends ListActivity {
	ArrayList<HashMap<String, String>> attributeList;
	final Handler mHandler = new Handler();
	Dialog progressDialog;
	private int profileId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Attribute list");
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			profileId = Integer.parseInt(extras.getString("id"));
			startLoadingList();
		}
	}

	private void generateList() {
		AttributeService attributeService = getAttributeService();
		try {
			Response response = attributeService.getAttributeList(profileId);
			if (response.getStatus() == Status.STATUS_SUCCESS) {
				attributeList = (ArrayList<HashMap<String, String>>) (response.getResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AttributeService getAttributeService() {
		UserAccount account = UserAccount.getAccount(this);
		AttributeService attributeService = new AttributeService(account.getEmail(), account.getDataAuthToken());
		return attributeService;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.attribute_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_attributes:
			showMessage("save");
			AttributeService attributeService = getAttributeService();
			List<String> ids = new ArrayList<String>();
			for (HashMap<String, String> attr : attributeList) {
				if (attr.get("selected").equals("true")) {
					ids.add(attr.get("id"));
				}
				Log.i("Selected", attr.get("id") + attr.get("name") + " : " + attr.get("selected"));
			}
			try {
				attributeService.updateAttributeList(profileId, ids);
				startLoadingList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void updateAttributeListInUI() {
		this.setListAdapter(new AttributeListAdapter(this, attributeList));
		registerForContextMenu(getListView());
		this.progressDialog.dismiss();
	}

	final Runnable resultUpdater = new Runnable() {
		public void run() {
			updateAttributeListInUI();
		}
	};

	protected void startLoadingList() {
		this.progressDialog = ProgressDialog.show(AttributeListActivity.this, "Wait", "Loading attributes...", true);
		Thread t = new Thread() {
			public void run() {
				generateList();
				mHandler.post(resultUpdater);
			}
		};
		t.start();
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

	class AttributeListAdapter extends ArrayAdapter<HashMap<String, String>> {

		public AttributeListAdapter(Context context, List<HashMap<String, String>> objects) {
			super(context, R.id.txtAttributeName, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.attribute_rowlayout, null);
			}
			final HashMap<String, String> attributeMap = getItem(position);
			if (attributeMap != null) {
				TextView txtName = (TextView) v.findViewById(R.id.txtAttributeName);
				txtName.setText(attributeMap.get("name"));
				txtName.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						showMessage(attributeMap.get("value"));
					}
				});

				CheckBox chkSelect = (CheckBox) v.findViewById(R.id.chkChecked);
				chkSelect.setChecked(Boolean.parseBoolean(attributeMap.get("selected")));
				chkSelect.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						attributeMap.put("selected", attributeMap.get("selected").equals("true") ? "false" : "true");
					}
				});
			}
			return v;
		}

	}
}
