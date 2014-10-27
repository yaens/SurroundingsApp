package ch.yk.android.surroundingsapp.activity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ch.yk.android.surroundingsapp.R;
import ch.yk.android.surroundingsapp.R.id;
import ch.yk.android.surroundingsapp.R.layout;
import ch.yk.android.surroundingsapp.R.menu;
import ch.yk.android.surroundingsapp.RESTResult.Musikschule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnTaskCompleted{

	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
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
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		
		new CallAPI(this).execute();

	}

	@Override
	public void onTaskCompleted(ArrayList<Musikschule> musikschuleResult) {
		
		for(Musikschule elem : musikschuleResult){
			mMap.addMarker(new MarkerOptions().position(new LatLng(elem.getLat(), elem.getLon())).title(
					elem.getName()));
		}
			   
		
	}
	

	private class CallAPI extends AsyncTask<String, String, String> {
		
		OnTaskCompleted callBackListener;
		ArrayList<Musikschule> musikschuleResult = new ArrayList<Musikschule>();
		
		public CallAPI(OnTaskCompleted listener){
			this.callBackListener = listener;
		}

		@Override
		protected String doInBackground(String... params) {

			InputStream in = null;
			HttpURLConnection conn = null;
			URL url = null;

			try {
				url = new URL("https://mingle.io/query?q=ch_zh_musikschule");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;

			trustAllHosts();
			HttpsURLConnection https = null;

			try {

				https = (HttpsURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			https.setHostnameVerifier(DO_NOT_VERIFY);
			conn = https;

			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				in = new BufferedInputStream(conn.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedReader streamReader = null;
			try {
				streamReader = new BufferedReader(new InputStreamReader(in,
						"UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			try {
				while ((inputStr = streamReader.readLine()) != null)
					responseStrBuilder.append(inputStr);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject test = null;

			try {
				test = new JSONObject(responseStrBuilder.toString());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JSONArray arr = null;
			try {
				arr = test.getJSONArray("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < arr.length(); i++)
			{
			    try {
					String lat = arr.getJSONObject(i).getString("lat");
					String lon = arr.getJSONObject(i).getString("lon");
					String name = arr.getJSONObject(i).getString("Name");
					
					Musikschule ms = new Musikschule();
					ms.setName(name);
					ms.setLat(Double.parseDouble(lat));
					ms.setLon(Double.parseDouble(lon));
					
					musikschuleResult.add(ms);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			}
			
			
			return null;
		}
		
		   // required methods

	    protected void onPostExecute(String result){
	        // your stuff
	        callBackListener.onTaskCompleted(musikschuleResult);
	    }

	}

	// always verify the host - dont check for certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub

			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
