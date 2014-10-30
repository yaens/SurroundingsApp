package ch.yk.android.surroundingsapp.businessobject;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class Musikschule extends Result {
	
	public Musikschule(){

	}
	
	private String name;
	private double lon;
	private double lat;
	
	private void setLon(double lon) {
		this.lon = lon;
		
	}

	private void setLat(double lat) {
		this.lat = lat;
		
	}

	private void setName(String name) {
		this.name = name;
		
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public double getLat() {
		return this.lat;
	}

	@Override
	public double getLon() {
		return this.lon;
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
	
	public String getIconName(){
		
		return "Musikschule.png";
	}
}
