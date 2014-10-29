package ch.yk.android.surroundingsapp.rest;

import java.util.ArrayList;

import org.json.JSONObject;

import ch.yk.android.surroundingsapp.RESTResult.Result;
import ch.yk.android.surroundingsapp.activity.MapHandler;
import ch.yk.android.surroundingsapp.activity.OnTaskCompleted;
import ch.yk.android.surroundingsapp.obstacleHandler.ResultHandler;

public class GenericAPICall implements OnTaskCompleted{
	
	private ResultHandler resultHandler;
	
	public GenericAPICall(ResultHandler rHandler){
		this.resultHandler = rHandler;
	}
	
	public void executeAPICall(){
		new CallAPI(this).execute();
	}

	@Override
	public void onTaskCompleted(ArrayList<JSONObject> resultList) {
		this.resultHandler.handleResult(resultList);
	}

}
