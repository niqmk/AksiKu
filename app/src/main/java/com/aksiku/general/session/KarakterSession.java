package com.aksiku.general.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.aksiku.AksiKuApp;
import com.aksiku.controller.GlobalController;
import com.aksiku.general.Config;
import com.aksiku.general.model.MisiModel;

public class KarakterSession {
	private static final String ID = SessionConfig.session_karakter_name + "ID";
	private static final String NAMA = SessionConfig.session_karakter_name + "NAMA";
	private static final String IMAGE = SessionConfig.session_karakter_name + "IMAGE";
	private static final String BADGE = SessionConfig.session_karakter_name + "BADGE";
	private static final String SMILE = SessionConfig.session_karakter_name + "SMILE";
	private static final String SAD = SessionConfig.session_karakter_name + "SAD";
	private static final String SHOCKED = SessionConfig.session_karakter_name + "SHOCKED";
	private static final String BALLOON = SessionConfig.session_karakter_name + "BALLOON";
	private static final String STRUCTURE = SessionConfig.session_karakter_name + "STRUCTURE";
	private static final String DESC1 = SessionConfig.session_karakter_name + "DESC1";
	private static final String DESC2 = SessionConfig.session_karakter_name + "DESC2";
	private static final String DESC3 = SessionConfig.session_karakter_name + "DESC3";
	private static final String DESC4 = SessionConfig.session_karakter_name + "DESC4";
	private static final String DESC5 = SessionConfig.session_karakter_name + "DESC5";
	private static final String DESC6 = SessionConfig.session_karakter_name + "DESC6";
	private static final String DESC7 = SessionConfig.session_karakter_name + "DESC7";
	private static final String DESC8 = SessionConfig.session_karakter_name + "DESC8";
	private static final String DESC9 = SessionConfig.session_karakter_name + "DESC9";
	public void openSession(final MisiModel misi_model) {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_karakter_name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(ID, misi_model.id);
		editor.putString(NAMA, misi_model.title);
		editor.putString(IMAGE, misi_model.list_icon);
		editor.putString(BADGE, misi_model.badge_icon);
		editor.putString(SMILE, misi_model.smile_icon);
		editor.putString(SAD, misi_model.sad_icon);
		editor.putString(SHOCKED, misi_model.shocked_icon);
		editor.putString(BALLOON, misi_model.balloon_icon);
		editor.putString(STRUCTURE, misi_model.structure_image);
		editor.putString(DESC1, misi_model.description_1);
		editor.putString(DESC2, misi_model.description_2);
		editor.putString(DESC3, misi_model.description_3);
		editor.putString(DESC4, misi_model.description_4);
		editor.putString(DESC5, misi_model.description_5);
		editor.putString(DESC6, misi_model.description_6);
		editor.putString(DESC7, misi_model.description_7);
		editor.putString(DESC8, misi_model.description_8);
		editor.putString(DESC9, misi_model.description_9);
		editor.commit();
	}
	public void closeSession() {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_karakter_name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(ID);
		editor.remove(NAMA);
		editor.remove(IMAGE);
		editor.remove(BADGE);
		editor.remove(SMILE);
		editor.remove(SAD);
		editor.remove(SHOCKED);
		editor.remove(BALLOON);
		editor.remove(STRUCTURE);
		editor.remove(DESC1);
		editor.remove(DESC2);
		editor.remove(DESC3);
		editor.remove(DESC4);
		editor.remove(DESC5);
		editor.remove(DESC6);
		editor.remove(DESC7);
		editor.remove(DESC8);
		editor.remove(DESC9);
		editor.commit();
	}
	public boolean isSession() {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_karakter_name, Context.MODE_PRIVATE);
		if(preferences.getInt(ID, 0) == 0) {
			return true;
		}else {
			return false;
		}
	}
	public final String getFromSession(final String id) {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_karakter_name, Context.MODE_PRIVATE);
		if(GlobalController.isNotNull(preferences.getString(id, Config.text_blank) )) {
			return preferences.getString(id, Config.text_blank);
		}else {
			return Config.text_blank;
		}
	}
	public final int getIntFromSession(final String id) {
		SharedPreferences preferences = AksiKuApp.getAppContext().getSharedPreferences(SessionConfig.session_karakter_name, Context.MODE_PRIVATE);
		return preferences.getInt(id, 0);
	}
	public final int Id() {
		return getIntFromSession(ID);
	}
	public final String Nama() {
		return getFromSession(NAMA);
	}
	public final String Image() {
		return getFromSession(IMAGE);
	}
	public final String Badge() {
		return getFromSession(BADGE);
	}
	public final String Smile() {
		return getFromSession(SMILE);
	}
	public final String Sad() {
		return getFromSession(SAD);
	}
	public final String Shocked() {
		return getFromSession(SHOCKED);
	}
	public final String Balloon() {
		return getFromSession(BALLOON);
	}
	public final String Structure() {
		return getFromSession(STRUCTURE);
	}
	public final String Desc1() {
		return getFromSession(DESC1);
	}
	public final String Desc2() {
		return getFromSession(DESC2);
	}
	public final String Desc3() {
		return getFromSession(DESC3);
	}
	public final String Desc4() {
		return getFromSession(DESC4);
	}
	public final String Desc5() {
		return getFromSession(DESC5);
	}
	public final String Desc6() {
		return getFromSession(DESC6);
	}
	public final String Desc7() {
		return getFromSession(DESC7);
	}
	public final String Desc8() {
		return getFromSession(DESC8);
	}
	public final String Desc9() {
		return getFromSession(DESC9);
	}
}