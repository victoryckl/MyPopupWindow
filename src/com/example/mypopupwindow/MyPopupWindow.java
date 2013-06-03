package com.example.mypopupwindow;

import com.example.pop.MyPop;
import com.example.popview.PopView;
import com.example.popview.PopView.OnDismissListener;
import com.example.popview.PopViewWrapper;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.Toast;

public class MyPopupWindow extends Activity {

	private static final String TAG = null;
	private Button mBtnPopup, mBtnPopview;
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
		mBtnPopup = (Button)findViewById(R.id.btn_popupwindow);
		mBtnPopup.setOnClickListener(mBtnClickListener);
		
		mBtnPopview = (Button)findViewById(R.id.btn_popview);
		mBtnPopview.setOnClickListener(mBtnClickListener);
	}
	
	private OnClickListener mBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_popupwindow:
				showPop();
				break;
			case R.id.btn_popview:
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
		String html = "<html>"
				+"\n<head>"
				+"\n	<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"
				+"\n</head>"
				+"\n<body>"
				+"\n<div>"
				+"\n	<p>test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,"
				+"\n	<p>test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,"
				+"\n	<p>test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,"
				+"\n	<p>test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,"
				+"\n	<p>test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,"
				+"\n	<p>test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,test text,	"
				+"\n	"
				+"\n	<table style='float:right; border:1px solid #00F;' width='50%' height='40%' cellpadding='0' cellspacing='0'> "
				+"\n		<tr>"
				+"\n		"
				+"\n			<td align='center' valign='middle'>"
				+"\n			<object type='application/x-shockwave-flash'"
				+"\n				id='myFlashMovie' "
				+"\n				align='middle'"
				+"\n				data='file:///android_asset/g.swf'"
				+"\n				wmode=''"
				+"\n				height='100%' "
				+"\n				width='100%'>"
				+"\n					<param name='movie' value='file:///android_asset/g.swf'>"
				+"\n					<param name='quality' value='height'>"
				+"\n					<param name='wmode' value='window'>"
				+"\n					"
				+"\n					<param name='allowScriptAccess' value='always'>"
				+"\n					<param name='menu' value='false'>"
				+"\n					"
				+"\n					<param name='allowFullScreen' value='true'>"
				+"\n					<div style='height: 100%; margin: 0; padding: 0; '> "
				+"\n						<div id='center'> "
				+"\n							<center>"
				+"\n								<br>"
				+"\n								<h3><font color='#C23352'>播放失败!</font></h3>"
				+"\n								<h5>请安装Flash插件，或者重新打开一次</h5>"
				+"\n							</center>"
				+"\n						</div> "
				+"\n					</div>"
				+"\n			</object>"
				+"\n"
				+"\n			</script>"
				+"\n			</td> "
				+"\n		</tr> "
				+"\n	</table> "
				+"\n</div>"
				+"\n</body>"
				+"\n</html>"
				+"\n";
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
