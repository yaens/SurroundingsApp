package ch.yk.android.surroundingsapp.map;

import java.util.HashMap;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import ch.yk.android.surroundingsapp.R;
import ch.yk.android.surroundingsapp.activity.MainActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapHandler {

	private GoogleMap gmap;
	private HashMap<Marker, MarkerInfo> markerMap = new HashMap<Marker, MarkerInfo>();

	public MapHandler(GoogleMap gmap, final MainActivity mainActivity) {
		gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		this.gmap = gmap;
		
		// Setting a custom info window adapter for the google map
		this.gmap.setInfoWindowAdapter(new InfoWindowAdapter() {

	        // Use default InfoWindow frame
	        @Override
	        public View getInfoWindow(Marker arg0) {
	            return null;
	        }

	        // Defines the contents of the InfoWindow
	        @Override
	        public View getInfoContents(Marker arg0) {
	        	
	        	MarkerInfo currentInfo = markerMap.get(arg0);

	            // Getting view from the layout file info_window_layout
	            View v = mainActivity.getLayoutInflater().inflate(R.layout.windowlayout, null);

	            // Getting the position from the marker
	            LatLng latLng = arg0.getPosition();

	            // Getting reference to the TextView to set latitude
	            TextView tvName = (TextView) v.findViewById(R.id.name);

	            // Getting reference to the TextView to set longitude
	            TextView tvDescription = (TextView) v.findViewById(R.id.description);

	            // Setting the latitude
	            tvName.setText(Html.fromHtml("<b>"+currentInfo.getName()+"</b>"));

	            // Setting the longitude
	            tvDescription.setText(currentInfo.getDescription());

	            // Returning the view containing InfoWindow contents
	            return v;

	        }
	    });
	}
	
	public void clearMap(){
		this.markerMap.clear();
		gmap.clear();
	}

	public void addMarker(String name, double lat, double lon, int drawable, String description) {
		
		MarkerOptions markerOptions = new MarkerOptions();
		
		markerOptions.position(new LatLng(lat, lon));
		markerOptions.title(name);
		
		markerOptions.icon(BitmapDescriptorFactory.fromResource(drawable));
		//markerOptions.icon(BitmapDescriptorFactory.fromAsset("icons/"+iconName));
		
		if(!description.isEmpty()){
			markerOptions.snippet(description);
		}	
		Marker newMarker = this.gmap.addMarker(markerOptions);
		
		MarkerInfo newInfo = new MarkerInfo();
		newInfo.setName(name);
		newInfo.setDescription(description);
		
		this.markerMap.put(newMarker, newInfo);
	}
	
	public void zoomToLocation(double lat, double lon){
		LatLng location = new LatLng(lat,lon);
	    this.gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,
	            14));
	}

}
