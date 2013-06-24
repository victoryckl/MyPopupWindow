package com.example.mypopupwindow;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.pop.MyPop;
import com.example.popview.PopViewWrapper;

public class MyPopupWindow extends Activity {

	private static final String TAG = null;
	private Button mBtnPopup, mBtnPopview;
	private Display mDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
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
		
		findViewById(R.id.x_btn_video_demo).setOnClickListener(mBtnClickListener);
		
		mPopViewWrapper.initPopView();
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
			case R.id.x_btn_video_demo:
				startVideoDemo();
				break;
			default:
				break;
			}
		}
	};
	
	private void showPop() {
		MyPop pop = new MyPop(this);
		/*
		 * popupWindow.showAsDropDown��View view�������Ի���λ���ڽ�����view���
		 * showAsDropDown(View anchor, int xoff, int yoff)�����Ի���λ���ڽ�����view�����x y �����ƫ����
		 * showAtLocation(View parent, int gravity, int x, int y)�����Ի���
		 * parent ������ gravity ���������ֵ�λ����Gravity.CENTER  x y ���ֵ
		 */
		pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}

	protected void startVideoDemo() {
		Intent intent = new Intent();
		intent.setClass(this, VideoActivity.class);
		startActivity(intent);
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
		Log.i(TAG, "onResume()");
		mPopViewWrapper.onResume();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Log.i(TAG, "onPause()");
		mPopViewWrapper.onPause();
		super.onPause();
	}
	
	@Override
	protected void onStart() {
		Log.i(TAG, "onStart()");
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		Log.i(TAG, "onStop()");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
	}
}
