package com.example.rocket;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

public class Bullet {
	
	// location of the bullet
	private int bulletX;
	private int bulletY;
	
	// constants that define the bullet's width and height
	public static int BULLET_WIDTH = 10;
	public static int BULLET_HEIGHT = 40;
	
	// used for setting the color of the bullet
	private Paint paint;

	// gives the newly created bullet an x and y value
	public Bullet(int x, int y)
	{
		bulletX = x;
		bulletY = y;
		
		paint = new Paint();
	}
	
	// draws the bullet to the canvas passed from RocketView
	public void draw(Canvas canvas)
	{
		paint.setColor(Color.RED);
		canvas.drawRect(bulletX  - (Bullet.BULLET_WIDTH)/2, bulletY, bulletX + BULLET_WIDTH  - (Bullet.BULLET_WIDTH)/2, bulletY + BULLET_HEIGHT, paint);
	}
	
	////
	//// Getters and setters below:
	////
	
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
