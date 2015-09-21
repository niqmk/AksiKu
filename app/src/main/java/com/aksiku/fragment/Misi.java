package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.aksiku.AksiKuApp;
import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.LocationController;
import com.aksiku.general.Config;
import com.aksiku.general.GlobalVariables;
import com.aksiku.general.model.MisiModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class Misi extends Fragment {
	public static final String TAG = Misi.class.getCanonicalName();
	public static Misi instance;
	private Narasi1 narasi1_fragment;
	private Narasi2 narasi2_fragment;
	private Narasi3 narasi3_fragment;
	private LayoutInflater inflater;
	private LinearLayout lay_content;
	private ImageButton btn_refresh;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		this.inflater = inflater;
		try {
			final View view = inflater.inflate(R.layout.misi, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		instance = null;
	}
	public void openNarasi1() {
		if(narasi1_fragment != null) {
			return;
		}
		narasi1_fragment = new Narasi1();
		GlobalController.push(narasi1_fragment, Narasi1.TAG);
	}
	public void openNarasi2(final boolean pop) {
		if(narasi2_fragment != null) {
			return;
		}
		if(pop) {
			GlobalController.pop();
		}
		narasi2_fragment = new Narasi2();
		GlobalController.push(narasi2_fragment, Narasi2.TAG);
	}
	public void openNarasi3(final boolean pop) {
		if(narasi3_fragment != null) {
			return;
		}
		if(pop) {
			GlobalController.pop();
		}
		narasi3_fragment = new Narasi3();
		GlobalController.push(narasi3_fragment, Narasi3.TAG);
	}
	public void openMap(final boolean pop) {
		if(!LocationController.isGPSNetworkProviderEnabled(AksiKuApp.getAppContext())) {
			if(Main.instance != null) {
				GlobalController.openSettingsProvider(Main.instance);
			}
			return;
		}
		if(pop) {
			GlobalController.pop();
		}
		if(Main.instance == null) {
			return;
		}
		Main.instance.openMap();
	}
	public void closeNarasi1Fragment() {
		narasi1_fragment = null;
	}
	public void closeNarasi2Fragment() {
		narasi2_fragment = null;
	}
	public void closeNarasi3Fragment() {
		narasi3_fragment = null;
	}
	private void setInitial(final View view) {
		lay_content = (LinearLayout)view.findViewById(R.id.lay_content);
		btn_refresh = (ImageButton)view.findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				populasiData();
			}
		});
		populasiData();
	}
	private void populasiData() {
		lay_content.removeAllViews();
		GlobalController.showLoading(Main.instance);
		final RequestQueue queue = Volley.newRequestQueue(Main.instance);
		final String url = Config.url_server + "missions";
		final StringRequest request = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						GlobalController.closeLoading();
						final MisiModel misi_model = new MisiModel(response);
						setLayout(misi_model);
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError error) {
				GlobalController.closeLoading();
				if(error.networkResponse == null) {
					GlobalController.showToast(R.string.error_no_internet);
					return;
				}
				final int status_code = error.networkResponse.statusCode;
				final NetworkResponse response = error.networkResponse;
				if(status_code == 400) {
					try {
						final String value = new String(response.data, "UTF-8");
						final JSONObject json = new JSONObject(value);
						GlobalController.showToast(json.getString("message"));
					}catch(JSONException ex) {
					}catch(UnsupportedEncodingException ex) {}
					return;
				}
				if(Main.instance == null) {
					return;
				}
				Main.instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.showToast(error.getMessage());
					}
				});
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
	}
	private void setLayout(final MisiModel misi_model) {
		for(int counter = 0; counter < misi_model.misi_list.size(); counter++) {
			MisiModel misi = misi_model.misi_list.get(counter);
			final View baris_misi = inflater.inflate(R.layout.baris_misi, null, false);
			final ImageButton btn_misi_1 = (ImageButton)baris_misi.findViewById(R.id.btn_misi_1);
			final ImageButton btn_misi_2 = (ImageButton)baris_misi.findViewById(R.id.btn_misi_2);
			final ImageButton btn_misi_3 = (ImageButton)baris_misi.findViewById(R.id.btn_misi_3);
			final ImageButton btn_misi_4 = (ImageButton)baris_misi.findViewById(R.id.btn_misi_4);
			ImageLoader.getInstance().displayImage(misi.list_icon, btn_misi_1, GlobalController.getOption(true, true));
			setEventListener(btn_misi_1, misi);
			counter++;
			if(counter < misi_model.misi_list.size()) {
				misi = misi_model.misi_list.get(counter);
				ImageLoader.getInstance().displayImage(misi.list_icon, btn_misi_2, GlobalController.getOption(true, true));
				setEventListener(btn_misi_2, misi);
			}else {
				btn_misi_2.setVisibility(View.INVISIBLE);
			}
			counter++;
			if(counter < misi_model.misi_list.size()) {
				misi = misi_model.misi_list.get(counter);
				ImageLoader.getInstance().displayImage(misi.list_icon, btn_misi_3, GlobalController.getOption(true, true));
				setEventListener(btn_misi_3, misi);
			}else {
				btn_misi_3.setVisibility(View.INVISIBLE);
			}
			counter++;
			if(counter < misi_model.misi_list.size()) {
				misi = misi_model.misi_list.get(counter);
				ImageLoader.getInstance().displayImage(misi.list_icon, btn_misi_4, GlobalController.getOption(true, true));
				setEventListener(btn_misi_4, misi);
			}else {
				btn_misi_4.setVisibility(View.INVISIBLE);
			}
			lay_content.addView(baris_misi);
		}
	}
	private void setEventListener(final ImageButton btn_misi, final MisiModel misi_model) {
		btn_misi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goNext(misi_model);
			}
		});
	}
	private void goNext(final MisiModel misi_model) {
		GlobalVariables.karakter_session.openSession(misi_model);
		openNarasi1();
	}
}