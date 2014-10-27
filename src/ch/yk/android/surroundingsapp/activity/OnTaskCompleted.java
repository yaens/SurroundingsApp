package ch.yk.android.surroundingsapp.activity;

import java.util.ArrayList;

import ch.yk.android.surroundingsapp.RESTResult.Musikschule;

public interface OnTaskCompleted{
    void onTaskCompleted(ArrayList<Musikschule> musikschuleResult);
}