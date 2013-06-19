package com.example.popview;

import android.app.Activity;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

import com.example.mypopupwindow.R;
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
			
			mPopView.init(word, explain, "/sdcard/video.avi");
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
			AbsoluteLayout.LayoutParams orglp;
			if (mPopView.isFullscreen()) {
				lp.x = 0;
				lp.y = 0;
				lp.width = parentW;
				lp.height = parentH;
				
				orglp = new AbsoluteLayout.LayoutParams(lp);
				orglp = calculateParams(orglp, parentW, parentH);
				mPopView.setOrgPosition(orglp.x, orglp.y, orglp.width, orglp.height);
			} else {
				lp = calculateParams(lp, parentW, parentH);
			}
			
			if (mPopView.getX() != lp.x 
					|| mPopView.getY() != lp.y
					|| mPopView.getWidth() != lp.width
					|| mPopView.getHeight() != lp.height) {
//				String f = String.format("new position[(%d,%d),(%d,%d)]", lp.x,
//						lp.y, lp.width, lp.height);
//				Log.i(TAG, f);
				mPopView.setLayoutParams(lp);
			}
		}
	}

	private int wMax = 600, wMin = 480;
	private int hMax = 600, hMin = 480;
	private AbsoluteLayout.LayoutParams calculateParams(AbsoluteLayout.LayoutParams lp, int parentW, int parentH) {
		DisplayMetrics m = getDisplayMetrics();
		float ratio = (float)m.widthPixels / m.heightPixels;
		
		int w,h;

		if (ratio > 1.0) {
			h = parentH;
			if (h * 0.8 > hMax) {
				h = hMax;
//			} else if (h > hMax) {
//				h = hMax;
			} else if (h > hMin) {
				h = hMin;
			}
			w = (int)(h * ratio);
			w = w < parentW ? w : parentW;
			if (w > wMax) {
				w = wMax;
			} else if (w > wMin) {
				w = wMin;
			}
		} else {
			w = parentW;
			if (w > wMax) {
				w = wMax;
			} else if (w > wMin) {
				w = wMin;
			}
			h = (int)(w / ratio);
			h = h < parentH ? h : parentH;
			if (h > hMax) {
				h = hMax;
			} else if (h > hMin) {
				h = hMin;
			}
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
	
	private DisplayMetrics getDisplayMetrics() {
		Display d = mActivity.getWindowManager().getDefaultDisplay();
		DisplayMetrics m = new DisplayMetrics();
		d.getMetrics(m);
		Log.i(TAG, "display -> w:" + m.widthPixels + ", h:" + m.heightPixels);
		return m;
	}
}
