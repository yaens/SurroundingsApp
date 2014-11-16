package ch.yk.android.surroundingsapp.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsHandler {
	
	private SharedPreferences sharedPref;
	public SettingsHandler(Context settingsContext){
		this.sharedPref = PreferenceManager.getDefaultSharedPreferences(settingsContext);
	}

	public double getRadius() {
		String prefRadius = sharedPref.getString("pref_key_common_radius", "1000");
		return Double.parseDouble(prefRadius);
	}

	public boolean isIncludeSchools() {
		return sharedPref.getBoolean("pref_key_importdata_category_family_schools", false);
	}

	public boolean isIncludeParks() {
		return sharedPref.getBoolean("pref_key_importdata_category_nature_parks", false);
	}

	public boolean isIncludeMusicschool() {
		return sharedPref.getBoolean("pref_key_importdata_category_family_musicschool", false);
	}

	public boolean isIncludeKindergarden() {
		return sharedPref.getBoolean("pref_key_importdata_category_family_kindergarden", false);
	}

	public boolean isIncludeRecycling() {
		return sharedPref.getBoolean("pref_key_importdata_category_common_recycling", false);
	}

	public boolean isIncludePlayground() {
		return sharedPref.getBoolean("pref_key_importdata_category_family_playground", false);
	}
	
	public boolean isIncludeTenniscourt(){
		return sharedPref.getBoolean("pref_key_importdata_category_sports_tenniscourts", false);
	}
	
	public boolean isIncludeSwimmingpool(){
		return sharedPref.getBoolean("pref_key_importdata_category_sports_swimmingpools", false);
	}
}
