package com.example.popview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.webkit.WebView;

public class MyWebView extends WebView {

	private static final String TAG = "MyWebView";

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyWebView(Context context) {
		super(context);
	}
	
	private void init(Context context) {
		Log.i(TAG, "context:" + context.toString());
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		String m = String.format("%d -> %d", oldt, t);
		Log.i(TAG, m);
		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollChanged(l, t, oldl, oldt);
		}
	}
	
	public interface OnScrollListener {
		void onScrollChanged(int l, int t, int oldl, int oldt);
	}
	private OnScrollListener mOnScrollListener = null;
	public void setOnScrollListenser(OnScrollListener l) {
		mOnScrollListener = l;
	}
}
