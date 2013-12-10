package com.example.rocket;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SmallAsteroid extends Enemy
{
	// bitmap image to draw on canvas
	private Bitmap bm;
	
	public SmallAsteroid(int x, int y, Bitmap image)
	{
		// calls the Enemy constructor with the values passed in from RocketView
		super(x, y, 50, 50, 1);
		
		// set the image to draw
		bm = image;
	}
	
	public void draw(Canvas canvas)
	{
		// draw the image on the canvas passed from RocketView
		canvas.drawBitmap(bm, enemyX-25, enemyY, paint);
	}
}
