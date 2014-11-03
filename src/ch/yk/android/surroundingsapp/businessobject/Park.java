package ch.yk.android.surroundingsapp.businessobject;

import org.json.JSONException;
import org.json.JSONObject;

public class Park extends Result{
	
	private String www;
	private String address;
	
	public Park(){

	}
	
	public String getWww() {
		return www;
	}

	private void setWww(String www) {
		this.www = www;
	}

	
	@Override
	public void setData(JSONObject obj) throws JSONException{
		
		String lat = obj.getString("lat");
		String lon = obj.getString("lon");
		String name = obj.getString("Name");
		String www = obj.getString("www");
		
		this.setName("Park: " + name);
		this.setLat(Double.parseDouble(lat));
		this.setLon(Double.parseDouble(lon));
		this.setWww(www);
	}
	
	@Override
	public String getIconName(){
		return "Park.png";
	}

	@Override
	public String getDescription() {
		String description = " ";
		return description;
	}

}
