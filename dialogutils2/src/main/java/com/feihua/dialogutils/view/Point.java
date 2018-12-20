package com.feihua.dialogutils.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Point extends View
{
	public Point(Context context, AttributeSet ast){
		super(context,ast);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		
		Paint paint=new Paint();
		paint.setColor(Color.parseColor("#008cf9"));//getContext().getResources().getString(R.color.colorMain)));       //设置画笔颜色
		paint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
		paint.setStrokeWidth(20f); 
		paint.setAntiAlias(true); 
		
		canvas.drawCircle(getWidth()/2,getHeight()/2,15,paint);
		//canvas.drawPoint(0,0,paint);
	}
	
	
	
}
