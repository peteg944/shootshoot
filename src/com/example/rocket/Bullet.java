package com.example.rocket;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

public class Bullet {
	
	private int bulletX;
	private int bulletY;
	public static int BULLET_WIDTH = 10;
	public static int BULLET_HEIGHT = 40;
	
	private Paint paint;

	public Bullet(int x, int y)
	{
		bulletX = x;
		bulletY = y;
		
		paint = new Paint();
	}
	
	public void draw(Canvas canvas)
	{
		paint.setColor(Color.RED);
		canvas.drawRect(bulletX  - (Bullet.BULLET_WIDTH)/2, bulletY, bulletX + BULLET_WIDTH  - (Bullet.BULLET_WIDTH)/2, bulletY + BULLET_HEIGHT, paint);
	}
	
	public int getX()
	{
		return bulletX;
	}
	
	public int getY()
	{
		return bulletY;
	}
	
	public void setX(int x)
	{
		bulletX = x;
	}
	
	public void setY(int y)
	{
		bulletY = y;
	}
}
