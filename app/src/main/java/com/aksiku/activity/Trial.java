package com.aksiku.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.aksiku.R;

public class Trial extends Activity {
	private VideoView video;
	private ImageButton btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trial);
		setInitial();
	}
	@Override
	protected void onDestroy() {
		if(video != null) {
			video.stopPlayback();
		}
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(video != null) {
			video.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.trial);
			video.start();
			video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.setLooping(false);
				}
			});
		}
	}
	private void setInitial() {
		video = (VideoView)findViewById(R.id.video);
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}