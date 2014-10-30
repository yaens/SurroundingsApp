package ch.yk.android.surroundingsapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import ch.yk.android.surroundingsapp.R;
import ch.yk.android.surroundingsapp.businessobject.Musikschule;
import ch.yk.android.surroundingsapp.obstacleHandler.ConcreteObstacleHandler;
import ch.yk.android.surroundingsapp.rest.GenericAPICall;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends FragmentActivity {

	private GoogleMap mMap;
	AutoCompleteTextView autoCompleteTextView;
	
	private MapHandler mapHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.input_address);
		
		autoCompleteTextView.requestFocus();
		autoCompleteTextView.setAdapter(new PlaceHolder(MainActivity.this,R.layout.list_item_autocomplete));
		
		autoCompleteTextView.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		        	mMap.clear();
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
				this.mapHandler = new MapHandler(mMap);
			}
		}
	}

	private void setUpMap() {
		
		ConcreteObstacleHandler msHandler = new ConcreteObstacleHandler<Musikschule>(mapHandler, Musikschule.class);
		msHandler.setQuery("ch_zh_kindergarten", 47.367, 8.5500, 10);
		new GenericAPICall(msHandler).executeAPICall();

	}
}
