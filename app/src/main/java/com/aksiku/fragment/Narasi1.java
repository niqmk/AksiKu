package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aksiku.R;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.NarasiController;
import com.aksiku.custom.TypedTextView;
import com.aksiku.general.GlobalVariables;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Narasi1 extends Fragment {
	public static final String TAG = Narasi1.class.getCanonicalName();
	public static final int MAX_STEP = 3;
	public static Narasi1 instance;
	private ImageView img_karakter;
	private TypedTextView lbl_narasi;
	private ImageButton btn_lanjut;
	private int step = 1;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.narasi1, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(Misi.instance != null) {
			Misi.instance.closeNarasi1Fragment();
		}
		instance = null;
	}
	private void setInitial(final View view) {
		img_karakter = (ImageView)view.findViewById(R.id.img_karakter);
		lbl_narasi = (TypedTextView)view.findViewById(R.id.lbl_narasi);
		btn_lanjut = (ImageButton)view.findViewById(R.id.btn_lanjut);
		setEventListener();
		populasiData();
	}
	private void setEventListener() {
		btn_lanjut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (step < MAX_STEP) {
					step++;
					populasiData();
				}else {
					goNext();
				}
			}
		});
	}
	private void populasiData() {
		lbl_narasi.setText(NarasiController.get(step));
		if(step == 1) {
			ImageLoader.getInstance().displayImage(GlobalVariables.karakter_session.Smile(), img_karakter, GlobalController.getOption(true, false));
		}else if(step < MAX_STEP) {
			ImageLoader.getInstance().displayImage(GlobalVariables.karakter_session.Sad(), img_karakter, GlobalController.getOption(true, false));
		}
	}
	private void goNext() {
		if(Misi.instance == null) {
			return;
		}
		Misi.instance.openNarasi2(true);
	}
}