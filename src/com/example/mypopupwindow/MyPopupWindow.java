package com.example.mypopupwindow;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MyPopupWindow extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_popup_window);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_popup_window, menu);
		return true;
	}

}
