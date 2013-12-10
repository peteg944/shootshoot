package com.example.rocket;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SmallAsteroid extends Enemy
{
	private Bitmap bm;
	
	public SmallAsteroid(int x, int y, Bitmap image)
	{
		super(x, y, 50, 50, 1);
		bm = image;
	}
	
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(bm, enemyX-25, enemyY, paint);
	}
}


/*package com.example.rocket;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Asteroid {

	private int asteroidX;
	private int asteroidY;
	public static int ASTEROID_WIDTH = 50;
	public static int ASTEROID_HEIGHT = 50;
	
	private Paint paint;

	public Asteroid(int x, int y)
	{
		asteroidX = x;
		asteroidY = y;
		
		paint = new Paint();
	}
	
	public void draw(Canvas canvas)
	{
		paint.setColor(Color.GREEN);
		canvas.drawRect(asteroidX - (Asteroid.ASTEROID_WIDTH/2), asteroidY, asteroidX + ASTEROID_WIDTH - (Asteroid.ASTEROID_WIDTH/2) , asteroidY + ASTEROID_HEIGHT, paint);
	}
	
	public int getX()
	{
		return asteroidX;
	}
	
	public int getY()
	{
		return asteroidY;
	}
	
	public void setX(int x)
	{
		asteroidX = x;
	}
	
	public void setY(int y)
	{
		asteroidY = y;
	}

}*/
