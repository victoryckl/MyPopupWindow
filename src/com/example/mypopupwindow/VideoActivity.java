package com.example.mypopupwindow;

import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class VideoActivity extends Activity {
	private static final int FILE_SELECT_CODE = 0;
	private static final String TAG = "VideoActivity";
	private EditText mEditFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_activity);
		
		init();
	}
	private void init() {
		findViewById(R.id.x_btn_call_system).setOnClickListener(mBtnClickListener);
		findViewById(R.id.x_btn_videoview).setOnClickListener(mBtnClickListener);
		findViewById(R.id.x_btn_mediaplayer).setOnClickListener(mBtnClickListener);
		findViewById(R.id.x_btn_choose_file).setOnClickListener(mBtnClickListener);
		
		mEditFilePath = (EditText)findViewById(R.id.x_edit_path);
	}
	
	private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.x_btn_call_system:
				callSystemVideoPlayer();
				break;
			case R.id.x_btn_videoview:
				break;
			case R.id.x_btn_mediaplayer:
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
		Uri uri = Uri.parse(mEditFilePath.getText().toString());
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "video/mp4");
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
			Toast.makeText(this, R.string.x_str_no_file_browser, Toast.LENGTH_SHORT).show();
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
	
}
