package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.controller.GlobalController;
import com.aksiku.custom.TypedTextView;

public class Petunjuk extends Fragment {
	public static final String TAG = Petunjuk.class.getCanonicalName();
	public static Petunjuk instance;
	private TypedTextView lbl_petunjuk;
	private Button btn_ok;
	private ImageButton btn_lanjut;
	private final int max_index = 2;
	private int index = 1;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.petunjuk, container, false);
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
		lbl_petunjuk = (TypedTextView) view.findViewById(R.id.lbl_petunjuk);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_lanjut = (ImageButton) view.findViewById(R.id.btn_lanjut);
		setEventListener();
		setPetunjuk(index);
	}
	private void setEventListener() {
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
		btn_lanjut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPetunjuk(index + 1);
			}
		});
	}
	private void setPetunjuk(final int index) {
		this.index = index;
		lbl_petunjuk.setText(GlobalController.getString("text_petunjuk_" + index));
		if(max_index == index) {
			btn_ok.setVisibility(View.VISIBLE);
			btn_lanjut.setVisibility(View.INVISIBLE);
		}
	}
	private void goNext() {
		if(Main.instance != null) {
			Main.instance.openMasuk();
		}
	}
}