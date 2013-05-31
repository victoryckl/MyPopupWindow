package com.example.pop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.mypopupwindow.R;

public class MyPop extends PopupWindow {

	private static final String TAG = "MyPop";
	private Context mContext;
	
	public MyPop() {
		// TODO Auto-generated constructor stub
	}

	public MyPop(Context context) {
		super(context);
		initPopWindow(context);
	}

	public MyPop(View contentView) {
		super(contentView);
		// TODO Auto-generated constructor stub
	}

	public MyPop(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyPop(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public MyPop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyPop(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
	}

	public MyPop(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}

	public MyPop(View contentView, int width, int height, boolean focusable) {
		super(contentView, width, height, focusable);
		// TODO Auto-generated constructor stub
	}

	void initPopWindow(Context context) {
		Log.i(TAG, "initPopWindow");
		if (context != null) {
			mContext = context.getApplicationContext();
			// 加载popupWindow的布局文件
			View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop, null);
			// 设置popupWindow的背景颜色
//			contentView.setBackgroundColor(Color.RED);
			
			setWidth(400);
			setHeight(300);
			// 为弹出框设定自定义的布局
			setContentView(contentView);
			
			//设置一个空图片，否则背景是灰色的
			setBackgroundDrawable(new BitmapDrawable());

			final EditText editText = (EditText) contentView.findViewById(R.id.editText1);
			// 设定当你点击editText时，弹出的输入框是啥样子的。这里设置默认为数字输入哦，这时候你会发现你输入非数字的东西是不行的哦
			editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			/*
			 * 这个setFocusable(true);非常重要，如果不在弹出之前加上这条语句，你会很悲剧的发现，你是无法在
			 * editText中输入任何东西的。该方法可以设定popupWindow获取焦点的能力。当设置为true时，系统会捕获到焦点给popupWindow
			 * 上的组件。默认为false哦.该方法一定要在弹出对话框之前进行调用。
			 */
			setFocusable(true);
		}
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		
		super.showAtLocation(parent, gravity, x, y);
	}
}
