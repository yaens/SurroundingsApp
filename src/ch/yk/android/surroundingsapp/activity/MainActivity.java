package ch.yk.android.surroundingsapp.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import ch.yk.android.surroundingsapp.R;
import ch.yk.android.surroundingsapp.businessobject.Kindergarden;
import ch.yk.android.surroundingsapp.businessobject.Musicschool;
import ch.yk.android.surroundingsapp.businessobject.Park;
import ch.yk.android.surroundingsapp.businessobject.Playground;
import ch.yk.android.surroundingsapp.businessobject.Recycling;
import ch.yk.android.surroundingsapp.businessobject.School;
import ch.yk.android.surroundingsapp.businessobject.Swimmingpool;
import ch.yk.android.surroundingsapp.businessobject.Tenniscourt;
import ch.yk.android.surroundingsapp.map.MapHandler;
import ch.yk.android.surroundingsapp.obstacleHandler.ConcreteObstacleHandler;
import ch.yk.android.surroundingsapp.rest.GenericAPICall;
import ch.yk.android.surroundingsapp.support.PlaceHolder;
import ch.yk.android.surroundingsapp.support.SettingsHandler;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends ActionBarActivity {

	private GoogleMap mMap;
	AutoCompleteTextView autoCompleteTextView;
	private Context appContext;
	
	private MapHandler mapHandler;
	private SettingsHandler settingsHandler;
	
	public static boolean refreshMap = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		
		this.settingsHandler = new SettingsHandler(this);
		
		this.appContext = this.getBaseContext();
		
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.input_address);
		
		autoCompleteTextView.requestFocus();
		autoCompleteTextView.setAdapter(new PlaceHolder(MainActivity.this,R.layout.list_item_autocomplete));
		
		autoCompleteTextView.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
		        boolean handled = false;

		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		        	
		        	//clear the map after entering a new adress
		        	mapHandler.clearMap();
		        	//Hide Keyboard after the ok button click
		        	textView.clearFocus();
		        	
		        	EditText searchAdressField = (EditText) findViewById(R.id.input_address);
		        	((LinearLayout) findViewById(R.id.dummy_id)).requestFocus();
		        	
		        	InputMethodManager autoCompletionManager = (InputMethodManager)getSystemService(
		        	      Context.INPUT_METHOD_SERVICE);
		        	autoCompletionManager.hideSoftInputFromWindow(searchAdressField.getWindowToken(), 0);
		        	
		        	Geocoder gc=new Geocoder(appContext,Locale.GERMAN);
		        	List<Address> addresses = null;
		        	try {
		        		
						addresses=gc.getFromLocationName(searchAdressField.getText().toString(), 1);
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
	protected void onResume()
	{
	   super.onResume();

	   if(refreshMap){
		   this.mapHandler.clearMap();
		   refreshMap = false;
	   }
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
		
		Double radius = this.settingsHandler.getRadius();
		radius = radius / 1000;
		
		double latitude = currentAddress.getLatitude();
		double longitude = currentAddress.getLongitude();
		
		this.mapHandler.addMarker("Home", latitude, longitude, R.drawable.icon_home, this.buildDescription(currentAddress));
		this.mapHandler.zoomToLocation(latitude, longitude);
		
		if(this.settingsHandler.isIncludeMusicschool()){
			ConcreteObstacleHandler<Musicschool> musikschuleHandler = new ConcreteObstacleHandler<Musicschool>(mapHandler, Musicschool.class);
			musikschuleHandler.setQuery("ch_zh_musikschule", latitude, longitude, radius);
			new GenericAPICall(musikschuleHandler).executeAPICall();
		}
		
		if(this.settingsHandler.isIncludeKindergarden()){
			ConcreteObstacleHandler<Kindergarden> kindergartenHandler = new ConcreteObstacleHandler<Kindergarden>(mapHandler, Kindergarden.class);
			kindergartenHandler.setQuery("ch_zh_kindergarten", latitude, longitude, radius);
			new GenericAPICall(kindergartenHandler).executeAPICall();
		}
		
		if(this.settingsHandler.isIncludeSchools()){
			ConcreteObstacleHandler<School> schuleHandler = new ConcreteObstacleHandler<School>(mapHandler, School.class);
			schuleHandler.setQuery("ch_zh_volksschule", latitude, longitude, radius);
			new GenericAPICall(schuleHandler).executeAPICall();
		}
		
		if(this.settingsHandler.isIncludeRecycling()){
			ConcreteObstacleHandler<Recycling> sammelstelleHandler = new ConcreteObstacleHandler<Recycling>(mapHandler, Recycling.class);
			sammelstelleHandler.setQuery("ch_zh_sammelstelle", latitude, longitude, radius);
			new GenericAPICall(sammelstelleHandler).executeAPICall();
		}
		
		if(this.settingsHandler.isIncludePlayground()){
			ConcreteObstacleHandler<Playground> spielplatzHandler = new ConcreteObstacleHandler<Playground>(mapHandler, Playground.class);
			spielplatzHandler.setQuery("ch_zh_spielplaetze", latitude, longitude, radius);
			new GenericAPICall(spielplatzHandler).executeAPICall();
		}
		
		if(this.settingsHandler.isIncludeParks()){
			ConcreteObstacleHandler<Park> parkHandler = new ConcreteObstacleHandler<Park>(mapHandler, Park.class);
			parkHandler.setQuery("ch_zh_park", latitude, longitude, radius);
			new GenericAPICall(parkHandler).executeAPICall();
		}
		
		if(this.settingsHandler.isIncludeTenniscourt()){
			ConcreteObstacleHandler<Tenniscourt> tenniscourtHandler = new ConcreteObstacleHandler<Tenniscourt>(mapHandler, Tenniscourt.class);
			tenniscourtHandler.setQuery("ch_zh_tennisplatz", latitude, longitude, radius);
			new GenericAPICall(tenniscourtHandler).executeAPICall();
		}
		
		if(this.settingsHandler.isIncludeSwimmingpool()){
			ConcreteObstacleHandler<Swimmingpool> swimmingpoolHandler = new ConcreteObstacleHandler<Swimmingpool>(mapHandler, Swimmingpool.class);
			swimmingpoolHandler.setQuery("ch_zh_hallenbad", latitude, longitude, radius);
			new GenericAPICall(swimmingpoolHandler).executeAPICall();
		}
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
