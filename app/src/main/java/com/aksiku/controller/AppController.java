package com.aksiku.controller;

import android.media.MediaPlayer;

import com.aksiku.AksiKuApp;
import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.general.Config;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AppController {
	private static MediaPlayer player;
	public static interface AddressCallback {
		public abstract void getAddressResult(final String result);
	}
	public static void shutterSound() {
		player = MediaPlayer.create(AksiKuApp.getAppContext(), R.raw.shutter);
		player.setLooping(false);
		player.setVolume(150, 150);
		player.start();
	}
	public static void getAddress(final double latitude, final double longitude, final AddressCallback address_callback) {
		final RequestQueue queue = Volley.newRequestQueue(Main.instance);
		final String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude;
		final StringRequest request = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						String result = Config.text_blank;
						try {
							final JSONObject json = new JSONObject(response);
							if(json.getString("status").equals("OK")) {
								final JSONObject data = new JSONObject(json.getJSONArray("results").getString(0));
								result = data.getString("formatted_address");
							}
						}catch(JSONException ex) {}
						address_callback.getAddressResult(result);
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError error) {
				address_callback.getAddressResult(Config.text_blank);
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
	}
}