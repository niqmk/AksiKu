package com.aksiku.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aksiku.AksiKuApp;
import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.controller.AppController;
import com.aksiku.controller.GlobalController;
import com.aksiku.controller.ImageController;
import com.aksiku.controller.LocationController;
import com.aksiku.general.Config;
import com.aksiku.general.GlobalVariables;
import com.aksiku.general.model.JawabanQuestModel;
import com.aksiku.general.model.MisiPhotosModel;
import com.aksiku.general.model.QuestModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Map extends Fragment implements LocationController.LocationCallback {
	public static final String TAG = Map.class.getCanonicalName();
	public static Map instance;
	private enum MapState {
		NoneState,
		QuestState,
		PinState
	}
	private final int trivia = 5;
	private final int fun = 3;
	private final int bonus_multiple_1 = 2;
	private final int bonus_multiple_2 = 4;
	private final int bonus = 6;
	private boolean trivia_ok = false;
	private boolean fun_ok = false;
	private boolean bonus_multiple_1_ok = false;
	private boolean bonus_multiple_2_ok = false;
	private boolean bonus_ok = false;
	private Score score_fragment;
	private Quest quest_fragment;
	private Galeri galeri_fragment;
	private TambahPin tambah_pin_fragment;
	private Trivia trivia_fragment;
	private TriviaPilgan trivia_pilgan_fragment;
	private TriviaBonus trivia_bonus_fragment;
	private LayoutInflater inflater;
	private SupportMapFragment support_map_fragment;
	private GoogleMap map;
	private ImageButton btn_refresh;
	private Button btn_map_score;
	private ImageButton btn_user_online;
	private TextView lbl_user_online;
	private TextView lbl_poin_bronze;
	private TextView lbl_poin_silver;
	private TextView lbl_poin_gold;
	private ImageView img_karakter;
	private ImageView img_misi;
	private ImageView img_pp;
	private Button btn_nama;
	private LinearLayout lay_quest;
	private Button btn_galeri;
	private Button btn_trivia;
	private Button btn_selesai;
	private LocationController location_controller = new LocationController(AksiKuApp.getAppContext());
	private float map_latitude = -6.93724f;
	private float map_longitude = 107.6496925f;
	private ArrayList<MarkerOptions> marker_option_list;
	private MarkerOptions last_marker_option;
	private ArrayList<QuestModel> quest_list;
	private MapState map_state;
	private HashMap<Integer, ArrayList<MisiPhotosModel>> quest_simpan_map;
	private ArrayList<Button> button_quest_list;
	private QuestModel current_quest_model;
	private QuestModel quest_trivia_model;
	private QuestModel quest_fun_model;
	private QuestModel quest_multiple_1_model;
	private QuestModel quest_multiple_2_model;
	private JawabanQuestModel jawaban_quest_model;
	private int quest = 0;
	private int bronze = 0;
	private int silver = 0;
	private int gold = 0;
	private int total = 0;
	private boolean tambah_pin = false;
	private final int max_limit_request = 3;
	private int limit_request = 0;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		instance = this;
		try {
			final View view = inflater.inflate(R.layout.map, container, false);
			setInitial(view);
			return view;
		}catch(InflateException ex) {}
		return null;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	@Override
	public void onDestroyView() {
		Fragment fragment = (getFragmentManager().findFragmentById(R.id.map_view));
		if(fragment != null) {
			FragmentTransaction ft = Main.instance.getSupportFragmentManager().beginTransaction();
			ft.remove(fragment);
			ft.commit();
		}
		super.onDestroyView();
	}
	@Override
	public void didLocationFailed() {
		setMapLocation();
	}
	@Override
	public void didLocationUpdated(float latitude, float longitude) {
		this.map_latitude = latitude;
		this.map_longitude = longitude;
		setMapLocation();
	}
	@Override
	public void isProviderEnabled(boolean enabled) {}
	public void onKeyDown() {
		if(map_state == MapState.PinState) {
			map_state = MapState.QuestState;
			GlobalController.showAlert(
					Main.instance,
					GlobalController.getString(R.string.app_name),
					"Pin lokasi map dibatalkan",
					"OK", null);
			return;
		}
		if(GlobalController.isAlertVisible()) {
			return;
		}
		GlobalController.showAlert(
				Main.instance,
				GlobalController.getString(R.string.app_name),
				"Keluar dari aplikasi ku?",
				"Ya",
				"Tidak",
				new GlobalController.AlertCallback() {
					@Override
					public void didAlertButton1() {
						Main.instance.close();
					}

					@Override
					public void didAlertButton2() {
					}
				});
	}
	public void closeScoreFragment() {
		score_fragment = null;
	}
	public void closeQuestFragment(final JawabanQuestModel jawaban_quest_model, final boolean success) {
		quest_fragment = null;
		if(jawaban_quest_model != null) {
			this.jawaban_quest_model = jawaban_quest_model;
			map_state = MapState.PinState;
			tambah_pin = true;
			GlobalController.showAlert(
					Main.instance,
					GlobalController.getString(R.string.app_name),
					"Pin lokasi map mu",
					"OK", null);
		}else {
			if(success) {
				quest++;
				if(quest <= quest_list.size()) {
					setButtonQuest();
				}
				return;
			}
		}
	}
	public void closeGaleriFragment() {
		galeri_fragment = null;
	}
	public void closeTambahPinFragment(final boolean tambah) {
		tambah_pin_fragment = null;
		if(tambah) {
			openQuestFragment(current_quest_model, quest + 1, true);
		}else {
			if(quest < (quest_list.size() - 1)) {
				quest++;
				setButtonQuest();
			}
		}
	}
	public void closeTriviaFragment(final JawabanQuestModel jawaban_quest_model) {
		trivia_fragment = null;
		if(jawaban_quest_model != null) {
			this.jawaban_quest_model = jawaban_quest_model;
			map_state = MapState.PinState;
			tambah_pin = false;
			GlobalController.showAlert(
					Main.instance,
					GlobalController.getString(R.string.app_name),
					"Pin lokasi map mu",
					"OK", null);
		}

	}
	public void closeTriviaPilganFragment(final JawabanQuestModel jawaban_quest_model) {
		trivia_pilgan_fragment = null;
		if(jawaban_quest_model != null) {
			this.jawaban_quest_model = jawaban_quest_model;
			tambah_pin = false;
			addQuest(new LatLng(0.0, 0.0));
		}
	}
	public void closeTriviaBonusFragment(final JawabanQuestModel jawaban_quest_model) {
		trivia_bonus_fragment = null;
		if(jawaban_quest_model != null) {
			tambahExtraPoin();
			bonus_ok = true;
			setButtonTrivia(quest);
		}
	}
	public void saveQuest(final JawabanQuestModel jawaban_quest) {
		final MisiPhotosModel misi_photos_model = new MisiPhotosModel(jawaban_quest);
		saveQuest(quest + 1, misi_photos_model);
	}
	public void saveQuest(final ArrayList<MisiPhotosModel> misi_photos_list) {
		if(!(misi_photos_list.size() > 0)) {
			return;
		}
		int quest_counter = 1;
		int quest_id = 0;
		final Iterator<MisiPhotosModel> iterator = misi_photos_list.iterator();
		while(iterator.hasNext()) {
			final MisiPhotosModel misi_photos = iterator.next();
			if(quest_id == 0) {
				quest_id = misi_photos.quest_id;
			}else {
				if(quest_id != misi_photos.quest_id) {
					quest_counter++;
					quest_id = misi_photos.quest_id;
				}
			}
			addMarker(misi_photos.latitude, misi_photos.longitude);
			saveQuest(quest_counter, misi_photos);
		}
		quest = quest_counter;
		if(quest == button_quest_list.size()) {
			quest++;
		}
		setButtonQuest();
	}
	public void saveQuest(final int quest_counter, final MisiPhotosModel misi_photos_model) {
		if(quest_simpan_map == null) {
			quest_simpan_map = new HashMap<>();
		}
		ArrayList<MisiPhotosModel> misi_photos_list = new ArrayList<>();
		if(quest_simpan_map.containsKey(quest_counter)) {
			misi_photos_list = quest_simpan_map.get(quest_counter);
		}
		misi_photos_list.add(misi_photos_model);
		quest_simpan_map.put(quest_counter, misi_photos_list);
	}
	private void setInitial(final View view) {
		btn_refresh = (ImageButton)view.findViewById(R.id.btn_refresh);
		btn_map_score = (Button)view.findViewById(R.id.btn_map_score);
		btn_user_online = (ImageButton)view.findViewById(R.id.btn_user_online);
		lbl_user_online = (TextView)view.findViewById(R.id.lbl_user_online);
		lbl_poin_bronze = (TextView)view.findViewById(R.id.lbl_poin_bronze);
		lbl_poin_silver = (TextView)view.findViewById(R.id.lbl_poin_silver);
		lbl_poin_gold = (TextView)view.findViewById(R.id.lbl_poin_gold);
		img_karakter = (ImageView)view.findViewById(R.id.img_karakter);
		img_misi = (ImageView)view.findViewById(R.id.img_misi);
		img_pp = (ImageView)view.findViewById(R.id.img_pp);
		btn_nama = (Button)view.findViewById(R.id.btn_nama);
		lay_quest = (LinearLayout)view.findViewById(R.id.lay_quest);
		btn_galeri = (Button)view.findViewById(R.id.btn_galeri);
		btn_trivia = (Button)view.findViewById(R.id.btn_trivia);
		btn_selesai = (Button)view.findViewById(R.id.btn_selesai);
		int checkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(AksiKuApp.getAppContext());
		if(checkGooglePlayServices != ConnectionResult.SUCCESS) {
			GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices, getActivity(), 1001).show();
			return;
		}
		FragmentManager fragment_manager = getChildFragmentManager();
		support_map_fragment = (SupportMapFragment)fragment_manager.findFragmentById(R.id.map_view);
		if(support_map_fragment != null) {
			support_map_fragment.getMapAsync(new OnMapReadyCallback() {
				public void onMapReady(GoogleMap googleMap) {
					map = googleMap;
					map.setTrafficEnabled(true);
					map.getUiSettings().setMyLocationButtonEnabled(true);
					map.setMyLocationEnabled(true);
					map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					MapsInitializer.initialize(Main.instance);
					map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
						@Override
						public void onMapClick(final LatLng point) {
							if (map_state != MapState.PinState) {
								return;
							}
							addMarker(point);
						}
					});
					map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
						@Override
						public boolean onMarkerClick(final Marker marker) {
							AppController.getAddress(marker.getPosition().latitude, marker.getPosition().longitude, new AppController.AddressCallback() {
								@Override
								public void getAddressResult(String result) {
									marker.setTitle("Alamat :");
									marker.setSnippet(result);
									marker.hideInfoWindow();
									marker.showInfoWindow();
								}
							});
							return false;
						}
					});
				}
			});
		}
		setEventListener();
		setTrivia(false);
		setSelesai(false);
		populasiData();
		location_controller.loadLocationWithOrder(this);
	}
	private void setEventListener() {
		btn_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doRefresh();
			}
		});
		btn_map_score.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doMapScore();
			}
		});
		btn_user_online.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doUserOnline();
			}
		});
		btn_galeri.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doGaleri();
			}
		});
		btn_trivia.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (quest == bonus_multiple_1 - 1) {
					doTriviaPilgan();
				} else if (quest == bonus_multiple_2 - 1) {
					doTriviaPilgan();
				} else if (quest == fun - 1) {
					doTriviaFun();
				} else if (quest == trivia - 1) {
					doTrivia();
				} else if (quest == bonus - 1) {
					doTriviaBonus();
				}
			}
		});
		btn_selesai.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doSelesai();
			}
		});
	}
	private void populasiData() {
		setBronze();
		setSilver();
		setGold();
		ImageLoader.getInstance().displayImage(GlobalVariables.karakter_session.Badge(), img_karakter, GlobalController.getOption(true, true));
		img_pp.setImageBitmap(ImageController.getBitmapFromString(GlobalVariables.user_session.PP()));
		btn_nama.setText(GlobalVariables.user_session.Nama());
		ImageLoader.getInstance().displayImage(GlobalVariables.karakter_session.Balloon(), img_misi, GlobalController.getOption(true, true));
		setQuest();
		doUserOnline();
	}
	private void doRefresh() {
		limit_request = 0;
		if(map_state == MapState.PinState) {
			map_state = MapState.QuestState;
			GlobalController.showAlert(
					Main.instance,
					GlobalController.getString(R.string.app_name),
					"Pin lokasi map dibatalkan",
					"OK",
					new GlobalController.AlertCallback() {
						@Override
						public void didAlertButton1() {
							setQuest();
						}
						@Override
						public void didAlertButton2() {
						}
					});
		}else {
			setQuest();
		}
	}
	private void doMapScore() {
		if(score_fragment != null) {
			return;
		}
		final Bundle bundle = new Bundle();
		bundle.putInt("bronze", bronze);
		bundle.putInt("silver", silver);
		bundle.putInt("gold", gold);
		bundle.putInt("total", total);
		score_fragment = new Score();
		score_fragment.setArguments(bundle);
		GlobalController.push(score_fragment, Score.TAG);
	}
	private void doUserOnline() {
		final RequestQueue queue = Volley.newRequestQueue(Main.instance);
		final String url = Config.url_server + "misc/total-players-online";
		final StringRequest request = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							final JSONObject json = new JSONObject(response);
							final int count = json.getInt("count");
							if(Main.instance != null) {
								Main.instance.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										lbl_user_online.setText(Config.text_blank + count);
									}
								});
							}
						}catch(JSONException ex) {}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError error) {}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
	}
	private void doGaleri() {
		if(galeri_fragment != null) {
			return;
		}
		final Bundle bundle = new Bundle();
		bundle.putSerializable(Galeri.TAG, quest_simpan_map);
		galeri_fragment = new Galeri();
		galeri_fragment.setArguments(bundle);
		GlobalController.push(galeri_fragment, Galeri.TAG);
	}
	private void doTrivia() {
		if(trivia_fragment != null) {
			return;
		}
		if(quest_trivia_model == null) {
			return;
		}
		jawaban_quest_model = null;
		final Bundle bundle = new Bundle();
		bundle.putParcelable(QuestModel.TAG, quest_trivia_model);
		trivia_fragment = new Trivia();
		trivia_fragment.setArguments(bundle);
		GlobalController.push(trivia_fragment, Trivia.TAG);
	}
	private void doTriviaFun() {
		if(trivia_fragment != null) {
			return;
		}
		if(quest_fun_model == null) {
			return;
		}
		jawaban_quest_model = null;
		final Bundle bundle = new Bundle();
		bundle.putParcelable(QuestModel.TAG, quest_fun_model);
		trivia_fragment = new Trivia();
		trivia_fragment.setArguments(bundle);
		GlobalController.push(trivia_fragment, Trivia.TAG);
	}
	private void doTriviaPilgan() {
		if(trivia_pilgan_fragment != null) {
			return;
		}
		if(bonus_multiple_1_ok && bonus_multiple_2_ok) {
			return;
		}
		QuestModel qm = null;
		if(!bonus_multiple_1_ok) {
			qm = quest_multiple_1_model;
		}else if(!bonus_multiple_2_ok) {
			qm = quest_multiple_2_model;
		}
		if(qm == null) {
			return;
		}
		jawaban_quest_model = null;
		final Bundle bundle = new Bundle();
		bundle.putParcelable(QuestModel.TAG, qm);
		trivia_pilgan_fragment = new TriviaPilgan();
		trivia_pilgan_fragment.setArguments(bundle);
		GlobalController.push(trivia_pilgan_fragment, TriviaPilgan.TAG);
	}
	private void doTriviaBonus() {
		if(trivia_bonus_fragment != null) {
			return;
		}
		jawaban_quest_model = null;
		trivia_bonus_fragment = new TriviaBonus();
		GlobalController.push(trivia_bonus_fragment, TriviaBonus.TAG);
	}
	private void doSelesai() {
		if(Main.instance == null) {
			return;
		}
		Main.instance.openSelesai();
	}
	private void setMapLocation() {
		if(map == null) {
			return;
		}
		if(Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(map_latitude, map_longitude), 18);
				map.animateCamera(update);
			}
		});
	}
	private void addMarker(final LatLng point) {
		if(map != null) {
			last_marker_option = new MarkerOptions().position(point).title(Config.text_blank).snippet(Config.text_blank);
			addQuest(point);
		}
	}
	private void addMarker(final double latitude, final double longitude) {
		if(map != null) {
			final MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(Config.text_blank).snippet(Config.text_blank);
			map.addMarker(marker);
		}
	}
	private void setBronze() {
		if (Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				lbl_poin_bronze.setText(Config.text_blank + bronze);
			}
		});
	}
	private void setSilver() {
		if (Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				lbl_poin_silver.setText(Config.text_blank + silver);
			}
		});
	}
	private void setGold() {
		if (Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				lbl_poin_gold.setText(Config.text_blank + gold);
			}
		});
	}
	private void setTrivia(final boolean show) {
		if (Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (show) {
					btn_trivia.setVisibility(View.VISIBLE);
				} else {
					btn_trivia.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	private void setSelesai(final boolean show) {
		if(Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (show) {
					btn_selesai.setVisibility(View.VISIBLE);
					img_karakter.setVisibility(View.VISIBLE);
				} else {
					btn_selesai.setVisibility(View.INVISIBLE);
					img_karakter.setVisibility(View.GONE);
				}
			}
		});
	}
	private void openTambahPin() {
		if(tambah_pin_fragment != null) {
			return;
		}
		tambah_pin_fragment = new TambahPin();
		GlobalController.push(tambah_pin_fragment, TambahPin.TAG);
	}
	private void openQuestFragment(final QuestModel quest_model, final int quest_counter, final boolean success) {
		if(quest_fragment != null) {
			return;
		}
		jawaban_quest_model = null;
		final Bundle bundle = new Bundle();
		bundle.putParcelable(QuestModel.TAG, quest_model);
		bundle.putInt("counter", quest_counter);
		bundle.putBoolean("success", success);
		quest_fragment = new Quest();
		quest_fragment.setArguments(bundle);
		GlobalController.push(quest_fragment, Quest.TAG);
	}
	private void setQuest() {
		GlobalController.showLoading(Main.instance);
		lay_quest.removeAllViews();
		if(map != null) {
			map.clear();
		}
		final RequestQueue queue = Volley.newRequestQueue(Main.instance);
		final String url = Config.url_server + "missions/" + GlobalVariables.karakter_session.Id() + "?expand=quests";
		final StringRequest request = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						final QuestModel quest_model = new QuestModel(response);
						quest_list = new ArrayList<>(quest_model.quest_list);
						setQuestLayout();
						getPhotos();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError error) {
				if(limit_request < max_limit_request) {
					limit_request++;
					setQuest();
					return;
				}
				GlobalController.closeLoading();
				GlobalController.showToast("Terdapat request yang error, silahkan lakukan refresh terlebih dahulu");
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
	}
	private void getPhotos() {
		final RequestQueue queue = Volley.newRequestQueue(Main.instance);
		final String url = Config.url_server + "missions/" + GlobalVariables.karakter_session.Id() + "/photos?access_token=" + GlobalVariables.user_session.Token();
		final StringRequest request = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						final MisiPhotosModel misi_photos_model = new MisiPhotosModel(response);
						saveQuest(misi_photos_model.misi_photos_list);
						getPoin();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError error) {
				if(limit_request < max_limit_request) {
					limit_request++;
					getPhotos();
					return;
				}
				GlobalController.closeLoading();
				GlobalController.showToast("Terdapat request yang error, silahkan lakukan refresh terlebih dahulu");
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
	}
	private void getPoin() {
		final RequestQueue queue = Volley.newRequestQueue(Main.instance);
		final String url = Config.url_server + "players/" + GlobalVariables.user_session.Id() + "?expand=hallOfFame&fields=hallOfFame";
		final StringRequest request = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						GlobalController.closeLoading();
						try {
							JSONObject json = new JSONObject(response);
							json = json.getJSONObject("hallOfFame");
							bronze = json.getInt("bronze");
							silver = json.getInt("silver");
							gold = json.getInt("gold");
							total = json.getInt("total");
							setBronze();
							setSilver();
							setGold();
						}catch(JSONException ex) {}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError error) {
				if(limit_request < max_limit_request) {
					limit_request++;
					getPoin();
					return;
				}
				GlobalController.closeLoading();
				GlobalController.showToast("Terdapat request yang error, silahkan lakukan refresh terlebih dahulu");
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
	}
	private void addQuest(final LatLng point) {
		if(jawaban_quest_model == null) {
			map_state = MapState.QuestState;
			return;
		}
		jawaban_quest_model.latitude = point.latitude;
		jawaban_quest_model.longitude = point.longitude;
		GlobalController.showLoading(Main.instance);
		final RequestQueue queue = Volley.newRequestQueue(Main.instance);
		final String url = Config.url_server + "location/new";
		final StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						GlobalController.closeLoading();
						map_state = MapState.QuestState;
						saveQuest(jawaban_quest_model);
						if(tambah_pin) {
							tambahPoin();
							openTambahPin();
						}else {
							tambahExtraPoin();
							if (quest == bonus_multiple_1 - 1) {
								bonus_multiple_1_ok = true;
							} else if (quest == bonus_multiple_2 - 1) {
								bonus_multiple_2_ok = true;
							} else if (quest == fun - 1) {
								fun_ok = true;
							} else if (quest == trivia - 1) {
								trivia_ok = true;
							}
							setButtonTrivia(quest);
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError error) {
				GlobalController.closeLoading();
				String message = Config.text_blank;
				int status_code = 0;
				if(error.networkResponse != null) {
					final NetworkResponse response = error.networkResponse;
					status_code = response.statusCode;
					try {
						final String value = new String(response.data, "UTF-8");
						final JSONObject json = new JSONObject(value);
						message = json.getString("message");
					}catch(JSONException ex) {
					}catch(UnsupportedEncodingException ex) {}
					if(!GlobalController.isNotNull(message)) {
						message = error.getMessage();
					}
				}
				if(!GlobalController.isNotNull(message)) {
					message = "Terdapat masalah pada penyimpanan (" + status_code + ")";
				}
				GlobalController.showAlert(
						Main.instance,
						GlobalController.getString(R.string.app_name),
						message + ", coba lagi? Isi nya\n" +
								"Token : " + GlobalVariables.user_session.Token() + "\n" +
								"Quest : " + String.valueOf(jawaban_quest_model.quest_id) + "\n" +
								"Latitude : " + String.valueOf(jawaban_quest_model.latitude) + "\n" +
								"Longitude : " + String.valueOf(jawaban_quest_model.longitude) + "\n" +
								"Answer 1 : " + jawaban_quest_model.answer_1 + "\n" +
								"Answer 2 : " + jawaban_quest_model.answer_2 + "\n" +
								"Answer 3 : " + jawaban_quest_model.answer_3,
						"Ya",
						"Tidak",
						new GlobalController.AlertCallback() {
							@Override
							public void didAlertButton1() {
								addQuest(point);
							}
							@Override
							public void didAlertButton2() {
								map_state = MapState.QuestState;
							}
						});
			}
		}) {
			@Override
			protected java.util.Map<String, String> getParams() {
				java.util.Map<String, String> params = new HashMap<>();
				params.put("access_token", GlobalVariables.user_session.Token());
				params.put("quest_id", String.valueOf(jawaban_quest_model.quest_id));
				params.put("latitude", String.valueOf(jawaban_quest_model.latitude));
				params.put("longitude", String.valueOf(jawaban_quest_model.longitude));
				params.put("answer_1", jawaban_quest_model.answer_1);
				params.put("answer_2", jawaban_quest_model.answer_2);
				params.put("answer_3", jawaban_quest_model.answer_3);
				params.put("photo", jawaban_quest_model.photo);
				return params;
			}
		};
		request.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
	}
	private void setQuestLayout() {
		button_quest_list = new ArrayList<>();
		int quest_count = 0;
		for(int counter = 0; counter < quest_list.size(); counter++) {
			final QuestModel quest_model = quest_list.get(counter);
			if(quest_model.type.equals(QuestModel.QuestNormal)) {
				quest_count++;
				final View button_quest_view = inflater.inflate(R.layout.button_quest, null, false);
				final Button btn_quest = (Button) button_quest_view.findViewById(R.id.btn_quest);
				btn_quest.setText("Quest " + quest_count);
				btn_quest.setEnabled(false);
				button_quest_list.add(btn_quest);
				final int quest_counter = quest_count;
				setQuestEventListener(btn_quest, quest_model, quest_counter);
				lay_quest.addView(button_quest_view);
			}else if(quest_model.type.equals(QuestModel.QuestTrivia)) {
				quest_trivia_model = quest_model;
			}else if(quest_model.type.equals(QuestModel.QuestFun)) {
				quest_fun_model = quest_model;
			}else if(quest_model.type.equals(QuestModel.QuestBonus)) {
				if(quest_multiple_1_model == null) {
					quest_multiple_1_model = quest_model;
				}else if(quest_multiple_2_model == null) {
					quest_multiple_2_model = quest_model;
				}
			}
		}
		setButtonQuest();
	}
	private void setQuestEventListener(
			final Button btn_quest,
			final QuestModel quest_model,
			final int quest_counter) {
		btn_quest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				current_quest_model = quest_model;
				openQuestFragment(quest_model, quest_counter, false);
			}
		});
	}
	private void setButtonQuest() {
		if(Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for(int counter = 0; counter < button_quest_list.size(); counter++) {
					final Button btn_quest = button_quest_list.get(counter);
					if(counter <= quest) {
						btn_quest.setEnabled(true);
					}else {
						btn_quest.setEnabled(false);
					}
				}
			}
		});
		if((quest + 1) >= bonus_multiple_1 && (quest + 1) <= bonus) {
			final int quest_counter = quest + 1;
			setButtonTrivia(quest_counter);
		}else {
			setTrivia(false);
		}
		if((quest + 1) >= button_quest_list.size()) {
			setSelesai(true);
		}else {
			setSelesai(false);
		}
	}
	private void setButtonTrivia(final int quest_counter) {
		if (quest_counter == bonus_multiple_1 && bonus_multiple_1_ok) {
			setTrivia(false);
		} else if (quest_counter == bonus_multiple_2 && bonus_multiple_2_ok) {
			setTrivia(false);
		} else if (quest_counter == fun && fun_ok) {
			setTrivia(false);
		} else if (quest_counter == trivia && trivia_ok) {
			setTrivia(false);
		} else if (quest_counter == bonus && bonus_ok) {
			setTrivia(false);
		}else {
			setTrivia(true);
		}
	}
	private void tambahPoin() {
		if(jawaban_quest_model == null) {
			return;
		}
		if(quest == 0 || quest == 1) {
			bronze += jawaban_quest_model.point;
			setBronze();
		}else if(quest == 2 || quest == 3) {
			silver += jawaban_quest_model.point;
			setSilver();
		}else {
			gold += jawaban_quest_model.point;
			setGold();
		}
		total = bronze + silver + gold;
	}
	private void tambahExtraPoin() {
		if(jawaban_quest_model == null) {
			return;
		}
		total += jawaban_quest_model.point;
	}
}