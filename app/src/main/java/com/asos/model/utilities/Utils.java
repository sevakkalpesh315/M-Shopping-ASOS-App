package com.asos.model.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URL;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class Utils {

	public static String downloadFileFromInternet(String url) {
		if (url == null /* || url.isEmpty() == true */)
			new IllegalArgumentException("url is empty/null");
		StringBuilder sb = new StringBuilder();
		InputStream inStream = null;
		try {
			url = urlEncode(url);
			URL link = new URL(url);
			inStream = link.openStream();
			int i;
			int total = 0;
			byte[] buffer = new byte[8 * 1024];
			while ((i = inStream.read(buffer)) != -1) {
				if (total >= (1024 * 1024)) {
					return "";
				}
				total += i;
				sb.append(new String(buffer, 0, i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	private static String urlEncode(String url) {
		if (url == null /* || url.isEmpty() == true */)
			return null;
		url = url.replace("[", "");
		url = url.replace("]", "");
		url = url.replaceAll(" ", "%20");
		return url;
	}

	public static InputStream getInputStreamFromUrl(String url) {
		InputStream content = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			content = response.getEntity().getContent();
		} catch (Exception e) {
			Log.e("Exception Error: ", "Network exception", e);
		}
		return content;
	}

	/**
	 * This method takes one parameters and returns true if network is available
	 * 
	 * @param context
	 *            set the ConnectivityManager application context
	 * @return boolean
	 * @see NetworkInfo
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
