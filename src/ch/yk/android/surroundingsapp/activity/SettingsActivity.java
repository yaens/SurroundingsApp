package ch.yk.android.surroundingsapp.activity;

import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import ch.yk.android.surroundingsapp.R;

public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		// Registers a listener whenever a key changes            
		SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		MainActivity.refreshMap = true;
	}
	

	

}