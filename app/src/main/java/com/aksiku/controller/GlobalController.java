package com.aksiku.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.aksiku.AksiKuApp;
import com.aksiku.R;
import com.aksiku.activity.Main;
import com.aksiku.general.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class GlobalController {
	public static interface AlertCallback {
		public abstract void didAlertButton1();
		public abstract void didAlertButton2();
	}
	private static ProgressDialog loading;
	private static AlertDialog alert_dialog;
	private static Toast toast;
	private static PowerManager.WakeLock wake_lock;
	public static boolean isNotNull(final String value) {
		if(value == null) {
			return false;
		}
		if(value.trim().equals(Config.text_blank)) {
			return false;
		}else {
			return true;
		}
	}
	public static boolean isEmailValid(final String text) {
		final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
				"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
						"\\@" +
						"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
						"(" +
						"\\." +
						"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
						")+");
		return EMAIL_ADDRESS_PATTERN.matcher(text).matches();
	}
	public static String getString(final int resource_id) {
		return AksiKuApp.getAppContext().getString(resource_id);
	}
	public static String getString(final String resource_id) {
		int res_id = AksiKuApp.getAppContext().getResources().getIdentifier(resource_id, "string", AksiKuApp.getAppContext().getPackageName());
		return AksiKuApp.getAppContext().getString(res_id);
	}
	public static void push(final Fragment fragment, final String tag) {
		if(Main.instance == null) {
			return;
		}
		final FragmentTransaction fragment_transaction = Main.instance.getSupportFragmentManager().beginTransaction();
		fragment_transaction.add(R.id.lay_main, fragment, tag).addToBackStack(null).commit();
	}
	public static void push(final Fragment parent, final Fragment fragment, final String tag) {
		final FragmentTransaction fragment_transaction = parent.getFragmentManager().beginTransaction();
		fragment_transaction.add(R.id.lay_main, fragment, tag).addToBackStack(null).commit();
	}
	public static void pop() {
		if(Main.instance != null) {
			Main.instance.pop();
		}
	}
	public static void bukanPause() {
		if(Main.instance != null) {
			Main.instance.bukanPause();
		}
	}
	public static void openSettingsProvider(final Activity activity) {
		activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Config.provider_settings_request_code);
	}
	public static void showToast(final int resource_id) {
		if (Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				GlobalController.showToast(AksiKuApp.getAppContext(), AksiKuApp.getAppContext().getString(resource_id), Config.toast_delay);
			}
		});
	}
	public static void showToast(final String message) {
		if(Main.instance == null) {
			return;
		}
		Main.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				GlobalController.showToast(AksiKuApp.getAppContext(), message, Config.toast_delay);
			}
		});
	}
	@SuppressLint("ShowToast")
	public static void showToast(final Context context, final String value, final int delay) {
		if(toast != null) {
			if(toast.getView().isShown()) {
				return;
			}
		}
		toast = Toast.makeText(context, value, delay);
		toast.show();
	}
	public static void closeToast() {
		if(toast != null) {
			toast.cancel();
		}
	}
	public static void showLoading(final Activity activity) {
		if(activity == null) {
			return;
		}
		if(loading != null) {
			return;
		}
		loading = new ProgressDialog(activity);
		loading.setCancelable(false);
		loading.setMessage(activity.getString(R.string.message_loading));
		loading.show();
	}
	public static void closeLoading() {
		if(loading == null) {
			return;
		}
		loading.cancel();
		loading.dismiss();
		loading = null;
	}
	public static boolean isLoading() {
		return (loading != null);
	}
	public static void showAlert(
			final Activity activity,
			final String title,
			final String message,
			final String button1,
			final String button2,
			final AlertCallback alert_callback) {
		closeAlert();
		alert_dialog = new AlertDialog.Builder(activity)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(button1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(alert_callback != null) {
							alert_callback.didAlertButton1();
						}
					}
				})
				.setNegativeButton(button2, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(alert_callback != null) {
							alert_callback.didAlertButton2();
						}
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();
		alert_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				alert_dialog = null;
			}
		});
	}
	public static void showAlert(
			final Activity activity,
			final String title,
			final String message,
			final String button1,
			final AlertCallback alert_callback) {
		closeAlert();
		alert_dialog = new AlertDialog.Builder(activity)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(button1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (alert_callback != null) {
							alert_callback.didAlertButton1();
						}
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();
		alert_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				alert_dialog = null;
			}
		});
	}
	public static boolean isAlertVisible() {
		return (alert_dialog != null);
	}
	public static void closeAlert() {
		if(alert_dialog == null) {
			return;
		}
		alert_dialog.dismiss();
		alert_dialog.cancel();
		alert_dialog = null;
	}
	public static void vibrate(final Context context, final int time) {
		Vibrator v = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(time);
	}
	public static void setAlpha(final View view, final float value) {
		AlphaAnimation alpha = new AlphaAnimation(value, value);
		alpha.setFillAfter(true);
		view.startAnimation(alpha);
	}
	public static long getUTCTimestamp() {
		return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();
	}
	public static int[] getIntegerArray(final int array_resource_id) {
		final TypedArray ar = AksiKuApp.getAppContext().getResources().obtainTypedArray(array_resource_id);
		final int len = ar.length();
		final int[] res_ids = new int[len];
		for(int i = 0; i < len; i++)
			res_ids[i] = ar.getResourceId(i, 0);
		ar.recycle();
		return res_ids;
	}
	public static DisplayImageOptions getOption(final boolean cache_memory, final boolean cache_disk) {
		return new DisplayImageOptions.Builder()
				.cacheInMemory(cache_memory)
				.cacheOnDisk(cache_disk)
				.build();
	}
	public static boolean isExternalStorageAvailable() {
		final String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}
	public static String getStorageDirectory(final Context context) {
		if(GlobalController.isExternalStorageAvailable()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}else {
			return context.getCacheDir().getAbsolutePath();
		}
	}
	public static void closeKeyboard(final Activity activity) {
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		View v = activity.getCurrentFocus();
		if(v == null) return;
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	@SuppressWarnings("deprecation")
	public static void setWakeLock(final Context context, final String TAG) {
		final PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			wake_lock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
		}else {
			wake_lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		}
		wake_lock.acquire();
	}
	public static void destroyWakeLock() {
		if(wake_lock != null) {
			if(wake_lock.isHeld()) {
				wake_lock.release();
			}
		}
	}
}
