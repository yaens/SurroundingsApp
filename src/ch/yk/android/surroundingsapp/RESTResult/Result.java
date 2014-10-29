package ch.yk.android.surroundingsapp.RESTResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Result {
	
	private double lat;
	private double lon;
	
	public Result(){
		
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public void setData(JSONObject obj) throws JSONException{
		
	}
}
