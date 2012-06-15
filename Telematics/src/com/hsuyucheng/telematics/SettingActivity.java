package com.hsuyucheng.telematics;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;

/** Setting for Record video
 * @author hsuyucheng
 * @version 0.1
 */
public class SettingActivity extends PreferenceActivity {
	private Button backButton;
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // set UI
        setContentView(R.layout.setting);
        // android 4.0 decprcation, but android 2.0 use
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
