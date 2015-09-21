package com.aksiku.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aksiku.R;
import com.aksiku.controller.GlobalController;
import com.aksiku.general.model.JawabanQuestModel;

public class TriviaBonus extends Fragment {
	public static final String TAG = TriviaBonus.class.getCanonicalName();
	public static TriviaBonus instance;
	private TextView lbl_acuan;
	private Button btn_ok;
	private JawabanQuestModel jawaban_quest_model;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.trivia_bonus, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(Map.instance != null) {
			Map.instance.closeTriviaBonusFragment(jawaban_quest_model);
		}
		instance = null;
	}
	private void setInitial(final View view) {
		lbl_acuan = (TextView)view.findViewById(R.id.lbl_acuan);
		btn_ok = (Button)view.findViewById(R.id.btn_ok);
		setEventListener();
		populasiData();
	}
	private void setEventListener() {
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doOK();
			}
		});
	}
	private void populasiData() {
		lbl_acuan.setText(String.format(GlobalController.getString(R.string.label_quest_bonus), 10));
	}
	private void doOK() {
		jawaban_quest_model = new JawabanQuestModel();
		jawaban_quest_model.quest_id = 0;
		jawaban_quest_model.point = 10;
		GlobalController.pop();
	}
}