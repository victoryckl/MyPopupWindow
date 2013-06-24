package com.example.mypopupwindow;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.toast.StaticToast;

public class VideoActivity extends Activity {
	private static final int FILE_SELECT_CODE = 0;
	private static final String TAG = "VideoActivity";
	private EditText mEditFilePath;
	private VideoView mVideoView;
	private ScrollView mScrollView;
	private LinearLayout mLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_video_activity);
		
		init();
	}
	private void init() {
		findViewById(R.id.x_btn_call_system).setOnClickListener(mBtnClickListener);
		findViewById(R.id.x_btn_videoview).setOnClickListener(mBtnClickListener);
		findViewById(R.id.x_btn_mediaplayer).setOnClickListener(mBtnClickListener);
		findViewById(R.id.x_btn_choose_file).setOnClickListener(mBtnClickListener);
		
		mScrollView = (ScrollView)findViewById(R.id.x_scrollview);
		mScrollView.smoothScrollTo(0, 0);
		
		mEditFilePath = (EditText)findViewById(R.id.x_edit_path);
		mVideoView = (VideoView)findViewById(R.id.x_videoview);
		mVideoView.setMediaController(new MediaController(this));
		
		mLinearLayout = (LinearLayout)findViewById(R.id.x_linearlayout_video);
		mLinearLayout.addOnLayoutChangeListener(mLayoutChangeListener);
	}
	
	private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.x_btn_call_system:
				callSystemVideoPlayer();
				break;
			case R.id.x_btn_videoview:
				callVideoViewPlayer();
				break;
			case R.id.x_btn_mediaplayer:
				callMediaPlayer();
				break;
			case R.id.x_btn_choose_file:
				chooseFile();
				break;
			default:
				break;
			}
		}
	};

	private void callSystemVideoPlayer() {
		String path = getPath();
		if (path == null) {
			return;
		}
		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "video/mp4");
		startActivity(intent);
	}
	
	protected void callVideoViewPlayer() {
		String path = getPath();
		if (path == null) {
			return;
		}
		Uri uri = Uri.parse(path);
		mVideoView.setVideoURI(uri);  
		mVideoView.start();  
		mVideoView.requestFocus();  
	}
	
	protected void callMediaPlayer() {
		String path = getPath();
		if (path == null) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("path", path);
		intent.setClass(this, MediaPlayerVideo.class);
		startActivity(intent);
	}
	
	private void chooseFile() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			String str = (String)getResources().getString(R.string.x_str_select_file);
			startActivityForResult(Intent.createChooser(intent, str), FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			StaticToast.show(this, R.string.x_str_no_file_browser, Toast.LENGTH_SHORT);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != Activity.RESULT_OK) {
			Log.e(TAG, "onActivityResult() error, resultCode: " + resultCode);
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}
		if (requestCode == FILE_SELECT_CODE) {
			Uri uri = data.getData();
			Log.i(TAG, "------->" + uri.getPath());
			mEditFilePath.setText(uri.getPath());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private String getPath() {
		String path = null;

		path = mEditFilePath.getText().toString();
		if (path == null || path.length() <= 0) {
			StaticToast.show(this, R.string.x_str_no_path, Toast.LENGTH_SHORT);
			path = null;
		} else {
			Log.i(TAG, "getPath(): " + path);
		}
		
		return path;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
//		String msg = String.format("linearlayout:(%d,%d-%d,%d)", 
//				(int)mLinearLayout.getX(), (int)mLinearLayout.getY(), mLinearLayout.getWidth(), mLinearLayout.getHeight());
//		Log.i(TAG, msg);
		setViewVideoPosition((int)mLinearLayout.getX(), (int)mLinearLayout.getY(), mLinearLayout.getWidth(), mLinearLayout.getHeight());
	}
	OnLayoutChangeListener mLayoutChangeListener = new OnLayoutChangeListener() {
		@Override
		public void onLayoutChange(View v, int left, int top, int right,
				int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
			if ((left == oldLeft) && (top == oldTop) && (right == oldRight) && (bottom == oldBottom)) {
				return ;
			}
			Handler handler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					if (msg.what == 0) {
//						String s = String.format("linearlayout:(%d,%d-%d,%d)", 
//								(int)mLinearLayout.getX(), (int)mLinearLayout.getY(), mLinearLayout.getWidth(), mLinearLayout.getHeight());
//						Log.i(TAG, "handler-->" + s);
						setViewVideoPosition((int)mLinearLayout.getX(), (int)mLinearLayout.getY(), mLinearLayout.getWidth(), mLinearLayout.getHeight());
					}
				};
			};
			handler.sendEmptyMessage(0);
		}
	};
	
	private void setViewVideoPosition(int left, int top, int w, int h) {
		int orientation = getResources().getConfiguration().orientation;
		LayoutParams lp = (LayoutParams) mVideoView.getLayoutParams();
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			lp.leftMargin = left + w;
			lp.topMargin = top;
		} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			lp.leftMargin = left;
			lp.topMargin = top + h;
		}
		mVideoView.setLayoutParams(lp);
	}
}
