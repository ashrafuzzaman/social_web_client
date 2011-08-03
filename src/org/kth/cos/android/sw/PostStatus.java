package org.kth.cos.android.sw;

import java.util.List;

import org.kth.cos.android.sw.data.Profile;
import org.kth.cos.android.sw.data.ProfileManager;
import org.kth.cos.android.sw.data.UserAccount;
import org.kth.cos.android.sw.network.StatusService;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PostStatus extends BaseActivity {
	/** Called when the activity is first created. */
	private List<Profile> profiles;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_status);
		setTitle("Post your status");
		profiles = new ProfileManager(PostStatus.this).fetchAllProfile();
		attachBtnPost();
	}

	private void attachBtnPost() {
		Button btnPost = (Button) findViewById(R.id.btnPost);
		btnPost.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String status = ((TextView) findViewById(R.id.txtStatus)).getText().toString();
				AlertDialog.Builder builder = new AlertDialog.Builder(PostStatus.this);
				builder.setTitle("Profile to share status");
				builder.setItems(ProfileManager.fetchAllProfileName(profiles), new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	UserAccount account = UserAccount.getAccount(PostStatus.this);
				    	try {
							new StatusService(account.getEmail(), account.getDataAuthToken()).postStatus(status, profiles.get(item).getProfileId());
							showMessage("Status posted");
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