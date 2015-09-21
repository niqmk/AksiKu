package com.aksiku.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aksiku.AksiKuApp;
import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.activity.Trial;
import com.aksiku.controller.GlobalController;
import com.aksiku.general.Config;
import com.aksiku.general.GlobalVariables;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Daftar extends Fragment {
	public static final String TAG = Daftar.class.getCanonicalName();
	public static Daftar instance;
	private EditText txt_nama;
	private EditText txt_nis;
	private EditText txt_email;
	private EditText txt_password;
	private Button btn_submit;
	private Button btn_masuk;
	private Button btn_trial;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.daftar, container, false);
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
	private void setInitial(final View view) {
		txt_nama = (EditText) view.findViewById(R.id.txt_nama);
		txt_nis = (EditText) view.findViewById(R.id.txt_nis);
		txt_email = (EditText) view.findViewById(R.id.txt_email);
		txt_password = (EditText) view.findViewById(R.id.txt_password);
		btn_submit = (Button) view.findViewById(R.id.btn_submit);
		btn_masuk = (Button) view.findViewById(R.id.btn_masuk);
		btn_trial = (Button) view.findViewById(R.id.btn_trial);
		setEventListener();
	}
	private void setEventListener() {
		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doSubmit();
			}
		});
		btn_masuk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Main.instance != null) {
					Main.instance.openMasuk();
				}
			}
		});
		btn_trial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Main.instance == null) {
					return;
				}
				Intent intent = new Intent(AksiKuApp.getAppContext(), Trial.class);
				Main.instance.startActivity(intent);
			}
		});
	}
	private void doSubmit() {
		final String nama = txt_nama.getText().toString();
		final String nis = txt_nis.getText().toString();
		final String email = txt_email.getText().toString();
		final String password = txt_password.getText().toString();
		if(!GlobalController.isNotNull(nama)) {
			GlobalController.showToast(R.string.error_nama);
			return;
		}else if(!GlobalController.isNotNull(nis)) {
			GlobalController.showToast(R.string.error_nis);
			return;
		}else if(!GlobalController.isEmailValid(email)) {
			GlobalController.showToast(R.string.error_email);
			return;
		}else if(!GlobalController.isNotNull(password)) {
			GlobalController.showToast(R.string.error_password);
			return;
		}
		GlobalController.closeKeyboard(Main.instance);
		GlobalController.showLoading(Main.instance);
		final RequestQueue queue = Volley.newRequestQueue(Main.instance);
		final String url = Config.url_server + "players/register";
		final StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						GlobalController.closeLoading();
						try {
							final JSONObject json = new JSONObject(response);
							final String access_token = json.getString("access_token");
							final int id = json.getInt("id");
							final String username = json.getString("username");
							GlobalVariables.user_session.openSession(access_token, id, username, Config.text_blank);
							goNext();
						}catch(JSONException ex) {}
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
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<>();
				params.put("username", nama);
				params.put("nis", nis);
				params.put("email", email);
				params.put("password_hash", password);
				return params;
			}
		};
		request.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
	}
	private void goNext() {
		if(Main.instance != null) {
			Main.instance.openProfilePicture();
		}
	}
}