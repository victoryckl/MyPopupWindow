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
			// ����popupWindow�Ĳ����ļ�
			View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop, null);
			// ����popupWindow�ı�����ɫ
//			contentView.setBackgroundColor(Color.RED);
			
			setWidth(400);
			setHeight(300);
			// Ϊ�������趨�Զ���Ĳ���
			setContentView(contentView);
			
			//����һ����ͼƬ�����򱳾��ǻ�ɫ��
			setBackgroundDrawable(new BitmapDrawable());

			final EditText editText = (EditText) contentView.findViewById(R.id.editText1);
			// �趨������editTextʱ���������������ɶ���ӵġ���������Ĭ��Ϊ��������Ŷ����ʱ����ᷢ������������ֵĶ����ǲ��е�Ŷ
			editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			/*
			 * ���setFocusable(true);�ǳ���Ҫ��������ڵ���֮ǰ����������䣬���ܱ���ķ��֣������޷���
			 * editText�������κζ����ġ��÷��������趨popupWindow��ȡ�����������������Ϊtrueʱ��ϵͳ�Ჶ�񵽽����popupWindow
			 * �ϵ������Ĭ��ΪfalseŶ.�÷���һ��Ҫ�ڵ����Ի���֮ǰ���е��á�
			 */
			setFocusable(true);
		}
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		
		super.showAtLocation(parent, gravity, x, y);
	}
}
