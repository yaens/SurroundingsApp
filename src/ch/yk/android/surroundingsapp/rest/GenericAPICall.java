package ch.yk.android.surroundingsapp.rest;

import java.util.ArrayList;

import org.json.JSONObject;

import ch.yk.android.surroundingsapp.activity.OnTaskCompleted;
import ch.yk.android.surroundingsapp.obstacleHandler.ObstacleHandler;

public class GenericAPICall implements OnTaskCompleted{
	
	private ObstacleHandler resultHandler;
	
	public GenericAPICall(ObstacleHandler rHandler){
		this.resultHandler = rHandler;
	}
	
	public void executeAPICall(){
		new CallAPI(this).execute(resultHandler.getAPICall());
	}

	@Override
	public void onTaskCompleted(ArrayList<JSONObject> resultList) {
		this.resultHandler.handleResult(resultList);
	}

}
