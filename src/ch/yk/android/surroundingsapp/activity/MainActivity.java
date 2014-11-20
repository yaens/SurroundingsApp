package ch.yk.android.surroundingsapp.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import ch.yk.android.surroundingsapp.businessobject.Daycare;
import ch.yk.android.surroundingsapp.businessobject.Footballplace;
import ch.yk.android.surroundingsapp.businessobject.Kindergarden;
import ch.yk.android.surroundingsapp.businessobject.Musicschool;
import ch.yk.android.surroundingsapp.businessobject.Nursery;
import ch.yk.android.surroundingsapp.businessobject.Park;
import ch.yk.android.surroundingsapp.businessobject.Playground;
import ch.yk.android.surroundingsapp.businessobject.Police;
import ch.yk.android.surroundingsapp.businessobject.Publictoilet;
import ch.yk.android.surroundingsapp.businessobject.Recycling;
import ch.yk.android.surroundingsapp.businessobject.Result;
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

	private Map<String,Map<ConcreteObstacleHandler<? extends Result>,String>> obstacleHandlerMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		//This map contains all obstacleHandler
		this.obstacleHandlerMap = new HashMap<String,Map<ConcreteObstacleHandler<? extends Result>,String>>();
		
		this.setUpMapIfNeeded();
		this.createObstacleMap();
		
		this.settingsHandler = new SettingsHandler(this);
		this.settingsHandler.synchronizeActiveDataPrefs();
		
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
	   
	   this.settingsHandler.synchronizeActiveDataPrefs();
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
	
	private void createObstacleMap(){
		//Add all obstacles to the map
		
		//Add the SchoolHandler
		Map<ConcreteObstacleHandler<? extends Result>,String> schoolHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		schoolHandlerMap.put(new ConcreteObstacleHandler<School>(mapHandler, School.class), "ch_zh_volksschule");
		this.obstacleHandlerMap.put("pref_key_importdata_category_family_schools", schoolHandlerMap);
		
		//Add the MusicschoolHandler
		Map<ConcreteObstacleHandler<? extends Result>,String> musicschoolHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		musicschoolHandlerMap.put(new ConcreteObstacleHandler<Musicschool>(mapHandler, Musicschool.class), "ch_zh_musikschule");
		this.obstacleHandlerMap.put("pref_key_importdata_category_family_musicschool", musicschoolHandlerMap);
		
		//Add the KindergartenHandler
		Map<ConcreteObstacleHandler<? extends Result>,String> kindergardenHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		kindergardenHandlerMap.put(new ConcreteObstacleHandler<Kindergarden>(mapHandler, Kindergarden.class), "ch_zh_kindergarten");
		this.obstacleHandlerMap.put("pref_key_importdata_category_family_kindergarden", kindergardenHandlerMap);
		
		//Add the RecyclingHandler
		Map<ConcreteObstacleHandler<? extends Result>,String> recyclingHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		recyclingHandlerMap.put(new ConcreteObstacleHandler<Recycling>(mapHandler, Recycling.class), "ch_zh_sammelstelle");
		this.obstacleHandlerMap.put("pref_key_importdata_category_common_recycling", recyclingHandlerMap);
		
		//Add the PlaygroundHandler
		Map<ConcreteObstacleHandler<? extends Result>,String> playgroundHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		playgroundHandlerMap.put(new ConcreteObstacleHandler<Playground>(mapHandler, Playground.class), "ch_zh_spielplaetze");
		this.obstacleHandlerMap.put("pref_key_importdata_category_family_playground", playgroundHandlerMap);
		
		//Add the Parks
		Map<ConcreteObstacleHandler<? extends Result>,String> parkHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		parkHandlerMap.put(new ConcreteObstacleHandler<Park>(mapHandler, Park.class), "ch_zh_park");
		this.obstacleHandlerMap.put("pref_key_importdata_category_nature_parks", parkHandlerMap);
		
		//Add the Tenniscourts
		Map<ConcreteObstacleHandler<? extends Result>,String> tenniscourtHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		tenniscourtHandlerMap.put(new ConcreteObstacleHandler<Tenniscourt>(mapHandler, Tenniscourt.class),"ch_zh_tennisplatz");
		this.obstacleHandlerMap.put("pref_key_importdata_category_sports_tenniscourts", tenniscourtHandlerMap);
	
		//Add the Swimmingspools
		Map<ConcreteObstacleHandler<? extends Result>,String> swimmingpoolHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		swimmingpoolHandlerMap.put(new ConcreteObstacleHandler<Swimmingpool>(mapHandler, Swimmingpool.class), "ch_zh_hallenbad");
		this.obstacleHandlerMap.put("pref_key_importdata_category_sports_swimmingpools", swimmingpoolHandlerMap);
	
		//Add the Footballplaces
		Map<ConcreteObstacleHandler<? extends Result>,String> footballplaceHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		footballplaceHandlerMap.put(new ConcreteObstacleHandler<Footballplace>(mapHandler, Footballplace.class), "ch_zh_fussballplatz");
		this.obstacleHandlerMap.put("pref_key_importdata_category_sports_soccer", footballplaceHandlerMap);
		
		//Add the Nurseries
		Map<ConcreteObstacleHandler<? extends Result>,String> nurseryHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		nurseryHandlerMap.put(new ConcreteObstacleHandler<Nursery>(mapHandler, Nursery.class),"ch_zh_kinderkrippe");
		this.obstacleHandlerMap.put("pref_key_importdata_category_family_nursery", nurseryHandlerMap);
		
		//Add the Daycare
		Map<ConcreteObstacleHandler<? extends Result>,String> daycareHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		daycareHandlerMap.put(new ConcreteObstacleHandler<Daycare>(mapHandler, Daycare.class),"ch_zh_kinderhort");
		this.obstacleHandlerMap.put("pref_key_importdata_category_family_daycare", daycareHandlerMap);
		
		//Add the Police
		Map<ConcreteObstacleHandler<? extends Result>,String> policeHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		policeHandlerMap.put(new ConcreteObstacleHandler<Police>(mapHandler, Police.class),"ch_zh_stadtpolizei");
		this.obstacleHandlerMap.put("pref_key_importdata_category_service_police", policeHandlerMap);
		
		//Add the Toilets
		Map<ConcreteObstacleHandler<? extends Result>,String> toiletHandlerMap = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		toiletHandlerMap.put(new ConcreteObstacleHandler<Publictoilet>(mapHandler, Publictoilet.class),"ch_zh_zueriwc_nicht_rollstuhlgaengig");
		toiletHandlerMap.put(new ConcreteObstacleHandler<Publictoilet>(mapHandler, Publictoilet.class),"ch_zh_zueriwc_rollstuhlgaengig");
		this.obstacleHandlerMap.put("pref_key_importdata_category_service_toilet",toiletHandlerMap );
		
		/*Map<ConcreteObstacleHandler<? extends Result>,String>  = new HashMap<ConcreteObstacleHandler<? extends Result>,String>();
		.put(new ConcreteObstacleHandler<>(mapHandler, .class),"");
		this.obstacleHandlerMap.put("", );*/
		
		
	}

	private void setUpMap(Address currentAddress) {
		
		ArrayList<String> activeDataPrefs = this.settingsHandler.getActiveObstacleList();
		
		Double radius = this.settingsHandler.getRadius();
		radius = radius / 1000;
		
		double latitude = currentAddress.getLatitude();
		double longitude = currentAddress.getLongitude();
		
		this.mapHandler.addMarker("Home", latitude, longitude, R.drawable.icon_home, this.buildDescription(currentAddress));
		this.mapHandler.zoomToLocation(latitude, longitude);
		
		for(String activeDataEntry : activeDataPrefs){
			
			ConcreteObstacleHandler<? extends Result> obstacleHandler = null;
			String query = null;
			
			Map<ConcreteObstacleHandler<? extends Result>,String> obstacleHandlerMap = this.obstacleHandlerMap.get(activeDataEntry);
			
			for (Map.Entry<ConcreteObstacleHandler<? extends Result>,String> mapEntry : obstacleHandlerMap.entrySet())
			{
				obstacleHandler = mapEntry.getKey();
				query = mapEntry.getValue();
				obstacleHandler.setQuery(query, latitude, longitude, radius);
				
				new GenericAPICall(obstacleHandler).executeAPICall();
			}
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
