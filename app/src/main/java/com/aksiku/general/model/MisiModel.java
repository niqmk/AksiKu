package com.aksiku.general.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MisiModel {
	public int id;
	public String title;
	public String list_icon;
	public String badge_icon;
	public String smile_icon;
	public String sad_icon;
	public String shocked_icon;
	public String balloon_icon;
	public String structure_image;
	public String description_1;
	public String description_2;
	public String description_3;
	public String description_4;
	public String description_5;
	public String description_6;
	public String description_7;
	public String description_8;
	public String description_9;
	public ArrayList<MisiModel> misi_list;
	public MisiModel(final String response) {
		try {
			final JSONObject json = new JSONObject(response);
			final JSONArray json_array = json.getJSONArray("data");
			misi_list = new ArrayList<>();
			for(int counter = 0; counter < json_array.length(); counter++) {
				final MisiModel misi_model = new MisiModel(json_array.getJSONObject(counter));
				misi_list.add(misi_model);
			}
		}catch(JSONException ex) {}
	}
	public MisiModel(final JSONObject json) {
		try {
			id = json.getInt("id");
			title = json.getString("title");
			list_icon = json.getString("list_icon");
			badge_icon = json.getString("badge_icon");
			smile_icon = json.getString("smile_icon");
			sad_icon = json.getString("sad_icon");
			if(json.get("shocked_icon") != JSONObject.NULL) {
				shocked_icon = json.getString("shocked_icon");
			}
			balloon_icon = json.getString("balloon_icon");
			structure_image = json.getString("structure_image");
			description_1 = json.getString("description_1");
			description_2 = json.getString("description_2");
			description_3 = json.getString("description_3");
			description_4 = json.getString("description_4");
			description_5 = json.getString("description_5");
			description_6 = json.getString("description_6");
			description_7 = json.getString("description_7");
			description_8 = json.getString("description_8");
			//description_9 = json.getString("description_9");
		}catch(JSONException ex) {}
	}
}