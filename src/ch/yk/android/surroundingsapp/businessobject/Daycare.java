package ch.yk.android.surroundingsapp.businessobject;

import org.json.JSONException;
import org.json.JSONObject;

import ch.yk.android.surroundingsapp.R;

public class Daycare extends Result{
	
	private String www;
	private String address;
	
	public Daycare(){

	}
	
	public String getWww() {
		return www;
	}

	private void setWww(String www) {
		this.www = www;
	}

	public String getAddress() {
		return address;
	}

	private void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public void setData(JSONObject obj) throws JSONException{
		
		String lat = obj.getString("lat");
		String lon = obj.getString("lon");
		String name = obj.getString("Name");
		String www = obj.getString("www");
		String adresse = obj.getString("Adresse");
		
		this.setName("Kinderhort: " + name);
		this.setLat(Double.parseDouble(lat));
		this.setLon(Double.parseDouble(lon));
		this.setWww(www);
		this.setAddress(adresse);
	}
	
	@Override
	public int getIconName(){
		return R.drawable.icon_daycare;
	}

	@Override
	public String getDescription() {
		String description = this.getAddress();
		return description;
	}
	

}
