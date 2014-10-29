package ch.yk.android.surroundingsapp.RESTResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Musikschule extends Result {
	
	public Musikschule(){

	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setData(JSONObject obj) throws JSONException{
		
		String lat = obj.getString("lat");
		String lon = obj.getString("lon");
		String name = obj.getString("Name");
		
		this.setName(name);
		this.setLat(Double.parseDouble(lat));
		this.setLon(Double.parseDouble(lon));
		
	}
}
