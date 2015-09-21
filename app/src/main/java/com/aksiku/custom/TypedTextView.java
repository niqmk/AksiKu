package com.aksiku.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypedTextView extends TextView {
	private final static float TEXT_CHARACTER_DELAY = 0.1f;
	private final static int TEXT_CHARACTER_DELAY_MS = (int)(TEXT_CHARACTER_DELAY * 1000);
	private int current_character;
	private long last_time;
	private CharSequence text;
	public TypedTextView(Context context) {
		super(context);
	}
	public TypedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TypedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		final long time = SystemClock.uptimeMillis();
		final long delta = time - last_time;
		if(delta > TEXT_CHARACTER_DELAY_MS) {
			if(text != null) {
				if(current_character <= text.length()) {
					CharSequence subtext = text.subSequence(0, current_character);
					setText(subtext, TextView.BufferType.SPANNABLE);
					current_character++;
					postInvalidateDelayed(TEXT_CHARACTER_DELAY_MS);
				}
			}
		}
		super.onDraw(canvas);
	}
	public void setTypedText(CharSequence text) {
		this.text = text;
		current_character = 0;
		last_time = 0;
		postInvalidate();
	}
	public void snapToEnd() {
		current_character = text.length() - 1;
	}
}