package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aksiku.activity.Main;
import com.aksiku.R;

import java.util.Timer;
import java.util.TimerTask;

public class Home extends Fragment {
	public static final String TAG = Home.class.getCanonicalName();
	public static Home instance;
	private Timer timer;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.home, container, false);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		instance = null;
	}
	@Override
	public void onResume() {
		super.onResume();
		if(timer != null) {
			return;
		}
		timer = new Timer();
		timer.schedule(new FinishTimer(), 5000, 5000);
	}
	private class FinishTimer extends TimerTask {
		@Override
		public void run() {
			timer.cancel();
			timer = null;
			if(Main.instance != null) {
				Main.instance.openPetunjuk();
			}
		}
	}
}