package com.aksiku.general.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HallOfFameModel {
	public int id;
	public long bronze;
	public long silver;
	public long gold;
	public long total;
	public String nama;
	public String profile_picture;
	public ArrayList<HallOfFameModel> hall_of_fame_list;
	public HallOfFameModel(final String response) {
		try {
			final JSONObject json = new JSONObject(response);
			final JSONArray json_array = json.getJSONArray("data");
			hall_of_fame_list = new ArrayList<>();
			for(int counter = 0; counter < json_array.length(); counter++) {
				final HallOfFameModel hall_of_fame_model = new HallOfFameModel(json_array.getJSONObject(counter));
				hall_of_fame_list.add(hall_of_fame_model);
			}
		}catch(JSONException ex) {}
	}
	public HallOfFameModel(final JSONObject json) {
		try {
			id = json.getInt("id");
			bronze = json.getLong("bronze");
			silver = json.getLong("silver");
			gold = json.getLong("gold");
			total = json.getLong("total");
			final JSONObject player = json.getJSONObject("player");
			nama = player.getString("username");
			if(player.get("profile_picture") != JSONObject.NULL) {
				profile_picture = player.getString("profile_picture");
			}
		}catch(JSONException ex) {}
	}
}