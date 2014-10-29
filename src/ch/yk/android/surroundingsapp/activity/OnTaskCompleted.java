package ch.yk.android.surroundingsapp.activity;

import java.util.ArrayList;

import org.json.JSONObject;

public interface OnTaskCompleted{
    void onTaskCompleted(ArrayList<JSONObject> resultList);
}