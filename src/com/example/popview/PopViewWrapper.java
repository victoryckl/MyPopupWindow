package com.example.popview;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;

import com.example.popview.PopView.OnDismissListener;
import com.example.resid.ResourcesId;

public class PopViewWrapper {
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
			
			int id = ResourcesId.getResourcesId(mActivity, "layout", "x_popview");
			mPopView = (PopView) LayoutInflater.from(mActivity).inflate(id, null);
			mAbsoluteLayout.addView(mPopView);
			
			mPopView.setLayoutParams(calculateParams((AbsoluteLayout.LayoutParams)mPopView.getLayoutParams()));
			
			mPopView.init(word, explain);
			mPopView.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					mPopView = null;
					mAbsoluteLayout.setVisibility(View.GONE);
					mAbsoluteLayout.setOnClickListener(null);
					bIsShowing = false;
				}
			});
		}
	}

	private int wMax = 600, wMin = 480;
	private int hMax = 600, hMin = 480;
	private AbsoluteLayout.LayoutParams calculateParams(AbsoluteLayout.LayoutParams lp) {
		int w = mAbsoluteLayout.getWidth();
		int h = mAbsoluteLayout.getHeight();
		
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
		lp.x = (mAbsoluteLayout.getWidth() - lp.width)/2;
		lp.y = (mAbsoluteLayout.getHeight() - lp.height)/2;
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
