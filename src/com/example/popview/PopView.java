package com.example.popview;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.popview.MyWebView.OnScrollListener;
import com.example.resid.ResourcesId;

public class PopView extends LinearLayout {

	private static final String TAG = "PopView";
	private Context mContext;
	private TextView mTxtWord;
	private Button mBtnFullscreen;
	private Button mBtnBack;
	private MyWebView mWebView;
	private ViewGroup mParent;
	private VideoView mVideoView;
	private int mBtnFullscreenId, mBtnBackId;
	private int mStrFullscreenId, mStrSmallscreenId;

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
	
	public void init(String word, String explain, String videoPath) {
//		Log.i(TAG, "init()");
		ResourcesId res = ResourcesId.getInstance(mContext);

		bIsFullscreen = false;
		
		mParent = getParent(this);
		
		int id = res.getResourcesId("id", "x_txt_word");
		mTxtWord = (TextView)findViewById(id);
		mTxtWord.setText(word);
		
		mBtnFullscreenId = res.getResourcesId("id", "x_btn_fullscreen");
		mBtnFullscreen = (Button)findViewById(mBtnFullscreenId);
		mBtnFullscreen.setOnClickListener(mBtnOnClickListener);
		
		mBtnBackId = res.getResourcesId("id", "x_btn_back");
		mBtnBack = (Button)findViewById(mBtnBackId);
		mBtnBack.setOnClickListener(mBtnOnClickListener);

		id = res.getResourcesId("id", "x_webview_explain");
		mWebView = (MyWebView)findViewById(id);
		webviewSetting(mWebView);
//		mWebView.loadData(explain, "text/html", "uft-8");
//		mWebView.loadUrl("file:///android_asset/flash.html");
		mWebView.loadDataWithBaseURL("about:blank", explain, "text/html", "uft-8", null);
		
		mStrFullscreenId = res.getResourcesId("string", "x_fullscreen");
		mStrSmallscreenId = res.getResourcesId("string", "x_smallscreen");
		
		id = res.getResourcesId("id", "x_videoview");
		mVideoView = (VideoView)findViewById(id);
		
		dragView(this);

		showVideoView(videoPath);
	}

	private void webviewSetting(MyWebView webview) {
		WebSettings s = webview.getSettings();
		s.setJavaScriptEnabled(true);
		s.setPluginState(PluginState.ON);
		
		webview.setOnScrollListenser(mOnScrollListener);
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
			dismiss();
			return true;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	public void onResume() {
		if (mWebView != null) {
			mWebView.resumeTimers();
		}
	}
	
	public void onPause() {
		if (mWebView != null) {
			mWebView.pauseTimers();
		}
	}
	
	private OnClickListener mBtnOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == mBtnFullscreenId) {
				fullscreen();
			}
			else if (id == mBtnBackId) {
				dismiss();
			}
		}
	};

	private boolean bIsFullscreen = false;
	private int orgX, orgY, orgW, orgH;
	private void fullscreen() {
		if (bIsFullscreen) {
			changeXYWH(this, orgX, orgY, orgW, orgH);
			mBtnFullscreen.setText(mStrFullscreenId);
		} else {
			orgX = (int) getX();
			orgY = (int) getY();
			orgW = getWidth();
			orgH = getHeight();
			changeXYWH(this, 0, 0, mParent.getWidth(), mParent.getHeight());
			mBtnFullscreen.setText(mStrSmallscreenId);
		}
		bIsFullscreen = !bIsFullscreen;
	}
	
	public void setOrgPosition(int ox, int oy, int ow, int oh) {
		orgX = ox;
		orgY = oy;
		orgW = ow;
		orgH = oh;
	}
	
	public boolean isFullscreen() {
		return bIsFullscreen;
	}
	
	private void changeXYWH(View view, int x, int y, int w, int h) {
//		Log.i(TAG, "x:" + x + ", y:" + y + ", w:" + w + ", h:" + h);
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
				if (bIsFullscreen) {
					return true;
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x = (int) event.getX();
					y = (int) event.getY();
					String m = String.format("webview[scale:%f, content height:%d, height:%d, scroll y:%d]", 
							mWebView.getScale(), mWebView.getContentHeight(), 
							mWebView.getHeight(), mWebView.getScrollY());
					Log.i(TAG, m);
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

	private OnScrollListener mOnScrollListener = new OnScrollListener() {
		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			mVideoView.setTranslationY(-mWebView.getScrollY());
		}
	};
	
	private OnPreparedListener mOnPreparedListener = new OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.setLooping(true);
		}
	};
	
	private OnErrorListener mOnErrorListener = new OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			mVideoView.setVisibility(View.INVISIBLE);
			return false;
		}
	};
	
	
	protected void showVideoView(String path) {
		if (path == null || path.length() <= 0) {
			return;
		}
		mVideoView.setOnPreparedListener(mOnPreparedListener);
		mVideoView.setOnErrorListener(mOnErrorListener);
		mVideoView.setVideoPath(path);
		mVideoView.start();
		mVideoView.requestFocus();
		mVideoView.setVisibility(View.INVISIBLE);
	}
	
}
