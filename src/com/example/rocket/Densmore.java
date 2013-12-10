package com.example.rocket;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Densmore extends Enemy {
	
	private Bitmap bm;
	
	public Densmore(int x, int y, Bitmap image) {
		super(x, y, 50, 50, 1);
		bm = image;
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bm, enemyX-25, enemyY, paint);
	}

}
