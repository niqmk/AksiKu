package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.aksiku.R;
import com.aksiku.controller.GlobalController;
import com.aksiku.general.GlobalVariables;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Narasi3 extends Fragment {
	public static final String TAG = Narasi3.class.getCanonicalName();
	public static Narasi3 instance;
	private ImageView img_narasi;
	private Button btn_ok;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.narasi3, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(Misi.instance != null) {
			Misi.instance.closeNarasi3Fragment();
		}
		instance = null;
	}
	private void setInitial(final View view) {
		img_narasi = (ImageView)view.findViewById(R.id.img_narasi);
		btn_ok = (Button)view.findViewById(R.id.btn_ok);
		setEventListener();
		populasiData();
	}
	private void setEventListener() {
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
	}
	private void populasiData() {
		ImageLoader.getInstance().displayImage(GlobalVariables.karakter_session.Structure(), img_narasi, GlobalController.getOption(true, true));
	}
	private void goNext() {
		if(Misi.instance == null) {
			return;
		}
		Misi.instance.openMap(true);
	}
}