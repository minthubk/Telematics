package com.hsuyucheng.telematics;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends PreferenceActivity {
	private Button backButton;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // set UI
        setContentView(R.layout.setting);
        this.addPreferencesFromResource(R.xml.preference);
        backButton = (Button)findViewById(R.id.settingButton);
        backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
    }

}
