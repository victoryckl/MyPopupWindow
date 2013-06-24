package com.example.toast;

import android.content.Context;
import android.widget.Toast;

public class StaticToast {
	private static Toast mToast;
	
	private StaticToast() {}
	
	public static void show(Context context, String text, int duration) {
		if (mToast == null) {
			mToast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		mToast.setText(text);
		mToast.setDuration(duration);
		mToast.show();
	}
	
	public static void show(Context context, int id, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		mToast.setText(id);
		mToast.setDuration(duration);
		mToast.show();
	}
}
