package com.aksiku.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.aksiku.R;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.MusicController;
import com.aksiku.fragment.Home;
import com.aksiku.fragment.Map;
import com.aksiku.fragment.Masuk;
import com.aksiku.fragment.Misi;
import com.aksiku.fragment.Petunjuk;
import com.aksiku.fragment.ProfilePicture;
import com.aksiku.fragment.Daftar;
import com.aksiku.fragment.Selesai;
import com.aksiku.general.GlobalVariables;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Main extends FragmentActivity {
	public static Main instance;
	private FragmentManager fm_main;
	private Home home_fragment;
	private Petunjuk petunjuk_fragment;
	private Masuk masuk_fragment;
	private Daftar signin_fragment;
	private ProfilePicture profilepicture_fragment;
	private Misi misi_fragment;
	private Map map_fragment;
	private Selesai selesai_fragment;
	private boolean bukan_pause = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		instance = this;
		setInitial();
	}
	@Override
	protected void onResume() {
		super.onResume();
		bukan_pause = false;
		if(!GlobalVariables.user_session.isSession()) {
			MusicController.stop();
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		if(!bukan_pause) {
			MusicController.stop();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(fm_main.getBackStackEntryCount() > 0) {
				fm_main.popBackStack();
			}else {
				if(Map.instance != null) {
					Map.instance.onKeyDown();
				}else {
					close();
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Fragment fragment = (Fragment)getSupportFragmentManager().findFragmentById(R.id.lay_main);
		if(fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	public void bukanPause() {
		bukan_pause = true;
	}
	public void pop() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (fm_main.getBackStackEntryCount() > 0) {
					fm_main.popBackStack();
				}
			}
		});
	}
	public void openHome() {
		home_fragment = new Home();
		fm_main.beginTransaction()
				.replace(R.id.lay_main, home_fragment, Home.TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	public void openPetunjuk() {
		petunjuk_fragment = new Petunjuk();
		fm_main.beginTransaction()
				.replace(R.id.lay_main, petunjuk_fragment, Petunjuk.TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	public void openMasuk() {
		masuk_fragment = new Masuk();
		fm_main.beginTransaction()
				.replace(R.id.lay_main, masuk_fragment, Masuk.TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	public void openDaftar() {
		signin_fragment = new Daftar();
		fm_main.beginTransaction()
				.replace(R.id.lay_main, signin_fragment, Daftar.TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	public void openProfilePicture() {
		profilepicture_fragment = new ProfilePicture();
		fm_main.beginTransaction()
				.replace(R.id.lay_main, profilepicture_fragment, ProfilePicture.TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	public void openMisi() {
		MusicController.stop();
		misi_fragment = new Misi();
		fm_main.beginTransaction()
				.replace(R.id.lay_main, misi_fragment, Misi.TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	public void openMap() {
		map_fragment = new Map();
		fm_main.beginTransaction()
				.replace(R.id.lay_main, map_fragment, Map.TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	public void openSelesai() {
		selesai_fragment = new Selesai();
		fm_main.beginTransaction()
				.replace(R.id.lay_main, selesai_fragment, Selesai.TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	public void close() {
		GlobalVariables.unload();
		finish();
	}
	private void setInitial() {
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
						.setDefaultFontPath("fonts/CL.TTF")
						.setFontAttrId(R.attr.fontPath)
						.build());
		fm_main = getSupportFragmentManager();
		fm_main.addOnBackStackChangedListener(getListener());
		if(GlobalVariables.user_session.isSession()) {
			if(GlobalController.isNotNull(GlobalVariables.user_session.PP())) {
				openMisi();
				//openMap();
			}else {
				openProfilePicture();
			}
		}else {
			openHome();
		}
	}
	private FragmentManager.OnBackStackChangedListener getListener() {
		FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
			public void onBackStackChanged() {
				FragmentManager manager = getSupportFragmentManager();
				if(manager != null) {
					Fragment fragment = (Fragment)manager.findFragmentById(R.id.lay_main);
					if(fragment != null) {
						fragment.onResume();
					}
				}
			}
		};
		return result;
	}
}