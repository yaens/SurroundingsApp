package ch.yk.android.surroundingsapp.obstacleHandler;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import ch.yk.android.surroundingsapp.activity.MapHandler;
import ch.yk.android.surroundingsapp.businessobject.Result;

public class ConcreteObstacleHandler<T extends Result> extends ObstacleHandler{
	
	private String targetDataSet;
	private double lat;
	private double lon;
	private double range;

	private Class<T> mClass;
	
	public ConcreteObstacleHandler(MapHandler mapHandler,Class<T> cls) {
		super(mapHandler);
		this.mClass = cls;
		
	}
	
	public void setQuery(String targetDataSet, double lat, double lon, double range){
		this.lat = lat;
		this.lon = lon;
		this.range = range;
		this.targetDataSet = targetDataSet;
	}

	@Override
	public void handleResult(ArrayList<JSONObject> resultList) {
		
		for(JSONObject elem:resultList){
			
			T resultObject = null;
			
	        try{
	            resultObject = mClass.newInstance();
	        }catch(Exception e){
	            e.printStackTrace();
	        }
			
			try {
				resultObject.setData(elem);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.getMapHandler().addMarker(resultObject.getName(), resultObject.getLat(), resultObject.getLon(),resultObject.getIconName());
		}
		
	}

	@Override
	public String getAPICall() {
		String query = "[e|e<~"+ this.targetDataSet +",dist(e.lat,e.lon," + this.lat + "," + this.lon + ")<" + this.range+"]";
		return query;
	}
}
