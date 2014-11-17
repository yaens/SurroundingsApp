package ch.yk.android.surroundingsapp.businessobject;

import org.json.JSONException;
import org.json.JSONObject;

import ch.yk.android.surroundingsapp.R;

public class Footballplace extends Result{

	private String address;
	private String telNr;
	private String www;
	
	public Footballplace(){

	}

	public String getAddress() {
		return address;
	}

	private void setAddress(String address) {
		this.address = address;
	}
	
	public String getWww() {
		return www;
	}

	private void setWww(String www) {
		this.www = www;
	}
	
	public String getTelNr() {
		return telNr;
	}

	public void setTelNr(String telNr) {
		this.telNr = telNr;
	}
	
	@Override
	public void setData(JSONObject obj) throws JSONException{
		
		String lat = obj.getString("lat");
		String lon = obj.getString("lon");
		String name = obj.getString("Name");
		String adresse = obj.getString("Adresse");
		String telNr = obj.getString("Tel");
		String www = obj.getString("www");
		
		setName("Fussballplatz: " + name);
		this.setLat(Double.parseDouble(lat));
		this.setLon(Double.parseDouble(lon));
		this.setAddress(adresse);
		this.setTelNr(telNr);
		this.setWww(www);
	}
	
	@Override
	public int getIconName(){
		return R.drawable.icon_soccer;
	}

	@Override
	public String getDescription() {
		String description = this.getAddress() + " - Tel: " + telNr;
		return description;
	}
}