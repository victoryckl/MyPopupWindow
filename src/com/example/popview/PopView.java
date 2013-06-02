package com.example.popview;

import com.example.mypopupwindow.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PopView extends LinearLayout {

	private static final String TAG = "PopView";
	private Context mContext;
	private TextView mTxtWord;
	private Button mBtnFullscreen;
	private Button mBtnBack;
	private WebView mWebView;
	private ViewGroup mParent;

	public PopView(Context context) {
		super(context);
		setContext(context);
	}

	public PopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setContext(context);
	}

	public PopView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setContext(context);
	}
	
	private void setContext(Context context) {
		if (context != null) {
			mContext = context.getApplicationContext();
		}
	}
	
	public void init(String word, String explain) {
		Log.i(TAG, "init()");

		bIsFullscreen = false;
		
		mParent = getParent(this);
		
		mTxtWord = (TextView)findViewById(R.id.txt_word);
		mTxtWord.setText(word);
		
		mBtnFullscreen = (Button)findViewById(R.id.btn_fullscreen);
		mBtnFullscreen.setOnClickListener(mBtnOnClickListener);
		
		mBtnBack = (Button)findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(mBtnOnClickListener);
		
		mWebView = (WebView)findViewById(R.id.webview_explain);
		mWebView.loadData(explain, "text/html", "uft-8");
		
		dragView(this);
	}
	
	private ViewGroup getParent(View view) {
		ViewGroup vg = null;
		ViewParent parent = view.getParent();
		if (parent != null) {
			if (parent instanceof ViewGroup) {
				vg = (ViewGroup)parent;
			}
		}
		return vg;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Log.i(TAG, "" + event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		Log.i(TAG, "" + event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Toast.makeText(mContext, "PopView.onKeyUp()", Toast.LENGTH_SHORT).show();
			dismiss();
			return true;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	private OnClickListener mBtnOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_fullscreen:
				fullscreen();
				break;
			case R.id.btn_back:
				dismiss();
				break;
				
			default:
				break;
			}
		}
	};

	private boolean bIsFullscreen = false;
	private int orgX, orgY, orgW, orgH;
	private void fullscreen() {
		if (bIsFullscreen) {
			changeXYWH(this, orgX, orgY, orgW, orgH);
		} else {
			orgX = (int) getX();
			orgY = (int) getY();
			orgW = getWidth();
			orgH = getHeight();
			changeXYWH(this, 0, 0, mParent.getWidth(), mParent.getHeight());
		}
		bIsFullscreen = !bIsFullscreen;
	}
	
	private void changeXYWH(View view, int x, int y, int w, int h) {
		if (mParent != null) {
			AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) view.getLayoutParams();
			lp.width = w;
			lp.height = h;
			lp.x = x;
			lp.y = y;
			view.setLayoutParams(lp);
		}
	}
	
	public void dismiss() {
		if (mOnDismissListener != null) {
			mOnDismissListener.onDismiss();
			mOnDismissListener = null;
		}
		
		if (mParent != null) {
			mParent.removeView(this);
		}
		
		if (mWebView != null) {
			mWebView.destroy();
			mWebView = null;
		}
	}
	
	private OnDismissListener mOnDismissListener;
	public interface OnDismissListener {
		void onDismiss();
	}

	public void setOnDismissListener(OnDismissListener listener) {
		mOnDismissListener = listener;
	}
	
	private void dragView(View view) {
		view.setOnTouchListener(new OnTouchListener() {
			int x = 0, y = 0;
			int dx = 0, dy = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				Log.i(TAG, "onTouch("+event+")");
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x = (int) event.getX();
					y = (int) event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					dx = (int) (event.getX() - x);
					dy = (int) (event.getY() - y);
					offsetXY(v, dx, dy);
					break;
				case MotionEvent.ACTION_UP:
					break;

				default:
					break;
				}
				return true;
			}
		});
	}
	private void offsetXY(View view, int dx, int dy) {
		AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) view.getLayoutParams();
		lp.x += dx;
		lp.y += dy;
		view.setLayoutParams(lp);
	}
}