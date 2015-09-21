package com.aksiku.general.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestModel implements Parcelable {
	public static final String TAG = QuestModel.class.getCanonicalName();
	public static final String QuestNormal = "normal";
	public static final String QuestFun = "fun";
	public static final String QuestBonus = "bonus";
	public static final String QuestTrivia = "trivia";
	public int id;
	public String title;
	public String quest_directions;
	public String monkey_buble;
	public String question_1;
	public String question_2;
	public String question_3;
	public String type;
	public int game_point;
	public ArrayList<QuestModel> quest_list;
	public QuestModel(final String response) {
		try {
			final JSONObject json = new JSONObject(response);
			final JSONArray json_array = json.getJSONArray("quests");
			quest_list = new ArrayList<>();
			for(int counter = 0; counter < json_array.length(); counter++) {
				final QuestModel quest_model = new QuestModel(json_array.getJSONObject(counter));
				quest_list.add(quest_model);
			}
		}catch(JSONException ex) {}
	}
	public QuestModel(final JSONObject json) {
		try {
			id = json.getInt("id");
			title = json.getString("title");
			quest_directions = json.getString("quest_directions");
			monkey_buble = json.getString("monkey_buble");
			if(json.get("question_1") != JSONObject.NULL) {
				question_1 = json.getString("question_1");
			}
			if(json.get("question_2") != JSONObject.NULL) {
				question_2 = json.getString("question_2");
			}
			if(json.get("question_3") != JSONObject.NULL) {
				question_3 = json.getString("question_3");
			}
			type = json.getString("type");
			game_point = json.getInt("game_point");
		}catch(JSONException ex) {}
	}
	public QuestModel(Parcel in) {
		id = in.readInt();
		title = in.readString();
		quest_directions = in.readString();
		monkey_buble = in.readString();
		question_1 = in.readString();
		question_2 = in.readString();
		question_3 = in.readString();
		type = in.readString();
		game_point = in.readInt();
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(quest_directions);
		dest.writeString(monkey_buble);
		dest.writeString(question_1);
		dest.writeString(question_2);
		dest.writeString(question_3);
		dest.writeString(type);
		dest.writeInt(game_point);
	}
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		@Override
		public QuestModel createFromParcel(Parcel in) {
			return new QuestModel(in);
		}
		@Override
		public QuestModel[] newArray(int size) {
			return new QuestModel[size];
		}
	};
}