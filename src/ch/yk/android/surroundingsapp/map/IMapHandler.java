package ch.yk.android.surroundingsapp.map;

public interface IMapHandler {
	
	public void clearMap();
	public void addMarker(String name, double lat, double lon, int drawable, String description);

}
