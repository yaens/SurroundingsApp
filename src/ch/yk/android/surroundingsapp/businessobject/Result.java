package ch.yk.android.surroundingsapp.businessobject;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class Result {
	
	public abstract double getLat();

	public abstract double getLon();
	
	public abstract void setData(JSONObject obj) throws JSONException;

	public abstract String getName();
	
	public abstract String getIconName();
}
