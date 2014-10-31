package ch.yk.android.surroundingsapp.activity;

import com.google.android.gms.maps.CameraUpdateFactory;
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
		markerOptions.icon(BitmapDescriptorFactory.fromAsset("icons/"+iconName));
		
		this.gmap.addMarker(markerOptions);
	}
	
	public void zoomToLocation(double lat, double lon){
		LatLng location = new LatLng(lat,lon);
	    this.gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,
	            14));
	}

}
