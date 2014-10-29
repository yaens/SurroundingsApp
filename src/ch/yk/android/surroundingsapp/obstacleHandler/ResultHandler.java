package ch.yk.android.surroundingsapp.obstacleHandler;

import java.util.ArrayList;

import org.json.JSONObject;

import ch.yk.android.surroundingsapp.activity.MapHandler;

public abstract class ResultHandler {
	
	private MapHandler mapHandler;

	public ResultHandler(MapHandler mapHandler){

		this.setMapHandler(mapHandler);	
	}
	
	public abstract void handleResult(ArrayList<JSONObject> resultList);

	public MapHandler getMapHandler() {
		return mapHandler;
	}

	public void setMapHandler(MapHandler mapHandler) {
		this.mapHandler = mapHandler;
	}

}
