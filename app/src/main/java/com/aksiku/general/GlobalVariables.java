package com.aksiku.general;

import com.aksiku.AksiKuApp;
import com.aksiku.activity.Main;
import com.aksiku.general.session.KarakterSession;
import com.aksiku.general.session.UserSession;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GlobalVariables {
	public static UserSession user_session;
	public static KarakterSession karakter_session;
	public static void load() {
		user_session = new UserSession();
		karakter_session = new KarakterSession();
		setImageLoader();
	}
	public static void unload() {
		if(user_session.isSession()) {
			user_session.closeSession();
			karakter_session.closeSession();
			final RequestQueue queue = Volley.newRequestQueue(Main.instance);
			final String url = Config.url_server + "players/logout";
			final StringRequest request = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(final VolleyError error) {
					if(error.networkResponse == null) {
						return;
					}
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<>();
					params.put("access_token", GlobalVariables.user_session.Token());
					return params;
				}
			};
			request.setRetryPolicy(new DefaultRetryPolicy(
					30000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			queue.add(request);
		}
	}
	private static void setImageLoader() {
		final File cache_dir = StorageUtils.getCacheDirectory(AksiKuApp.getAppContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(AksiKuApp.getAppContext())
				.diskCacheExtraOptions(480, 800, null)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.diskCacheSize(50 * 1024 * 1024)
				.diskCache(new UnlimitedDiscCache(cache_dir))
				.diskCacheFileCount(100)
				.build();
		ImageLoader.getInstance().init(config);
	}
}