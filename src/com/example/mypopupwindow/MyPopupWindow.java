package com.example.mypopupwindow;

import com.example.pop.MyPop;

import android.os.Bundle;
import android.app.Activity;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyPopupWindow extends Activity {

	private Button mBtnPopup;
	private Display mDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_popup_window);
		
		mDisplay = getWindowManager().getDefaultDisplay();
		
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_popup_window, menu);
		return true;
	}

	private void init() {
		mBtnPopup = (Button)findViewById(R.id.button_popupwindow);
		mBtnPopup.setOnClickListener(mBtnClickListener);
	}
	
	private OnClickListener mBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_popupwindow:
				showPop();
				break;

			default:
				break;
			}
		}
	};
	
	private void showPop() {
		MyPop pop = new MyPop(this);
		/*
		 * popupWindow.showAsDropDown（View view）弹出对话框，位置在紧挨着view组件
		 * showAsDropDown(View anchor, int xoff, int yoff)弹出对话框，位置在紧挨着view组件，x y 代表着偏移量
		 * showAtLocation(View parent, int gravity, int x, int y)弹出对话框
		 * parent 父布局 gravity 依靠父布局的位置如Gravity.CENTER  x y 坐标值
		 */
		pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}
}
