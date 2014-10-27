package ch.yk.android.surroundingsapp.rest;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import ch.yk.android.surroundingsapp.RESTResult.Musikschule;
import ch.yk.android.surroundingsapp.activity.OnTaskCompleted;

public class CallAPI extends AsyncTask<String, String, String> {
	
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
	
    protected void onPostExecute(String result){
        // your stuff
        callBackListener.onTaskCompleted(musikschuleResult);
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



