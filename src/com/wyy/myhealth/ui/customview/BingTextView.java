package com.wyy.myhealth.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class BingTextView extends TextView {

	
	public BingTextView(Context context){
		super(context);
	}
	public BingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.rotate(45, getMeasuredWidth()/2, getMeasuredHeight()/2);
		super.onDraw(canvas);
	}

}
