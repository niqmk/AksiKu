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
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.ImageController;
import com.aksiku.general.Config;
import com.aksiku.general.model.JawabanQuestModel;
import com.aksiku.general.model.QuestModel;

import java.util.Timer;
import java.util.TimerTask;

public class Trivia extends Fragment {
	public static final String TAG = Trivia.class.getCanonicalName();
	public static Trivia instance;
	private QuestPhoto quest_photo_fragment;
	private ViewSwitcher switcher;
	private TextView lbl_quest;
	private TextView lbl_acuan;
	private ImageButton btn_kamera;
	private TextView lbl_monyet;
	private ImageButton btn_monyet;
	private Button btn_ok;
	private Timer tmr_switcher;
	private String string_data = Config.text_blank;
	private QuestModel quest_model;
	private JawabanQuestModel jawaban_quest_model;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.trivia, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		cancelTimer(false);
		if(Map.instance != null) {
			Map.instance.closeTriviaFragment(jawaban_quest_model);
		}
		instance = null;
	}
	public void closeQuestPhotoFragment(final String string_data) {
		this.string_data = string_data;
		quest_photo_fragment = null;
		setKamera(string_data);
	}
	private void setInitial(final View view) {
		switcher = (ViewSwitcher)view.findViewById(R.id.switcher);
		lbl_quest = (TextView)view.findViewById(R.id.lbl_quest);
		lbl_acuan = (TextView)view.findViewById(R.id.lbl_acuan);
		btn_kamera = (ImageButton)view.findViewById(R.id.btn_kamera);
		lbl_monyet = (TextView)view.findViewById(R.id.lbl_monyet);
		btn_monyet = (ImageButton)view.findViewById(R.id.btn_monyet);
		btn_ok = (Button)view.findViewById(R.id.btn_ok);
		setEventListener();
		populasiData();
	}
	private void setEventListener() {
		switcher.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelTimer(true);
			}
		});
		btn_kamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doKamera();
			}
		});
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doOK();
			}
		});
		tmr_switcher = new Timer();
		tmr_switcher.schedule(new TimerTask() {
			@Override
			public void run() {
				if (Main.instance == null) {
					return;
				}
				Main.instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (switcher.getDisplayedChild() == 0) {
							switcher.showNext();
						} else {
							switcher.showPrevious();
						}
					}
				});
			}
		}, 1000, 1000);
	}
	private void doKamera() {
		if(quest_photo_fragment != null) {
			return;
		}
		quest_photo_fragment = new QuestPhoto();
		GlobalController.push(quest_photo_fragment, QuestPhoto.TAG);
	}
	private void doOK() {
		if(!GlobalController.isNotNull(string_data)) {
			GlobalController.showAlert(Main.instance, "Warning", "Gunakan kamera untuk menangkap poto", "OK", null);
			return;
		}
		jawaban_quest_model = new JawabanQuestModel();
		jawaban_quest_model.quest_id = quest_model.id;
		jawaban_quest_model.point = quest_model.game_point;
		jawaban_quest_model.photo = string_data;
		GlobalController.pop();
	}
	private void populasiData() {
		quest_model = getArguments().getParcelable(QuestModel.TAG);
		if(quest_model.type.equals(QuestModel.QuestTrivia)) {
			lbl_quest.setText(GlobalController.getString(R.string.label_text_quest_trivia));
		}else if(quest_model.type.equals(QuestModel.QuestFun)) {
			lbl_quest.setText(GlobalController.getString(R.string.label_text_quest_fun));
		}
		lbl_acuan.setText(quest_model.quest_directions);
	}
	private void setKamera(final String string_data) {
		if(!GlobalController.isNotNull(string_data)) {
			return;
		}
		if(Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btn_kamera.setImageBitmap(ImageController.getBitmapFromString(string_data));
			}
		});
	}
	private void cancelTimer(final boolean update) {
		if(tmr_switcher == null) {
			return;
		}
		tmr_switcher.cancel();
		tmr_switcher = null;
		if(!update) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switcher.setVisibility(View.GONE);
				btn_monyet.setImageResource(R.drawable.ic_monyet_balon);
				lbl_monyet.setText(quest_model.monkey_buble);
				btn_monyet.setVisibility(View.VISIBLE);
				btn_kamera.bringToFront();
			}
		});
	}
}