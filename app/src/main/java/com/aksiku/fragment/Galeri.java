package com.aksiku.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.aksiku.R;
import com.aksiku.custom.CustomViewPager;
import com.aksiku.general.model.MisiPhotosModel;
import com.aksiku.view.GaleriView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Galeri extends Fragment {
	public static final String TAG = Galeri.class.getCanonicalName();
	public static Galeri instance;
	private LayoutInflater inflater;
	private ImageButton btn_kiri;
	private ImageButton btn_kanan;
	private CustomViewPager vwp_quest;
	private ArrayList<View> view_list = new ArrayList<>();
	private GaleriPagerAdapter adapter;
	private int index = 0;
	private HashMap<Integer, ArrayList> quest_simpan_map;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.galeri, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(Map.instance != null) {
			Map.instance.closeGaleriFragment();
		}
		instance = null;
	}
	private void setInitial(final View view) {
		btn_kiri = (ImageButton) view.findViewById(R.id.btn_kiri);
		btn_kanan = (ImageButton) view.findViewById(R.id.btn_kanan);
		vwp_quest = (CustomViewPager) view.findViewById(R.id.vwp_quest);
		setEventListener();
		populasiData();
	}
	private void setEventListener() {
		btn_kiri.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doKiri();
			}
		});
		btn_kanan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doKanan();
			}
		});
	}
	private void doKiri() {
		if(index <= 0) {
			return;
		}
		index--;
		vwp_quest.setCurrentItem(index);
	}
	private void doKanan() {
		if(index >= (view_list.size() - 1)) {
			return;
		}
		index++;
		vwp_quest.setCurrentItem(index);
	}
	private void populasiData() {
		quest_simpan_map = (HashMap<Integer, ArrayList>)getArguments().getSerializable(Galeri.TAG);
		if(quest_simpan_map == null) {
			return;
		}
		Iterator iterator = quest_simpan_map.entrySet().iterator();
		while(iterator.hasNext()) {
			java.util.Map.Entry pair = (java.util.Map.Entry)iterator.next();
			final GaleriView galeri_view = new GaleriView(inflater, (int)pair.getKey(), (ArrayList<MisiPhotosModel>)pair.getValue());
			view_list.add(galeri_view.getView());
		}
		adapter = new GaleriPagerAdapter();
		vwp_quest.setAdapter(adapter);
		vwp_quest.setScrollDurationFactor(3.0);
		vwp_quest.setCurrentItem(0);
	}
	private class GaleriPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return view_list.size();
		}
		@Override
		public Object instantiateItem(ViewGroup collection, int position) {
			collection.addView(view_list.get(position), 0);
			return view_list.get(position);
		}
		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
			collection.removeView(view_list.get(position));
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}
		@Override
		public void finishUpdate(ViewGroup arg0) {}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override
		public Parcelable saveState() {
			return null;
		}
		@Override
		public void startUpdate(ViewGroup arg0) {}
	}
}