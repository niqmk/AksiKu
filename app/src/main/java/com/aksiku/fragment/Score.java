package com.aksiku.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.ImageController;
import com.aksiku.custom.CustomViewPager;
import com.aksiku.general.Config;
import com.aksiku.general.GlobalVariables;
import com.aksiku.general.model.HallOfFameModel;
import com.aksiku.view.ScoreView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Score extends Fragment {
	public static final String TAG = Score.class.getCanonicalName();
	public static Score instance;
	private LayoutInflater inflater;
	private TextView lbl_nama;
	private ImageView img_pp;
	private TextView lbl_poin;
	private TextView lbl_poin_bronze;
	private TextView lbl_poin_silver;
	private TextView lbl_poin_gold;
	private ImageView img_karakter;
	private ImageButton btn_kiri;
	private ImageButton btn_kanan;
	private CustomViewPager vwp_score;
	private ArrayList<View> view_list = new ArrayList<>();
	private ScorePagerAdapter adapter;
	private HallOfFameModel hall_of_fame_model;
	private int index = 0;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.score, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(Map.instance != null) {
			Map.instance.closeScoreFragment();
		}
		instance = null;
	}
	private void setInitial(final View view) {
		img_pp = (ImageView)view.findViewById(R.id.img_pp);
		lbl_nama = (TextView)view.findViewById(R.id.lbl_nama);
		lbl_poin = (TextView)view.findViewById(R.id.lbl_poin);
		lbl_poin_bronze = (TextView)view.findViewById(R.id.lbl_poin_bronze);
		lbl_poin_silver = (TextView)view.findViewById(R.id.lbl_poin_silver);
		lbl_poin_gold = (TextView)view.findViewById(R.id.lbl_poin_gold);
		img_karakter = (ImageView)view.findViewById(R.id.img_karakter);
		btn_kiri = (ImageButton)view.findViewById(R.id.btn_kiri);
		btn_kanan = (ImageButton)view.findViewById(R.id.btn_kanan);
		vwp_score = (CustomViewPager)view.findViewById(R.id.vwp_score);
		setEventListener();
		populasiData();
	}
	private void setEventListener() {
		btn_kiri.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doKiri();
			}
		});
		btn_kanan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doKanan();
			}
		});
	}
	private void doKiri() {
		if(index <= 0) {
			return;
		}
		index--;
		vwp_score.setCurrentItem(index);
	}
	private void doKanan() {
		if(index >= (view_list.size() - 1)) {
			return;
		}
		index++;
		vwp_score.setCurrentItem(index);
	}
	private void populasiData() {
		final int bronze = getArguments().getInt("bronze");
		final int silver = getArguments().getInt("silver");
		final int gold = getArguments().getInt("gold");
		final int total = getArguments().getInt("total");
		img_pp.setImageBitmap(ImageController.getBitmapFromString(GlobalVariables.user_session.PP()));
		lbl_nama.setText(GlobalVariables.user_session.Nama());
		lbl_poin.setText(Config.text_blank + total);
		lbl_poin_bronze.setText(Config.text_blank + bronze);
		lbl_poin_silver.setText(Config.text_blank + silver);
		lbl_poin_gold.setText(Config.text_blank + gold);
		ImageLoader.getInstance().displayImage(GlobalVariables.karakter_session.Badge(), img_karakter, GlobalController.getOption(true, true));
				GlobalController.showLoading(Main.instance);
		final RequestQueue queue = Volley.newRequestQueue(Main.instance);
		final String url = Config.url_server + "hall-of-fame?expand=player";
		final StringRequest request = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						GlobalController.closeLoading();
						hall_of_fame_model = new HallOfFameModel(response);
						setLayout();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError error) {
				GlobalController.closeLoading();
				if(error.networkResponse == null) {
					GlobalController.showToast(R.string.error_no_internet);
					return;
				}
				final int status_code = error.networkResponse.statusCode;
				final NetworkResponse response = error.networkResponse;
				if(status_code == 400) {
					try {
						final String value = new String(response.data, "UTF-8");
						final JSONObject json = new JSONObject(value);
						GlobalController.showToast(json.getString("message"));
					}catch(JSONException ex) {
					}catch(UnsupportedEncodingException ex) {}
					return;
				}
				if(Main.instance == null) {
					return;
				}
				Main.instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.showToast(error.getMessage());
					}
				});
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
	}
	private void setLayout() {
		for(int counter = 0; counter < hall_of_fame_model.hall_of_fame_list.size(); counter += 10) {
			final ArrayList<HallOfFameModel> hall_of_fame_list = new ArrayList<>();
			for(int hof_counter = counter; hof_counter < counter + 10; hof_counter++) {
				if(hof_counter < hall_of_fame_model.hall_of_fame_list.size()) {
					hall_of_fame_list.add(hall_of_fame_model.hall_of_fame_list.get(hof_counter));
				}
			}
			final ScoreView score_view = new ScoreView(inflater, counter, hall_of_fame_list);
			view_list.add(score_view.getView());
		}
		adapter = new ScorePagerAdapter();
		vwp_score.setAdapter(adapter);
		vwp_score.setScrollDurationFactor(3.0);
		vwp_score.setCurrentItem(0);
	}
	private class ScorePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return view_list.size();
		}
		@Override
		public Object instantiateItem(ViewGroup collection, int position) {
			collection.addView(view_list.get(position), 0);
			return view_list.get(position);
		}
		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
			collection.removeView(view_list.get(position));
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}
		@Override
		public void finishUpdate(ViewGroup arg0) {}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override
		public Parcelable saveState() {
			return null;
		}
		@Override
		public void startUpdate(ViewGroup arg0) {}
	}
}