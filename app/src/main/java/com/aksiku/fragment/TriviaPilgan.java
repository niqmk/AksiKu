package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.controller.GlobalController;
import com.aksiku.general.Config;
import com.aksiku.general.model.JawabanQuestModel;
import com.aksiku.general.model.QuestModel;

import java.util.Timer;
import java.util.TimerTask;

public class TriviaPilgan extends Fragment {
	public static final String TAG = TriviaPilgan.class.getCanonicalName();
	public static TriviaPilgan instance;
	private LayoutInflater inflater;
	private ViewSwitcher switcher;
	private TextView lbl_quest;
	private TextView lbl_monyet;
	private ImageButton btn_monyet;
	private TextView lbl_acuan;
	private LinearLayout lay_pilihan;
	private Button btn_ok;
	private Timer tmr_switcher;
	private QuestModel quest_model;
	private JawabanQuestModel jawaban_quest_model;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.trivia_pilgan, container, false);
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
			Map.instance.closeTriviaPilganFragment(jawaban_quest_model);
		}
		instance = null;
	}
	private void setInitial(final View view) {
		switcher = (ViewSwitcher)view.findViewById(R.id.switcher);
		lbl_quest = (TextView)view.findViewById(R.id.lbl_quest);
		lbl_monyet = (TextView)view.findViewById(R.id.lbl_monyet);
		btn_monyet = (ImageButton)view.findViewById(R.id.btn_monyet);
		lbl_acuan = (TextView)view.findViewById(R.id.lbl_acuan);
		lay_pilihan = (LinearLayout)view.findViewById(R.id.lay_pilihan);
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
	private void doOK() {
		String jawaban_1 = Config.text_blank;
		String jawaban_2 = Config.text_blank;
		String jawaban_3 = Config.text_blank;
		if(GlobalController.isNotNull(quest_model.question_1)) {
			final RadioButton rad_pilihan = (RadioButton)lay_pilihan.findViewWithTag(1);
			if(rad_pilihan.isChecked()) {
				jawaban_1 = Config.text_ok;
			}
		}
		if(GlobalController.isNotNull(quest_model.question_2)) {
			final RadioButton rad_pilihan = (RadioButton)lay_pilihan.findViewWithTag(2);
			if(rad_pilihan.isChecked()) {
				jawaban_2 = Config.text_ok;
			}
		}
		if(GlobalController.isNotNull(quest_model.question_3)) {
			final RadioButton rad_pilihan = (RadioButton)lay_pilihan.findViewWithTag(3);
			if(rad_pilihan.isChecked()) {
				jawaban_3 = Config.text_ok;
			}
		}
		if((!GlobalController.isNotNull(jawaban_1) && (!GlobalController.isNotNull(jawaban_2) && (!GlobalController.isNotNull(jawaban_3))))) {
			GlobalController.showAlert(Main.instance, "Warning", "Pilih salah satu jawaban", "OK", null);
			return;
		}
		jawaban_quest_model = new JawabanQuestModel();
		jawaban_quest_model.quest_id = quest_model.id;
		jawaban_quest_model.answer_1 = jawaban_1;
		jawaban_quest_model.answer_2 = jawaban_2;
		jawaban_quest_model.answer_3 = jawaban_3;
		GlobalController.pop();
	}
	private void populasiData() {
		quest_model = getArguments().getParcelable(QuestModel.TAG);
		lbl_acuan.setText(quest_model.quest_directions);
		if(GlobalController.isNotNull(quest_model.question_1)) {
			lay_pilihan.addView(getPilihan(1));
		}
		if(GlobalController.isNotNull(quest_model.question_2)) {
			lay_pilihan.addView(getPilihan(2));
		}
		if(GlobalController.isNotNull(quest_model.question_3)) {
			lay_pilihan.addView(getPilihan(3));
		}
	}
	private View getPilihan(final int index) {
		final View quest_pilgan_view = inflater.inflate(R.layout.quest_pilgan, null, false);
		final RadioButton rad_pilihan = (RadioButton)quest_pilgan_view.findViewById(R.id.rad_pilihan);
		final TextView lbl_pilihan = (TextView)quest_pilgan_view.findViewById(R.id.lbl_pilihan);
		if(index == 1) {
			lbl_pilihan.setText(quest_model.question_1);
		}else if(index == 2) {
			lbl_pilihan.setText(quest_model.question_2);
		}else if(index == 3) {
			lbl_pilihan.setText(quest_model.question_3);
		}
		rad_pilihan.setTag(index);
		rad_pilihan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					int childcount = lay_pilihan.getChildCount();
					for (int i = 0; i < childcount; i++) {
						final View v = lay_pilihan.getChildAt(i);
						final RadioButton rad_button = (RadioButton)v.findViewById(R.id.rad_pilihan);
						if(Integer.parseInt(rad_button.getTag().toString()) != index) {
							rad_button.setChecked(false);
						}
					}
				}
			}
		});
		return quest_pilgan_view;
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
			}
		});
	}
}