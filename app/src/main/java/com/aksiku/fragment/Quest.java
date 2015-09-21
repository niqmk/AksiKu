package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class Quest extends Fragment {
	public static final String TAG = Quest.class.getCanonicalName();
	public static Quest instance;
	private LayoutInflater inflater;
	private QuestPhoto quest_photo_fragment;
	private ViewSwitcher switcher;
	private TextView lbl_quest;
	private TextView lbl_acuan;
	private ImageButton btn_monyet;
	private TextView lbl_monyet;
	private ImageButton btn_kamera;
	private LinearLayout lay_pertanyaan;
	private Button btn_ok;
	private Timer tmr_switcher;
	private QuestModel quest_model;
	private JawabanQuestModel jawaban_quest_model;
	private String string_data = Config.text_blank;
	private boolean success = false;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.quest, container, false);
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
			Map.instance.closeQuestFragment(jawaban_quest_model, success);
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
		btn_monyet = (ImageButton)view.findViewById(R.id.btn_monyet);
		lbl_monyet = (TextView)view.findViewById(R.id.lbl_monyet);
		btn_kamera = (ImageButton)view.findViewById(R.id.btn_kamera);
		lay_pertanyaan = (LinearLayout)view.findViewById(R.id.lay_pertanyaan);
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
		String jawaban_1 = Config.text_blank;
		String jawaban_2 = Config.text_blank;
		String jawaban_3 = Config.text_blank;
		if(GlobalController.isNotNull(quest_model.question_1)) {
			final EditText edit_text = (EditText)lay_pertanyaan.findViewWithTag(1);
			final String jawaban = edit_text.getText().toString();
			if((GlobalController.isNotNull(jawaban))) {
				jawaban_1 = jawaban;
			}else {
				GlobalController.showAlert(Main.instance, "Warning", "Isi semua pertanyaan", "OK", null);
				return;
			}
		}
		if(GlobalController.isNotNull(quest_model.question_2)) {
			final EditText edit_text = (EditText)lay_pertanyaan.findViewWithTag(2);
			final String jawaban = edit_text.getText().toString();
			if((GlobalController.isNotNull(jawaban))) {
				jawaban_2 = jawaban;
			}else {
				GlobalController.showAlert(Main.instance, "Warning", "Isi semua pertanyaan", "OK", null);
				return;
			}
		}
		if(GlobalController.isNotNull(quest_model.question_3)) {
			final EditText edit_text = (EditText)lay_pertanyaan.findViewWithTag(3);
			final String jawaban = edit_text.getText().toString();
			if((GlobalController.isNotNull(jawaban))) {
				jawaban_3 = jawaban;
			}else {
				GlobalController.showAlert(Main.instance, "Warning", "Isi semua pertanyaan", "OK", null);
				return;
			}
		}
		if(!GlobalController.isNotNull(string_data)) {
			GlobalController.showAlert(Main.instance, "Warning", "Gunakan kamera untuk menangkap poto", "OK", null);
			return;
		}
		jawaban_quest_model = new JawabanQuestModel();
		jawaban_quest_model.quest_id = quest_model.id;
		jawaban_quest_model.answer_1 = jawaban_1;
		jawaban_quest_model.answer_2 = jawaban_2;
		jawaban_quest_model.answer_3 = jawaban_3;
		jawaban_quest_model.photo = string_data;
		jawaban_quest_model.point = quest_model.game_point;
		GlobalController.pop();
	}
	private void populasiData() {
		quest_model = getArguments().getParcelable(QuestModel.TAG);
		final int quest_counter = getArguments().getInt("counter");
		success = getArguments().getBoolean("success");
		lbl_quest.setText("Quest " + quest_counter);
		lbl_acuan.setText(quest_model.quest_directions);
		if(GlobalController.isNotNull(quest_model.question_1)) {
			lay_pertanyaan.addView(getPertanyaan(1));
		}
		if(GlobalController.isNotNull(quest_model.question_2)) {
			lay_pertanyaan.addView(getPertanyaan(2));
		}
		if(GlobalController.isNotNull(quest_model.question_3)) {
			lay_pertanyaan.addView(getPertanyaan(3));
		}
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
	private View getPertanyaan(final int index) {
		final View quest_pertanyaan_view = inflater.inflate(R.layout.quest_pertanyaan, null, false);
		final TextView lbl_pertanyaan = (TextView)quest_pertanyaan_view.findViewById(R.id.lbl_pertanyaan);
		final EditText txt_jawaban = (EditText)quest_pertanyaan_view.findViewById(R.id.txt_jawaban);
		if(index == 1) {
			lbl_pertanyaan.setText(quest_model.question_1);
		}else if(index == 2) {
			lbl_pertanyaan.setText(quest_model.question_2);
		}else if(index == 3) {
			lbl_pertanyaan.setText(quest_model.question_3);
		}
		txt_jawaban.setTag(index);
		return quest_pertanyaan_view;
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
}