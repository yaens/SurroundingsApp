package ch.yk.android.surroundingsapp.obstacleHandler;

import java.util.ArrayList;

import org.json.JSONObject;

import ch.yk.android.surroundingsapp.map.MapHandler;

public abstract class ObstacleHandler {
	
	private MapHandler mapHandler;

	public ObstacleHandler(MapHandler mapHandler){

		this.setMapHandler(mapHandler);	
	}
	
	public MapHandler getMapHandler() {
		return mapHandler;
	}

	public void setMapHandler(MapHandler mapHandler) {
		this.mapHandler = mapHandler;
	}
	
	public abstract void handleResult(ArrayList<JSONObject> resultList);
	
	public abstract String getAPICall();

}
