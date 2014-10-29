package ch.yk.android.surroundingsapp.activity;

import ch.yk.android.surroundingsapp.RESTResult.Musikschule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapHandler {

	private GoogleMap gmap;

	public MapHandler(GoogleMap gmap) {
		this.gmap = gmap;
	}

	public void addMarker(String name, double lat, double lon) {
		gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
				.title(name));
	}

}
