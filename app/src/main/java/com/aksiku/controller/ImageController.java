package com.aksiku.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.aksiku.AksiKuApp;
import com.aksiku.R;
import com.aksiku.general.Config;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;

public class ImageController {
	public static byte[] getBytesFromBitmap(final Bitmap bitmap, final int compress_quality) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, compress_quality, stream);
		return stream.toByteArray();
	}
	public static String getStringFromBitmap(final Bitmap bitmap, final int comress_quality) {
		if(bitmap == null) {
			return Config.text_blank;
		}
		return ImageController.getStringFromBytes(getBytesFromBitmap(bitmap, comress_quality));
	}
	public static Bitmap getBitmapFromBytes(final byte[] bytes) {
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inPurgeable = true;
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}
	public static Bitmap getBitmapFromString(final String value) {
		if(!GlobalController.isNotNull(value)) {
			return BitmapFactory.decodeResource(AksiKuApp.getAppContext().getResources(), R.drawable.oval);
		}
		byte[] bytes = ImageController.getBytesFromString(value);
		return getBitmapFromBytes(bytes);
	}
	public static void save(final byte[] data) {
		File picture_file = getOutputMediaFile();
		if(picture_file == null) {
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(picture_file);
			fos.write(data);
			fos.close();
		}catch(FileNotFoundException ex) {
		}catch(IOException ex) {}
	}
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static String getStringFromBytes(final byte[] bytes) {
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static byte[] getBytesFromString(final String value) {
		if (!GlobalController.isNotNull(value)) {
			return null;
		}
		try {
			return Base64.decode(value, Base64.DEFAULT);
		}catch (Exception ex) {
		}catch (OutOfMemoryError ex) {}
		return null;
	}
	@TargetApi(Build.VERSION_CODES.FROYO)
	private static File getOutputMediaFile() {
		File media_storage_dir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), Config.TAG);
		if(!media_storage_dir.exists()) {
			if(!media_storage_dir.mkdirs()) {
				return null;
			}
		}
		File media_file  = new File(media_storage_dir.getPath() + File.separator + "IMG_"+ new Date().getTime() + ".jpg");
		return media_file;
	}
}