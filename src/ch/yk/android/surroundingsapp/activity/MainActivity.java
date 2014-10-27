package ch.yk.android.surroundingsapp.activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import ch.yk.android.surroundingsapp.R;
import ch.yk.android.surroundingsapp.RESTResult.Musikschule;
import ch.yk.android.surroundingsapp.rest.CallAPI;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnTaskCompleted {

	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		EditText editText = (EditText) findViewById(R.id.input_address);
		
		
		
		editText.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		        	
		        	//Hide Keyboard after the ok button click
		        	v.clearFocus();
		        	EditText myEditText = (EditText) findViewById(R.id.input_address);  
		        	InputMethodManager imm = (InputMethodManager)getSystemService(
		        	      Context.INPUT_METHOD_SERVICE);
		        	imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		        	
		            handled = true;
		            
		            setUpMap();
		        }
		        return handled;
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				//setUpMap();
			}
		}
	}

	private void setUpMap() {

		new CallAPI(this).execute();

	}

	@Override
	public void onTaskCompleted(ArrayList<Musikschule> musikschuleResult) {

		for (Musikschule elem : musikschuleResult) {
			mMap.addMarker(new MarkerOptions().position(
					new LatLng(elem.getLat(), elem.getLon())).title(
					elem.getName()));
		}

	}

}
