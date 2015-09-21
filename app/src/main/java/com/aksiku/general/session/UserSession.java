package com.aksiku.general.session;

import com.aksiku.AksiKuApp;
import com.aksiku.controller.GlobalController;
import com.aksiku.general.Config;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
	private static final String TOKEN = SessionConfig.session_init_name + "TOKEN";
	private static final String ID = SessionConfig.session_init_name + "ID";
	private static final String NAMA = SessionConfig.session_init_name + "NAMA";
	private static final String PP = SessionConfig.session_init_name + "PP";
	public void openSession(
			final String token,
			final int id,
			final String nama,
			final String pp) {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_init_name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(TOKEN, token);
		editor.putInt(ID, id);
		editor.putString(NAMA, nama);
		editor.putString(PP, pp);
		editor.commit();
	}
	public void savePP(final String pp) {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_init_name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PP, pp);
		editor.commit();
	}
	public void closeSession() {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_init_name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(TOKEN);
		editor.remove(ID);
		editor.remove(NAMA);
		editor.remove(PP);
		editor.commit();
	}
	public boolean isSession() {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_init_name, Context.MODE_PRIVATE);
		if(GlobalController.isNotNull(preferences.getString(TOKEN, Config.text_blank))) {
			return true;
		}else {
			return false;
		}
	}
	public final String getFromSession(final String id) {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_init_name, Context.MODE_PRIVATE);
		if(GlobalController.isNotNull(preferences.getString(id, Config.text_blank) )) {
			return preferences.getString(id, Config.text_blank);
		}else {
			return Config.text_blank;
		}
	}
	public final int getIntFromSession(final String id) {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_init_name, Context.MODE_PRIVATE);
		return preferences.getInt(id, 0);
	}
	public final String Token() {
		return getFromSession(TOKEN);
	}
	public final int Id() {
		return getIntFromSession(ID);
	}
	public final String Nama() {
		return getFromSession(NAMA);
	}
	public final String PP() {
		return getFromSession(PP);
	}
}