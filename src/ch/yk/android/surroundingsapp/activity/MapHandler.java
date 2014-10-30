package ch.yk.android.surroundingsapp.activity;

import ch.yk.android.surroundingsapp.businessobject.Musikschule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapHandler {

	private GoogleMap gmap;

	public MapHandler(GoogleMap gmap) {
		this.gmap = gmap;
	}

	public void addMarker(String name, double lat, double lon, String iconName) {
		
		MarkerOptions markerOptions = new MarkerOptions();
		
		markerOptions.position(new LatLng(lat, lon));
		markerOptions.title(name);
		markerOptions.icon(BitmapDescriptorFactory.fromAsset(iconName));
		
		this.gmap.addMarker(markerOptions);
	}

}
