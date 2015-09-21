package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.aksiku.AksiKuApp;
import com.aksiku.R;
import com.aksiku.controller.NarasiController;
import com.aksiku.custom.TypedTextView;
import com.aksiku.general.GlobalVariables;

public class Narasi2 extends Fragment {
	public static final String TAG = Narasi2.class.getCanonicalName();
	public static final int MAX_STEP = Narasi1.MAX_STEP + 2;
	public static Narasi2 instance;
	private TypedTextView lbl_narasi;
	private ImageButton btn_lanjut;
	private int step = Narasi1.MAX_STEP + 1;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.narasi2, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(Misi.instance != null) {
			Misi.instance.closeNarasi2Fragment();
		}
		instance = null;
	}
	private void setInitial(final View view) {
		lbl_narasi = (TypedTextView) view.findViewById(R.id.lbl_narasi);
		btn_lanjut = (ImageButton) view.findViewById(R.id.btn_lanjut);
		setEventListener();
		populasiData();
	}
	private void setEventListener() {
		btn_lanjut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(step < MAX_STEP) {
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
	}
	private void goNext() {
		if(Misi.instance == null) {
			return;
		}
		Misi.instance.openNarasi3(true);
	}
}