package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aksiku.R;
import com.aksiku.controller.GlobalController;

public class TambahPin extends Fragment {
	public static final String TAG = TambahPin.class.getCanonicalName();
	public static TambahPin instance;
	private View vw_frame;
	private Button btn_ya;
	private Button btn_tidak;
	private boolean tambah = false;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.tambah_pin, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(Map.instance != null) {
			Map.instance.closeTambahPinFragment(tambah);
		}
		instance = null;
	}
	private void setInitial(final View view) {
		vw_frame = (View)view.findViewById(R.id.vw_frame);
		btn_ya = (Button)view.findViewById(R.id.btn_ya);
		btn_tidak = (Button)view.findViewById(R.id.btn_tidak);
		setEventListener();
		GlobalController.setAlpha(vw_frame, 0.5f);
	}
	private void setEventListener() {
		btn_ya.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tambah = true;
				GlobalController.pop();
			}
		});
		btn_tidak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tambah = false;
				GlobalController.pop();
			}
		});
	}
}