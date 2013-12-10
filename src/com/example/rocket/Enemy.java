package com.example.rocket;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Enemy {

	protected int enemyX;
	protected int enemyY;
	private int health;
	private int width;
	private int height;
	
	protected Paint paint;
	// public BitmapImage image;

	public Enemy(int x, int y, int w, int theHeight, int theHealth)
	{
		enemyX = x;
		enemyY = y;
		health = theHealth;
		width = w;
		height = theHeight;
		
		paint = new Paint();
	}
	
	public void draw(Canvas canvas)
	{
		paint.setColor(Color.GREEN);
		canvas.drawRect(enemyX - width/2, enemyY, enemyX + width/2 , enemyY + height, paint);
	}
	
	public int getX()
	{
		return enemyX;
	}
	
	public int getY()
	{
		return enemyY;
	}
	
	public void setX(int x)
	{
		enemyX = x;
	}
	
	public void setY(int y)
	{
		enemyY = y;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHealth(int h)
	{
		health = h;
	}
}
