package com.aksiku;

import android.app.Application;
import android.content.Context;

import com.aksiku.general.GlobalVariables;

public class AksiKuApp extends Application {
	private static Context context;
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		setInitial();
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
		GlobalVariables.unload();
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	public static Context getAppContext() {
		return context;
	}
	private void setInitial() {
		GlobalVariables.load();
	}
}