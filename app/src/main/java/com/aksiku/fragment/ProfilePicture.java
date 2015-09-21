package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.aksiku.AksiKuApp;
import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.controller.CameraController;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.ImageController;
import com.aksiku.general.Config;
import com.aksiku.general.GlobalVariables;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map;

public class ProfilePicture extends Fragment {
	public static final String TAG = ProfilePicture.class.getCanonicalName();
	public static ProfilePicture instance;
	private CameraController camera_controller;
	private SurfaceView vw_master;
	private FrameLayout lay_camera;
	private Button btn_capture;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.profilepicture, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		if(camera_controller != null) {
			camera_controller.close();
		}
		super.onDestroyView();
		instance = null;
	}
	private void setInitial(final View view) {
		vw_master = (SurfaceView)view.findViewById(R.id.vw_master);
		lay_camera = (FrameLayout)view.findViewById(R.id.lay_camera);
		btn_capture = (Button)view.findViewById(R.id.btn_capture);
		camera_controller = new CameraController(AksiKuApp.getAppContext(), vw_master, true, camera_callback);
		camera_controller.showPreview(lay_camera, false);
		setEventListener();
	}
	private void setEventListener() {
		btn_capture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (camera_controller != null) {
					GlobalController.vibrate(AksiKuApp.getAppContext(), 300);
					camera_controller.take(false);
				}
			}
		});
	}
	private void goNext() {
		if(Main.instance != null) {
			Main.instance.openMisi();
		}
	}
	private void setRequest(final byte[] data) {
		GlobalController.showLoading(Main.instance);
		try {
			final String string_data = ImageController.getStringFromBitmap(ImageController.getBitmapFromBytes(data), 70);
			final JSONObject json = new JSONObject();
			json.put("access_token", GlobalVariables.user_session.Token());
			json.put("profile_picture", string_data);
			final RequestQueue queue = Volley.newRequestQueue(Main.instance);
			final String url = Config.url_server + "players/" + GlobalVariables.user_session.Id();
			final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, json,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							GlobalController.closeLoading();
							GlobalVariables.user_session.savePP(string_data);
							goNext();
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							GlobalController.closeLoading();
							if(error.networkResponse == null) {
								GlobalController.showToast(R.string.error_no_internet);
								return;
							}
							final int status_code = error.networkResponse.statusCode;
							final NetworkResponse response = error.networkResponse;
							if(status_code == 400 || status_code == 403) {
								try {
									final String value = new String(response.data, "UTF-8");
									final JSONObject json = new JSONObject(value);
									GlobalController.showToast(json.getString("message"));
								}catch(JSONException ex) {
								}catch(UnsupportedEncodingException ex) {}
								return;
							}
						}
					}
			) {
				@Override
				public Map<String, String> getHeaders() {
					Map<String, String> headers = new HashMap<>();
					headers.put("Content-Type", "application/json");
					headers.put("Accept", "application/json");
					return headers;
				}
			};
			request.setRetryPolicy(new DefaultRetryPolicy(
					30000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			queue.add(request);
		}catch(JSONException ex) {}
	}
	private CameraController.CameraCallback camera_callback = new CameraController.CameraCallback() {
		@Override
		public void didGetPicture(byte[] data) {
			setRequest(data);
		}
	};
}