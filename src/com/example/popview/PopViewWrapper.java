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
				int id = ResourcesId.getResourcesId(mActivity, "id", "absolutelayout");
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
			
			int id = ResourcesId.getResourcesId(mActivity, "layout", "popview");
			mPopView = (PopView) LayoutInflater.from(mActivity).inflate(id, null);
			mAbsoluteLayout.addView(mPopView);
			
			AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams)mPopView.getLayoutParams();
			lp.width = 600;
			lp.height = 462;
			lp.x = (mAbsoluteLayout.getWidth() - lp.width)/2;
			lp.y = (mAbsoluteLayout.getHeight() - lp.height)/2;
			mPopView.setLayoutParams(lp);
			
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
