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
		 * popupWindow.showAsDropDown��View view�������Ի���λ���ڽ�����view���
		 * showAsDropDown(View anchor, int xoff, int yoff)�����Ի���λ���ڽ�����view�����x y ������ƫ����
		 * showAtLocation(View parent, int gravity, int x, int y)�����Ի���
		 * parent ������ gravity ���������ֵ�λ����Gravity.CENTER  x y ����ֵ
		 */
		pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}
}
