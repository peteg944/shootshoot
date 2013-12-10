package com.example.rocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

public class RocketView extends View implements Runnable {
	
	public Object pauseLock;
	public boolean paused;
	
	private Paint paint;
	private Bullet bullet;
	private SmallAsteroid a1;
	
	public MediaPlayer player;
	public ShootSound shootingSound;
	
	public MediaPlayer backgroundPlayer;
	public BackgroundSound backSound;
	
	private CopyOnWriteArrayList<Enemy> enemies;
	
	private int spaceshipX;
	private int spaceshipY;
	private int spaceshipWidth;
	private int spaceshipHeight;
	
	private int leftPosition;
	private int middlePosition;
	private int rightPosition;
	
	private int width;
	private int height;
	
	private boolean isReady;
	private double time;
	private int enemiesEliminated;
	private int speed;
	private double currscore;
	private int score;
	private boolean densmore;
	private Bitmap shipImage;
	public Thread gameLoop;

	public RocketView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		paint = new Paint();
		bullet = null;
		a1 = null;
		enemies = new CopyOnWriteArrayList<Enemy>();
		isReady = false;
		time = 1.00;
		enemiesEliminated = 0;
		score = 0;
		currscore = 0;
		player = new MediaPlayer();
		backgroundPlayer = new MediaPlayer();
		shootingSound = new ShootSound();
		backSound = new BackgroundSound();
		densmore = false;
		shipImage = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
		pauseLock = new Object();
		paused = false;
		
		try {
			AssetFileDescriptor afd;
			afd = getContext().getAssets().openFd("Shoot.m4a");
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			player.prepare();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			AssetFileDescriptor afd;
			afd = getContext().getAssets().openFd("Background.m4a");
			backgroundPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			backgroundPlayer.prepare();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RocketView, 0, 0);
		try
		{
			spaceshipX = array.getInteger(R.styleable.RocketView_spaceshipX, 0);
			spaceshipY = array.getInteger(R.styleable.RocketView_spaceshipY, 0);
			
			spaceshipWidth = array.getInteger(R.styleable.RocketView_spaceshipWidth, 50);
			spaceshipHeight = array.getInteger(R.styleable.RocketView_spaceshipHeight, 50);
		}
		finally { array.recycle(); }
		
