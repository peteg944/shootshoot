package com.example.rocket;

import android.graphics.Bitmap;
import android.graphics.Canvas;

// Alien is derived from Enemy
public class Alien extends Enemy {

	// gets drawn onto the canvas passed in from RocketView
	private Bitmap bm;
	
	public Alien(int x, int y, Bitmap image) {
		super(x, y, 50, 50, 1); // calls the Enemy constructor with the parameters passed in
		bm = image; // set the bitmap image to one that's passed in
	}
	
	public void draw(Canvas canvas) {
		// actually draw the bitmap image to the canvas at the specified location
		// inherited from the Enemy class
		canvas.drawBitmap(bm, enemyX-25, enemyY, paint);
	}
}
