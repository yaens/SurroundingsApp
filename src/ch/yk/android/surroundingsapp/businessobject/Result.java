package ch.yk.android.surroundingsapp.businessobject;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Result {
	
	public abstract double getLat();

	public abstract double getLon();
	
	public abstract void setData(JSONObject obj) throws JSONException;

	public abstract String getName();
	
	public abstract String getIconName();
}
