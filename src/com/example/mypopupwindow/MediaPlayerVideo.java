package com.example.mypopupwindow;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;

public class MediaPlayerVideo extends Activity implements OnCompletionListener,OnErrorListener,
	OnInfoListener,	OnPreparedListener, OnSeekCompleteListener,OnVideoSizeChangedListener,
	SurfaceHolder.Callback {
	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	private MediaPlayer mPlayer;
	private Display mDisplay;
	private String mPath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_mediaplayer_activity);
		init();
	}
	
	@Override
	protected void onDestroy() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
		super.onDestroy();
	}

	private void init() {
		mPath = getIntent().getExtras().getString("path");
		
		mSurfaceView = (SurfaceView)findViewById(R.id.x_video_surface);
		//��SurfaceView���CallBack����
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);		
		//Ϊ�˿��Բ�����Ƶ����ʹ��CameraԤ����������Ҫָ����Buffer����
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//���濪ʼʵ��MediaPlayer����
		mPlayer = new MediaPlayer();
		mPlayer.setOnCompletionListener(this);
		mPlayer.setOnErrorListener(this);
		mPlayer.setOnInfoListener(this);
		mPlayer.setOnPreparedListener(this);
		mPlayer.setOnSeekCompleteListener(this);
		mPlayer.setOnVideoSizeChangedListener(this);
		
		//Ȼ��ָ����Ҫ�����ļ���·������ʼ��MediaPlayer
		try {
			mPlayer.setDataSource(mPath);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Ȼ������ȡ�õ�ǰDisplay����
		mDisplay = getWindowManager().getDefaultDisplay();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// ��Surface�ߴ�Ȳ���ı�ʱ���� 
		Log.v("Surface Change:::", "surfaceChanged called");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// ��SurfaceView�е�Surface��������ʱ�򱻵��� 
		//����������ָ��MediaPlayer�ڵ�ǰ��Surface�н��в���
		mPlayer.setDisplay(mHolder);
		//��ָ����MediaPlayer���ŵ����������ǾͿ���ʹ��prepare����prepareAsync��׼��������
		mPlayer.prepareAsync();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v("Surface Destory:::", "surfaceDestroyed called");		
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// ��video��С�ı�ʱ����
		//�������������player��source�����ٴ���һ��
		Log.v("Video Size Change", "onVideoSizeChanged called");
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// seek�������ʱ����
		Log.v("Seek Completion", "onSeekComplete called");
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// ��prepare��ɺ󣬸÷������������������ǲ�����Ƶ     
		//����ȡ��video�Ŀ�͸�
		int vWidth = mPlayer.getVideoWidth();
		int vHeight = mPlayer.getVideoHeight();
		//���video�Ŀ���߸߳����˵�ǰ��Ļ�Ĵ�С����Ҫ��������  
//		if (vWidth > mDisplay.getWidth() || vHeight > mDisplay.getHeight()) {
			float wRatio = (float)vWidth/(float)mDisplay.getWidth();
			float hRatio = (float)vHeight/(float)mDisplay.getHeight();
			//ѡ����һ����������
			float ratio = Math.max(wRatio, hRatio);
			vWidth = (int)Math.ceil((float)vWidth/ratio);
			vHeight = (int)Math.ceil((float)vHeight/ratio);
			//����surfaceView�Ĳ��ֲ���
			mSurfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
//		}
		mPlayer.start();
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
        // ��һЩ�ض���Ϣ���ֻ��߾���ʱ����     
        switch(what){    
        case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:    
            break;    
        case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:      
            break;    
        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:    
            break;    
        case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:     
            break;    
        }    
        return false;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("Play Error:::", "onError called");
        switch (what) {
        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
            Log.v("Play Error:::", "MEDIA_ERROR_SERVER_DIED");
            break;
        case MediaPlayer.MEDIA_ERROR_UNKNOWN:
            Log.v("Play Error:::", "MEDIA_ERROR_UNKNOWN");    
            break;
        default:
            break;
        }
        return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
        // ��MediaPlayer������ɺ󴥷�
        Log.v("Play Over:::", "onComletion called");    
        this.finish();
	}
}
