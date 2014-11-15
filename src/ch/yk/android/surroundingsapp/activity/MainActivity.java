package ch.yk.android.surroundingsapp.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import ch.yk.android.surroundingsapp.R;
import ch.yk.android.surroundingsapp.businessobject.Kindergarten;
import ch.yk.android.surroundingsapp.businessobject.Musikschule;
import ch.yk.android.surroundingsapp.businessobject.Park;
import ch.yk.android.surroundingsapp.businessobject.Sammelstelle;
import ch.yk.android.surroundingsapp.businessobject.Schule;
import ch.yk.android.surroundingsapp.businessobject.Spielplatz;
import ch.yk.android.surroundingsapp.map.MapHandler;
import ch.yk.android.surroundingsapp.obstacleHandler.ConcreteObstacleHandler;
import ch.yk.android.surroundingsapp.rest.GenericAPICall;
import ch.yk.android.surroundingsapp.support.PlaceHolder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends ActionBarActivity {

	private GoogleMap mMap;
	AutoCompleteTextView autoCompleteTextView;
	private Context appContext;
	
	private MapHandler mapHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		
		this.appContext = this.getBaseContext();
		
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.input_address);
		
		autoCompleteTextView.requestFocus();
		autoCompleteTextView.setAdapter(new PlaceHolder(MainActivity.this,R.layout.list_item_autocomplete));
		
		autoCompleteTextView.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		        	
		        	mapHandler.clearMap();
		        	//Hide Keyboard after the ok button click
		        	v.clearFocus();
		        	EditText myEditText = (EditText) findViewById(R.id.input_address);  
		        	InputMethodManager imm = (InputMethodManager)getSystemService(
		        	      Context.INPUT_METHOD_SERVICE);
		        	imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		        	
		        	Geocoder gc=new Geocoder(appContext,Locale.GERMAN);
		        	List<Address> addresses = null;
		        	try {
		        		
						addresses=gc.getFromLocationName(myEditText.getText().toString(), 1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
		        	Address currentAddress = addresses.get(0);
		        	
		            handled = true;
		            
		            setUpMap(currentAddress);
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
			//Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
	        //  .show();
			Intent i = new Intent(this, SettingsActivity.class);
		    startActivity(i);
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
				this.mapHandler = new MapHandler(mMap,this);
			}
		}
	}

	private void setUpMap(Address currentAddress) {
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String sradius = sharedPref.getString("pref_key_common_radius", "100");
		
		Double radius = Double.valueOf(sradius);
		radius = radius / 1000;
		
		double latitude = currentAddress.getLatitude();
		double longitude = currentAddress.getLongitude();
		
		this.mapHandler.addMarker("Home", latitude, longitude, R.drawable.icon_home, this.buildDescription(currentAddress));
		this.mapHandler.zoomToLocation(latitude, longitude);
		
		ConcreteObstacleHandler<Musikschule> musikschuleHandler = new ConcreteObstacleHandler<Musikschule>(mapHandler, Musikschule.class);
		musikschuleHandler.setQuery("ch_zh_musikschule", latitude, longitude, radius);
		
		ConcreteObstacleHandler<Kindergarten> kindergartenHandler = new ConcreteObstacleHandler<Kindergarten>(mapHandler, Kindergarten.class);
		kindergartenHandler.setQuery("ch_zh_kindergarten", latitude, longitude, radius);
		
		ConcreteObstacleHandler<Schule> schuleHandler = new ConcreteObstacleHandler<Schule>(mapHandler, Schule.class);
		schuleHandler.setQuery("ch_zh_volksschule", latitude, longitude, radius);
		
		ConcreteObstacleHandler<Sammelstelle> sammelstelleHandler = new ConcreteObstacleHandler<Sammelstelle>(mapHandler, Sammelstelle.class);
		sammelstelleHandler.setQuery("ch_zh_sammelstelle", latitude, longitude, radius);
		
		ConcreteObstacleHandler<Spielplatz> spielplatzHandler = new ConcreteObstacleHandler<Spielplatz>(mapHandler, Spielplatz.class);
		spielplatzHandler.setQuery("ch_zh_spielplaetze", latitude, longitude, radius);
		
		ConcreteObstacleHandler<Park> parkHandler = new ConcreteObstacleHandler<Park>(mapHandler, Park.class);
		parkHandler.setQuery("ch_zh_park", latitude, longitude, radius);
		
		new GenericAPICall(musikschuleHandler).executeAPICall();
		new GenericAPICall(kindergartenHandler).executeAPICall();
		new GenericAPICall(schuleHandler).executeAPICall();
		new GenericAPICall(sammelstelleHandler).executeAPICall();
		new GenericAPICall(spielplatzHandler).executeAPICall();
		new GenericAPICall(parkHandler).executeAPICall();

	}
	
	private String buildDescription(Address currentAddress){
		String description = "";
		
		description += currentAddress.getThoroughfare();
		
		if(currentAddress.getSubThoroughfare()!=null){
			description += " " + currentAddress.getSubThoroughfare();
		}
		
		if(currentAddress.getPostalCode()!=null){
			description += ", " + currentAddress.getPostalCode();
		}else {
			description += ", ";
		}
		
		if(currentAddress.getSubAdminArea()!=null){
			description += " " + currentAddress.getSubAdminArea();
		}
		
		if(currentAddress.getSubLocality()!=null){
			description += " " + currentAddress.getSubLocality();
		}
		
		return description;
	}
}
