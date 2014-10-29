package ch.yk.android.surroundingsapp.RESTResult;

import java.util.ArrayList;

import org.json.JSONObject;

public class GenericResultList<T extends Result> {
	
	ArrayList<T> ResultList = new ArrayList<T>();

	public void addResult(T result){
		
		ResultList.add(result);
	}
	
	public ArrayList<T> getResultList() {
		return ResultList;
	}

}
