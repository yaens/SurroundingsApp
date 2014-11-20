package ch.yk.android.surroundingsapp.support;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsHandler {
	
	private ArrayList<String> activeObstacleList;

	private SharedPreferences sharedPref;
	public SettingsHandler(Context settingsContext){
		
		activeObstacleList = new ArrayList<String>();
		
		this.sharedPref = PreferenceManager.getDefaultSharedPreferences(settingsContext);
	}

	public double getRadius() {
		String prefRadius = sharedPref.getString("pref_key_common_radius", "1000");
		return Double.parseDouble(prefRadius);
	}
	
	
	public void synchronizeActiveDataPrefs(){
		
		Map<String, ?> allEntries = this.sharedPref.getAll();
		
		for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
		    //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
			
			String key = entry.getKey();
			
			this.addRemovePreference(key);
		} 
		
	}
	
	private void addRemovePreference(String key){
		
		boolean isPreferenceOn = false;
		boolean isBool = false;
		
		try{
			isPreferenceOn = this.sharedPref.getBoolean(key,false);
			isBool = true;
		}catch(Exception e){
			//We just want the bool preferences here
		}
		
		if(isPreferenceOn){
			activeObstacleList.remove(key);
			activeObstacleList.add(key);
		}else if(isBool){
			activeObstacleList.remove(key);
		}
		
	}
	
	public ArrayList<String> getActiveObstacleList() {
		return activeObstacleList;
	}
}
