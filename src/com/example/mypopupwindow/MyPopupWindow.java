package com.example.mypopupwindow;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.pop.MyPop;
import com.example.popview.PopViewWrapper;

public class MyPopupWindow extends Activity {

	private static final String TAG = null;
	private Button mBtnPopup, mBtnPopview;
	private Display mDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_my_popup_window);
		
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
		mBtnPopup = (Button)findViewById(R.id.x_btn_popupwindow);
		mBtnPopup.setOnClickListener(mBtnClickListener);
		
		mBtnPopview = (Button)findViewById(R.id.x_btn_popview);
		mBtnPopview.setOnClickListener(mBtnClickListener);
	}
	
	private OnClickListener mBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.x_btn_popupwindow:
				showPop();
				break;
			case R.id.x_btn_popview:
				showPopView("hello", getHtml());
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

	//---------------------------
	private PopViewWrapper mPopViewWrapper = new PopViewWrapper(this);
	private void showPopView(String word, String html) {
		mPopViewWrapper.showPopView(word, html);
	}
	
	private String getHtml() {
		String html = "";
		InputStream in = null;
		try {
			in = getAssets().open("flash.html");
			int len = in.available();
			byte[] buffer = new byte[len];
			in.read(buffer);
			html = new String(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return html;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mPopViewWrapper.onKeyDown(keyCode, event)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (mPopViewWrapper.onKeyUp(keyCode, event)) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		mPopViewWrapper.onResume();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		mPopViewWrapper.onPause();
		super.onPause();
	}
}
