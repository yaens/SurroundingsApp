package ch.yk.android.surroundingsapp.businessobject;

import org.json.JSONException;
import org.json.JSONObject;

public class Result {
	
	private String name;
	private double lon;
	private double lat;
	
	protected void setLon(double lon) {
		this.lon = lon;
	}

	protected void setLat(double lat) {
		this.lat = lat;
	}

	protected void setName(String name) {
		this.name = name;	
	}
	
	public double getLat() {
		return this.lat;
	}

	public double getLon() {
		return this.lon;
	}
	
	public String getName() {
		return this.name;
	}

	public void setData(JSONObject obj) throws JSONException{
		
		String lat = obj.getString("lat");
		String lon = obj.getString("lon");
		String name = obj.getString("Name");
		
		this.setName(name);
		this.setLat(Double.parseDouble(lat));
		this.setLon(Double.parseDouble(lon));
	}
	
	public String getIconName(){
		
		return "Unknown.png";
	}

	public String getDescription() {
		return "Override this in subclass";
	}
}
