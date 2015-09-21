package com.aksiku.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aksiku.R;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.ImageController;
import com.aksiku.general.model.MisiPhotosModel;

import java.util.ArrayList;

public class GaleriView {
	private final LayoutInflater inflater;
	private final int quest;
	private final ArrayList<MisiPhotosModel> misi_photos_list;
	private View view;
	private LinearLayout lay_galeri;
	public GaleriView(final LayoutInflater inflater, final int quest, final ArrayList<MisiPhotosModel> misi_photos_list) {
		this.inflater = inflater;
		this.quest = quest;
		this.misi_photos_list = new ArrayList<>(misi_photos_list);
		setInitial();
	}
	public View getView() {
		return view;
	}
	private void setInitial() {
		view = inflater.inflate(R.layout.galeri_view, null, false);
		lay_galeri = (LinearLayout)view.findViewById(R.id.lay_galeri);
		populasiData();
	}
	private void populasiData() {
		lay_galeri.removeAllViews();
		for(int counter = 0; counter < misi_photos_list.size(); counter += 3) {
			final View galeri_photo_view = inflater.inflate(R.layout.galeri_photo, null, false);
			final MisiPhotosModel misi_photos_model_1 = misi_photos_list.get(counter);
			setLayout(galeri_photo_view, (counter % 3) + 1, misi_photos_model_1);
			if((counter + 1) < misi_photos_list.size()) {
				final MisiPhotosModel misi_photos_model_2 = misi_photos_list.get(counter + 1);
				setLayout(galeri_photo_view, ((counter) % 3) + 2, misi_photos_model_2);
			}else {
				final LinearLayout lay_galeri_view = (LinearLayout)galeri_photo_view.findViewById(R.id.lay_galeri_2);
				lay_galeri_view.setVisibility(View.INVISIBLE);
			}
			if((counter + 2) < misi_photos_list.size()) {
				final MisiPhotosModel misi_photos_model_3 = misi_photos_list.get(counter + 2);
				setLayout(galeri_photo_view, ((counter) % 3) + 3, misi_photos_model_3);
			}else {
				final LinearLayout lay_galeri_view = (LinearLayout)galeri_photo_view.findViewById(R.id.lay_galeri_3);
				lay_galeri_view.setVisibility(View.INVISIBLE);
			}
			lay_galeri.addView(galeri_photo_view);
		}
	}
	private void setLayout(final View galeri_photo_view, final int counter, final MisiPhotosModel misi_photos_model) {
		TextView lbl_quest;
		ImageView img_galeri;
		TextView lbl_galeri_1;
		TextView lbl_galeri_2;
		TextView lbl_galeri_3;
		if(counter == 1) {
			lbl_quest = (TextView)galeri_photo_view.findViewById(R.id.lbl_quest_1);
			img_galeri = (ImageView)galeri_photo_view.findViewById(R.id.img_galeri_1);
			lbl_galeri_1 = (TextView)galeri_photo_view.findViewById(R.id.lbl_answer_1_1);
			lbl_galeri_2 = (TextView)galeri_photo_view.findViewById(R.id.lbl_answer_1_2);
			lbl_galeri_3 = (TextView)galeri_photo_view.findViewById(R.id.lbl_answer_1_3);
		}else if(counter == 2) {
			lbl_quest = (TextView)galeri_photo_view.findViewById(R.id.lbl_quest_2);
			img_galeri = (ImageView)galeri_photo_view.findViewById(R.id.img_galeri_2);
			lbl_galeri_1 = (TextView)galeri_photo_view.findViewById(R.id.lbl_answer_2_1);
			lbl_galeri_2 = (TextView)galeri_photo_view.findViewById(R.id.lbl_answer_2_2);
			lbl_galeri_3 = (TextView)galeri_photo_view.findViewById(R.id.lbl_answer_2_3);
		}else if(counter == 3) {
			lbl_quest = (TextView)galeri_photo_view.findViewById(R.id.lbl_quest_3);
			img_galeri = (ImageView)galeri_photo_view.findViewById(R.id.img_galeri_3);
			lbl_galeri_1 = (TextView)galeri_photo_view.findViewById(R.id.lbl_answer_3_1);
			lbl_galeri_2 = (TextView)galeri_photo_view.findViewById(R.id.lbl_answer_3_2);
			lbl_galeri_3 = (TextView)galeri_photo_view.findViewById(R.id.lbl_answer_3_3);
		}else {
			return;
		}
		lbl_quest.setText("Quest " + quest);
		if(GlobalController.isNotNull(misi_photos_model.photo)) {
			img_galeri.setImageBitmap(ImageController.getBitmapFromString(misi_photos_model.photo));
		}
		if(GlobalController.isNotNull(misi_photos_model.answer_1)) {
			lbl_galeri_1.setText(misi_photos_model.answer_1);
		}else {
			lbl_galeri_1.setVisibility(View.INVISIBLE);
		}
		if(GlobalController.isNotNull(misi_photos_model.answer_2)) {
			lbl_galeri_2.setText(misi_photos_model.answer_2);
		}else {
			lbl_galeri_2.setVisibility(View.INVISIBLE);
		}
		if(GlobalController.isNotNull(misi_photos_model.answer_3)) {
			lbl_galeri_3.setText(misi_photos_model.answer_3);
		} else {
			lbl_galeri_3.setVisibility(View.INVISIBLE);
		}
	}
}