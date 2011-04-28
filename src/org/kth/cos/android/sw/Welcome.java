package org.kth.cos.android.sw;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.register);

        Button button = (Button) findViewById(R.id.btnRegister);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String email = ((TextView) findViewById(R.id.txtEmail)).getText().toString();
            	String pass = ((TextView) findViewById(R.id.txtPass)).getText().toString();
            	String confirmPass = ((TextView) findViewById(R.id.txtConfimPass)).getText().toString();
            	if(!pass.equals(confirmPass)) {
            		Toast.makeText(getBaseContext(), "Password does not match [" + pass + "] with [" + confirmPass + "]", Toast.LENGTH_SHORT).show();
            	} else {
            		Toast.makeText(getBaseContext(), "Password matched", Toast.LENGTH_SHORT).show();
            	}
            }
        });
    }
}