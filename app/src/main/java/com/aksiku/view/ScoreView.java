package com.aksiku.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aksiku.R;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.ImageController;
import com.aksiku.general.Config;
import com.aksiku.general.model.HallOfFameModel;

import java.util.ArrayList;

public class ScoreView {
	private final LayoutInflater inflater;
	private final int index;
	private final ArrayList<HallOfFameModel> hall_of_fame_list;
	private View view;
	private LinearLayout lay_score;
	public ScoreView(final LayoutInflater inflater, final int index, final ArrayList<HallOfFameModel> hall_of_fame_list) {
		this.inflater = inflater;
		this.index = index;
		this.hall_of_fame_list = new ArrayList<>(hall_of_fame_list);
		setInitial();
	}
	public View getView() {
		return view;
	}
	private void setInitial() {
		view = inflater.inflate(R.layout.score_view, null, false);
		lay_score = (LinearLayout)view.findViewById(R.id.lay_score);
		populasiData();
	}
	private void populasiData() {
		lay_score.removeAllViews();
		final View score_list = inflater.inflate(R.layout.score_list, null, false);
		final LinearLayout lay_kiri = (LinearLayout)score_list.findViewById(R.id.lay_kiri);
		final LinearLayout lay_kanan = (LinearLayout)score_list.findViewById(R.id.lay_kanan);
		for(int counter = index; counter - index < hall_of_fame_list.size(); counter++) {
			final View score_list_view = inflater.inflate(R.layout.score_list_view, null, false);
			final HorizontalScrollView scv_score = (HorizontalScrollView)score_list_view.findViewById(R.id.scv_score);
			final HallOfFameModel hall_of_fame_model = hall_of_fame_list.get(counter - index);
			setLayout(score_list_view, counter - index, hall_of_fame_model);
			if((counter - index) <= 5) {
				lay_kiri.setVisibility(View.VISIBLE);
				lay_kiri.addView(score_list_view);
				scv_score.setVisibility(View.VISIBLE);
			}else {
				lay_kanan.setVisibility(View.VISIBLE);
				lay_kanan.addView(score_list_view);
				scv_score.setVisibility(View.VISIBLE);
			}
		}
		lay_score.addView(score_list);
	}
	private void setLayout(final View score_list_view, final int counter, final HallOfFameModel hall_of_fame_model) {
		final TextView lbl_no = (TextView)score_list_view.findViewById(R.id.lbl_no);
		final TextView lbl_total_poin = (TextView)score_list_view.findViewById(R.id.lbl_total_poin);
		final ImageView img_pp = (ImageView)score_list_view.findViewById(R.id.img_pp);
		final TextView lbl_nama = (TextView)score_list_view.findViewById(R.id.lbl_nama);
		final TextView lbl_poin_bronze = (TextView)score_list_view.findViewById(R.id.lbl_poin_bronze);
		final TextView lbl_poin_silver = (TextView)score_list_view.findViewById(R.id.lbl_poin_silver);
		final TextView lbl_poin_gold = (TextView)score_list_view.findViewById(R.id.lbl_poin_gold);
		lbl_no.setText(Config.text_blank + (counter + 1));
		lbl_total_poin.setText(Config.text_blank + hall_of_fame_model.total);
		lbl_nama.setText(hall_of_fame_model.nama);
		lbl_poin_bronze.setText(Config.text_blank + hall_of_fame_model.bronze);
		lbl_poin_silver.setText(Config.text_blank + hall_of_fame_model.silver);
		lbl_poin_gold.setText(Config.text_blank + hall_of_fame_model.gold);
		if(GlobalController.isNotNull(hall_of_fame_model.profile_picture)) {
			img_pp.setImageBitmap(ImageController.getBitmapFromString(hall_of_fame_model.profile_picture));
		}
	}
}