		backSound.doInBackground();
		gameLoop = new Thread(this);
		gameLoop.start();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		paint.setColor(Color.argb(255, 20, 20, 20));
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        canvas.drawRect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight(), paint);
        
        paint.setColor(Color.CYAN);
        //canvas.drawRect(spaceshipX - (spaceshipWidth/2), spaceshipY, spaceshipX + (spaceshipWidth/2), spaceshipY + spaceshipHeight, paint);
        canvas.drawBitmap(shipImage, spaceshipX - (spaceshipWidth/2), spaceshipY, paint);
        
        paint.setColor(Color.argb(210, 200, 10, 10));
        canvas.drawRect(0, height * 7/8, width, height, paint);
        
        if(bullet != null)
        	bullet.draw(canvas);
        
        for(int i = 0; i < enemies.size(); i++ )
        {
        	Object a = enemies.get(i);
        	Enemy e = (Enemy)a;
        	e.draw(canvas);
        }
        
        String scoreText = "Enemies Killed: " + enemiesEliminated;
        paint.setColor(Color.WHITE);
        paint.setTextSize(45);
        canvas.drawText(scoreText, width * 1/4, height / 24 + 20, paint);
        
        paint.setColor(Color.argb(100, 100, 100, 100));
        canvas.drawRect(0, 0, width, height / 12, paint);
        
        //invalidate();
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
	{
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		
		width = xNew;
	    height = yNew;
	     
	    leftPosition = (width / 6);
	    middlePosition = (width / 2);
	    rightPosition = (width * 5 / 6);
	    
	    spaceshipX = middlePosition;
	    spaceshipY = height * 7 / 9;
	     
	    System.out.println("view width is: " + width);
	    System.out.println(leftPosition + " " + middlePosition + " " + rightPosition);
	    
	    //makeAsteroid(spaceshipX, -50);
	    
	  //  Random random = new Random();

		//int track = random.nextInt(3);
		//if(track == 0)
		//{
		//	enemies.add(new SmallAsteroid(leftPosition, -50));
		//}
		//else if(track == 1)
		//	enemies.add(new SmallAsteroid(middlePosition, -50));
		//else if(track == 2)
		//	enemies.add(new SmallAsteroid(rightPosition, -50));
		
		isReady = true;
	    
	    //invalidate();
	}
	
	public void moveShipLeft(View v)
	{
		if(spaceshipX == middlePosition)
			spaceshipX = leftPosition;
		
		else if(spaceshipX == rightPosition)
			spaceshipX = middlePosition;
		
		//invalidate();
		//requestLayout();
	}
	
	public void moveShipRight(View v)
	{
		if(spaceshipX == middlePosition)
			spaceshipX = rightPosition;
		
		else if(spaceshipX == leftPosition)
			spaceshipX = middlePosition;
		
		//invalidate();
		//requestLayout();
	}
	
	public void shoot(View v)
	{
		//System.out.println("shooting!!!!");
		
		if(bullet == null)
		{
			bullet = new Bullet(spaceshipX, spaceshipY - Bullet.BULLET_HEIGHT);
			if(!player.isPlaying())
				shootingSound.doInBackground();
			//System.out.println("making bullet");
		}
		
		//invalidate();
		//requestLayout();
	}
	
	public void makeAsteroid(int x, int y)
	{
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
		SmallAsteroid a1 = new SmallAsteroid(x, y, bm);
		enemies.add(a1);
	}
	
	public void makeDensmore(int x, int y)
	{
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.densmore);
		Densmore d1 = new Densmore(x, y, bm);
		enemies.add(d1);
	}
	
	public void makeAlien(int x, int y)
	{
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.enemyship);
		Alien a1 = new Alien(x, y, bm);
		enemies.add(a1);
	}
	
	public void setSpeed(int setspeed)
	{
		speed = setspeed;
	}
	
	public void setDensmore(boolean d)
	{
		densmore = d;
	}
	
	public class ShootSound extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... arg0) {
			player.start();
			return null;
		}
	}
	
	public class BackgroundSound extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... arg0) {
			backgroundPlayer.start();
			backgroundPlayer.setLooping(true);
			return null;
		}
	}

	@Override
	public void run() {
		boolean hasLost = false;
		
		while (!hasLost) {
			
			if(enemies.size() <= 4 && isReady)
			{
				Random random = new Random();
				int track = random.nextInt(3);
				if(densmore)
				{
					if(track == 0)
						makeDensmore(leftPosition, -50);
					else if(track == 1)
						makeDensmore(middlePosition, -50);
					else if(track == 2)
						makeDensmore(rightPosition, -50);
				}
				else
				{
					int otherRandomNumber = random.nextInt(4);
					if(otherRandomNumber <= 2)
					{
						if(track == 0)
							makeAsteroid(leftPosition, -50);
						else if(track == 1)
							makeAsteroid(middlePosition, -50);
						else if(track == 2)
							makeAsteroid(rightPosition, -50);
					}
					else
					{
						if(track == 0)
							makeAlien(leftPosition, -50);
						else if(track == 1)
							makeAlien(middlePosition, -50);
						else if(track == 2)
							makeAlien(rightPosition, -50);
					}
				}
			}
			
			if(bullet != null)
			{
				bullet.setY(bullet.getY() - 30);
				
				if(bullet.getY() <= 0)
					bullet = null;
			}
			
			for(Enemy e : enemies)
			{
				e.setY(e.getY() + speed);
				
				if(e.getX() == spaceshipX)
				{
					if(e.getY() + e.getWidth() >= spaceshipY)
					{
						enemies.remove(e);
						hasLost = true;
						break;
					}
				}
				else
				{
					if(e.getY() >= spaceshipY + spaceshipHeight)
					{
						enemies.remove(e);
						hasLost = true;
						break;
					}
				}
				
				if(bullet != null)
				{
					if(e.getY() + e.getHeight() >= bullet.getY() && e.getX() == bullet.getX())
					{
						bullet = null;
						if(e.getHealth()<=1)
						{
							enemies.remove(e);
							enemiesEliminated++;
						}
						else 
							e.setHealth(e.getHealth()-1);
					}
				}
			}
			
			postInvalidate();
			
			currscore += (speed*.05);
			
			if (((int)time % 11.00) == 0.00)
			{
				time = 1.00;
				speed+=1;
				System.out.println("speed increasing!!!");
			}
			else 
			time += 0.05;
			//score.setText("Score: " + score);
			
			synchronized(pauseLock)
			{
				while(paused)
				{
					try {
						pauseLock.wait();
					} catch(InterruptedException e) { e.printStackTrace(); }
				}
			}
			
	        try {
	            Thread.sleep(50);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
		
		backgroundPlayer.pause();
		player.pause();
		
		score = (int)currscore +enemiesEliminated*20;
		Intent goToLostScreen = new Intent(this.getContext(), LostActivity.class);
		goToLostScreen.putExtra("score", score);
		getContext().startActivity(goToLostScreen);
	}
}
