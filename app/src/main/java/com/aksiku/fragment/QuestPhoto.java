package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.aksiku.AksiKuApp;
import com.aksiku.R;
import com.aksiku.controller.CameraController;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.ImageController;

public class QuestPhoto extends Fragment {
	public static final String TAG = QuestPhoto.class.getCanonicalName();
	public static QuestPhoto instance;
	private CameraController camera_controller;
	private SurfaceView vw_master;
	private FrameLayout lay_camera;
	private Button btn_ok;
	private String picture_string_data;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.quest_photo, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		if(camera_controller != null) {
			camera_controller.close();
		}
		super.onDestroyView();
		if(Quest.instance != null) {
			Quest.instance.closeQuestPhotoFragment(picture_string_data);
		}
		if(Trivia.instance != null) {
			Trivia.instance.closeQuestPhotoFragment(picture_string_data);
		}
		instance = null;
	}
	private void setInitial(final View view) {
		vw_master = (SurfaceView) view.findViewById(R.id.vw_master);
		lay_camera = (FrameLayout) view.findViewById(R.id.lay_camera);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		setEventListener();
		camera_controller = new CameraController(AksiKuApp.getAppContext(), vw_master, false, camera_callback);
		camera_controller.showPreview(lay_camera, false);
	}
	private void setEventListener() {
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(camera_controller != null) {
					camera_controller.take(false);
				}
			}
		});
	}
	private CameraController.CameraCallback camera_callback = new CameraController.CameraCallback() {
		@Override
		public void didGetPicture(byte[] data) {
			picture_string_data = ImageController.getStringFromBitmap(ImageController.getBitmapFromBytes(data), 70);
			GlobalController.pop();
		}
	};
}