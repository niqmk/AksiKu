package com.aksiku.controller;

import android.media.MediaPlayer;

import com.aksiku.AksiKuApp;
import com.aksiku.R;

public class MusicController {
	private static MediaPlayer player;
	public static void play() {
		player = MediaPlayer.create(AksiKuApp.getAppContext(), R.raw.bg_music);
		player.setLooping(true);
		player.setVolume(150, 150);
		player.start();
	}
	public static void stop() {
		if(player == null) {
			return;
		}
		if(player.isPlaying()) {
			player.stop();
		}
	}
}