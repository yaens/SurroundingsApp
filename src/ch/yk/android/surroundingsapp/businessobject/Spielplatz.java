package ch.yk.android.surroundingsapp.businessobject;

import org.json.JSONException;
import org.json.JSONObject;

public class Spielplatz extends Result{
	
	private String www;
	private String address;
	
	public Spielplatz(){

	}
	
	public String getWww() {
		return www;
	}

	public String getAddress() {
		return address;
	}
	
	@Override
	public void setData(JSONObject obj) throws JSONException{
		
		String lat = obj.getString("lat");
		String lon = obj.getString("lon");
		String name = obj.getString("Objektbezeichung");
		
		this.setName("Spielplatz: " + name);
		this.setLat(Double.parseDouble(lat));
		this.setLon(Double.parseDouble(lon));
	}
	
	@Override
	public String getIconName(){
		return "Spielplatz.png";
	}

	@Override
	public String getDescription() {
		String description = " ";
		return description;
	}

}
