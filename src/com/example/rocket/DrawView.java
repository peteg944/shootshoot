package com.example.rocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class DrawView extends View {
	
	Paint paint = new Paint();
	public Spaceship ship;

	public DrawView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawRect(30, 30, 180, 180, paint);
        paint.setColor(Color.CYAN);
        canvas.drawRect(ship.x, ship.y, ship.x + ship.sizeX, ship.y + ship.sizeY, paint);
        canvas.drawRect(100,300,200,400,paint);
        
//        paint.setStrokeWidth(0);
//        paint.setColor(Color.CYAN);
//        canvas.drawRect(33, 60, 77, 77, paint);
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect(33, 33, 77, 60, paint);
        
	}

}
