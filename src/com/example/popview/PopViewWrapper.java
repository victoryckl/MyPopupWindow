package com.example.popview;

import android.app.Activity;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Toast;

import com.example.popview.PopView.OnDismissListener;
import com.example.resid.ResourcesId;

public class PopViewWrapper {
	protected static final String TAG = "PopViewWrapper";
	private boolean bIsShowing = false;
	private AbsoluteLayout mAbsoluteLayout;
	private PopView mPopView;
	private Activity mActivity;
	
	public PopViewWrapper(Activity activity) {
		mActivity = activity;
	}
	
	public void showPopView(String word, String explain) {
		if (!bIsShowing) {
			bIsShowing = true;
			
			if (mAbsoluteLayout == null) {
				int id = ResourcesId.getResourcesId(mActivity, "id", "x_absolutelayout");
				mAbsoluteLayout = (AbsoluteLayout)mActivity.findViewById(id);
			}
			
			mAbsoluteLayout.removeAllViews();
			mAbsoluteLayout.setVisibility(View.VISIBLE);
			mAbsoluteLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Log.i("AbsoluteLayou", "onClick()");
					mPopView.dismiss();
				}
			});
			
			mAbsoluteLayout.addOnLayoutChangeListener(mOnLayoutChangeListener);
			
			int id = ResourcesId.getResourcesId(mActivity, "layout", "x_popview");
			mPopView = (PopView) LayoutInflater.from(mActivity).inflate(id, null);
			mAbsoluteLayout.addView(mPopView);
			
			setPopViewPosition(mAbsoluteLayout.getWidth(), mAbsoluteLayout.getHeight());
			
			mPopView.init(word, explain);
			mPopView.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					mPopView = null;
					mAbsoluteLayout.setVisibility(View.GONE);
					mAbsoluteLayout.setOnClickListener(null);
					mAbsoluteLayout.removeOnLayoutChangeListener(mOnLayoutChangeListener);
					bIsShowing = false;
				}
			});
		}
	}
	
	View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
		@Override
		public void onLayoutChange(View v, 
				int left, int top, int right, int bottom, 
				int oldLeft, int oldTop, int oldRight, int oldBottom) {
//			String z = String.format("[(%d,%d),(%d,%d)]->[(%d,%d),(%d,%d)]", 
//					oldLeft, oldTop, oldRight, oldBottom,
//					left, top, right, bottom);
//			Log.i(TAG, z);
			if (left != oldLeft || top != oldTop || 
					right != oldRight || bottom != oldBottom) {
				Message msg = Message.obtain();
				msg.what = 0;
				msg.arg1 = right - left;
				msg.arg2 = bottom - top;
				mHandler.sendMessage(msg);
			}
		}
	};
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				setPopViewPosition(msg.arg1, msg.arg2);
			}
		};
	};
	
	private void setPopViewPosition(int parentW, int parentH) {
		if (mPopView != null) {
			AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams)mPopView.getLayoutParams();
			lp = calculateParams(lp, parentW, parentH);
			if ((mPopView.getWidth() != lp.width)
					|| (mPopView.getHeight() != lp.height)) {
				mPopView.setLayoutParams(lp);
			}
		}
	}

	private int wMax = 600, wMin = 480;
	private int hMax = 600, hMin = 480;
	private AbsoluteLayout.LayoutParams calculateParams(AbsoluteLayout.LayoutParams lp, int parentW, int parentH) {
		int w = parentW;
		int h = parentH;
		
		if (w > wMax) {
			w = wMax;
		} else if (w > wMin) {
			w = wMin;
		}

		if (h > hMax) {
			h = hMax;
		} else if (h > hMin) {
			h = hMin;
		}

		lp.width = w;
		lp.height = h;
		lp.x = (parentW - lp.width)/2;
		lp.y = (parentH - lp.height)/2;
//		Log.i(TAG, "parentW:" + parentW + ", parentH:" +parentH);
		return lp;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean ret = false;
		if (mPopView != null) {
			ret = mPopView.onKeyDown(keyCode, event);
		}
		return ret;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean ret = false;
		if (mPopView != null) {
			ret = mPopView.onKeyUp(keyCode, event);
		}
		return ret;
	}
	
	public void onResume() {
		if (mPopView != null) {
			mPopView.onResume();
		}
	}
	
	public void onPause() {
		if (mPopView != null) {
			mPopView.onPause();
		}
	}
}