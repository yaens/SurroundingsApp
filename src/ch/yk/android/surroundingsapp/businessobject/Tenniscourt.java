package ch.yk.android.surroundingsapp.businessobject;

import org.json.JSONException;
import org.json.JSONObject;

import ch.yk.android.surroundingsapp.R;

public class Tenniscourt extends Result{

	private String address;
	private String telNr;
	
	public Tenniscourt(){

	}

	public String getAddress() {
		return address;
	}

	private void setAddress(String address) {
		this.address = address;
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
		
		setName("Tennisplatz: " + name);
		this.setLat(Double.parseDouble(lat));
		this.setLon(Double.parseDouble(lon));
		this.setAddress(adresse);
		this.setTelNr(telNr);
	}
	
	@Override
	public int getIconName(){
		return R.drawable.icon_tenniscourt;
	}

	@Override
	public String getDescription() {
		String description = this.getAddress() + " - Tel: " + telNr;
		return description;
	}
}
