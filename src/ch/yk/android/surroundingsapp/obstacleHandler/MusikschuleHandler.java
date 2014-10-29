package ch.yk.android.surroundingsapp.obstacleHandler;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import ch.yk.android.surroundingsapp.RESTResult.Musikschule;
import ch.yk.android.surroundingsapp.activity.MapHandler;

public class MusikschuleHandler extends ResultHandler{

	public MusikschuleHandler(MapHandler mapHandler) {
		super(mapHandler);
		
	}

	@Override
	public void handleResult(ArrayList<JSONObject> resultList) {
		
		for(JSONObject elem:resultList){
			Musikschule ms = new Musikschule();
			
			try {
				ms.setData(elem);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.getMapHandler().addMarker(ms.getName(), ms.getLat(), ms.getLon());
		}
		
	}
}
