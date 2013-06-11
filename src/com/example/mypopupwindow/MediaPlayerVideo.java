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

	private void init() {
		mPath = getIntent().getExtras().getString("path");
		
		mSurfaceView = (SurfaceView)findViewById(R.id.x_video_surface);
		//给SurfaceView添加CallBack监听
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);		
		//为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//下面开始实例化MediaPlayer对象
		mPlayer = new MediaPlayer();
		mPlayer.setOnCompletionListener(this);
		mPlayer.setOnErrorListener(this);
		mPlayer.setOnInfoListener(this);
		mPlayer.setOnPreparedListener(this);
		mPlayer.setOnSeekCompleteListener(this);
		mPlayer.setOnVideoSizeChangedListener(this);
		
		//然后指定需要播放文件的路径，初始化MediaPlayer
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
		//然后，我们取得当前Display对象
		mDisplay = getWindowManager().getDefaultDisplay();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// 当Surface尺寸等参数改变时触发 
		Log.v("Surface Change:::", "surfaceChanged called");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 当SurfaceView中的Surface被创建的时候被调用 
		//在这里我们指定MediaPlayer在当前的Surface中进行播放
		mPlayer.setDisplay(mHolder);
		//在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
		mPlayer.prepareAsync();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v("Surface Destory:::", "surfaceDestroyed called");		
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// 当video大小改变时触发
		//这个方法在设置player的source后至少触发一次
		Log.v("Video Size Change", "onVideoSizeChanged called");
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// seek操作完成时触发
		Log.v("Seek Completion", "onSeekComplete called");
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// 当prepare完成后，该方法触发，在这里我们播放视频     
		//首先取得video的宽和高
		int vWidth = mPlayer.getVideoWidth();
		int vHeight = mPlayer.getVideoHeight();
		//如果video的宽或者高超出了当前屏幕的大小，则要进行缩放  
		if (vWidth > mDisplay.getWidth() || vHeight > mDisplay.getHeight()) {
			float wRatio = (float)vWidth/(float)mDisplay.getWidth();
			float hRatio = (float)vHeight/(float)mDisplay.getHeight();
			//选择大的一个进行缩放
			float ratio = Math.max(wRatio, hRatio);
			vWidth = (int)Math.ceil((float)vWidth/ratio);
			vHeight = (int)Math.ceil((float)vHeight/ratio);
			//设置surfaceView的布局参数
			mSurfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
		}
		mPlayer.start();
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
        // 当一些特定信息出现或者警告时触发     
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
        // 当MediaPlayer播放完成后触发
        Log.v("Play Over:::", "onComletion called");    
        this.finish();
	}
}
