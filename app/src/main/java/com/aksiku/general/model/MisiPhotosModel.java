package com.aksiku.general.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MisiPhotosModel implements Parcelable {
	public int id;
	public int quest_id;
	public String answer_1;
	public String answer_2;
	public String answer_3;
	public double latitude;
	public double longitude;
	public String photo;
	public ArrayList<MisiPhotosModel> misi_photos_list;
	public MisiPhotosModel(final String response) {
		try {
			final JSONArray json_array = new JSONArray(response);
			misi_photos_list = new ArrayList<>();
			for(int counter = 0; counter < json_array.length(); counter++) {
				final MisiPhotosModel misi_photos_model = new MisiPhotosModel(json_array.getJSONObject(counter));
				misi_photos_list.add(misi_photos_model);
			}
		}catch(JSONException ex) {}
	}
	public MisiPhotosModel(final JSONObject json) {
		try {
			id = json.getInt("id");
			quest_id = json.getInt("quest_id");
			answer_1 = json.getString("answer_1");
			answer_2 = json.getString("answer_2");
			answer_3 = json.getString("answer_3");
			latitude = json.getDouble("latitude");
			longitude = json.getDouble("longitude");
			photo = json.getString("photo");
		}catch(JSONException ex) {}
	}
	public MisiPhotosModel(final JawabanQuestModel jawaban_quest_model) {
		id = 0;
		quest_id = jawaban_quest_model.quest_id;
		answer_1 = jawaban_quest_model.answer_1;
		answer_2 = jawaban_quest_model.answer_2;
		answer_3 = jawaban_quest_model.answer_3;
		latitude = jawaban_quest_model.latitude;
		longitude = jawaban_quest_model.longitude;
		photo = jawaban_quest_model.photo;
	}
	public MisiPhotosModel(Parcel in) {
		id = in.readInt();
		quest_id = in.readInt();
		answer_1 = in.readString();
		answer_2 = in.readString();
		answer_3 = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		photo = in.readString();
		in.readTypedList(misi_photos_list, MisiPhotosModel.CREATOR);
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(quest_id);
		dest.writeString(answer_1);
		dest.writeString(answer_2);
		dest.writeString(answer_3);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(photo);
		dest.writeTypedList(misi_photos_list);
	}
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		@Override
		public MisiPhotosModel createFromParcel(Parcel in) {
			return new MisiPhotosModel(in);
		}
		@Override
		public MisiPhotosModel[] newArray(int size) {
			return new MisiPhotosModel[size];
		}
	};
}