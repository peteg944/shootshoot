package com.example.rocket;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.content.Intent;

public class RocketView extends View implements Runnable {
	
	public Object pauseLock;
	public boolean paused;
	
	private Paint paint;
	private Bullet bullet;
	
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
		
		// intitalizing of the instance variables
		paint = new Paint();
		bullet = null;
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
		pauseLock = new Object();
		paused = false;
		
		/*if(densmore)
			shipImage = BitmapFactory.decodeResource(getResources(), R.drawable.densmore);
		else
			shipImage = BitmapFactory.decodeResource(getResources(), R.drawable.ship);*/

		// load in the shooting and background sounds/music
		// and prepare the MediaPlayers to play
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
		
		// retrieve some values from the GameActivity xml file
		// that pertain to spaceship width and height
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RocketView, 0, 0);
		try
		{
			spaceshipX = array.getInteger(R.styleable.RocketView_spaceshipX, 0);
			spaceshipY = array.getInteger(R.styleable.RocketView_spaceshipY, 0);
			
			spaceshipWidth = array.getInteger(R.styleable.RocketView_spaceshipWidth, 50);
			spaceshipHeight = array.getInteger(R.styleable.RocketView_spaceshipHeight, 50);
		}
		finally
		{
			array.recycle(); // release array from memory
		}
		
		// start up the background music
		backSound.doInBackground();
		
		// start the separate thread that animates all of the objects on the screen
		gameLoop = new Thread(this);
		gameLoop.start();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		// draw a very dark gray background that spans the whole view
		paint.setColor(Color.argb(255, 20, 20, 20));
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        canvas.drawRect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight(), paint);
        
        // draw the spaceship, which depends on whether densmore/mystery mode is on or not
        paint.setColor(Color.CYAN);
        canvas.drawBitmap(shipImage, spaceshipX - (spaceshipWidth/2), spaceshipY, paint);
        
        // draw a red bar at the bottom of the view that provides a background for
        // the left, right, and shoot buttons
        paint.setColor(Color.argb(210, 200, 10, 10));
        canvas.drawRect(0, height * 7/8, width, height, paint);
        
        // if the bullet exists, then draw it
        if(bullet != null)
        	bullet.draw(canvas);
        
        // loop through all the enemies and draw them on the screen
        for(int i = 0; i < enemies.size(); i++ )
        {
        	Object a = enemies.get(i);
        	Enemy e = (Enemy)a;
        	e.draw(canvas);
        }
        
        // draw on the screen the number of enemies eliminated so far in the game
        String scoreText = "Enemies Killed: " + enemiesEliminated;
        paint.setColor(Color.WHITE);
        paint.setTextSize(45);
        canvas.drawText(scoreText, width * 1/4, height / 24 + 20, paint);
        
        // draw a translucent gray bar behind the Enemies Killed counter
        // to make it easier to distinguish
        paint.setColor(Color.argb(100, 100, 100, 100));
        canvas.drawRect(0, 0, width, height / 12, paint);
	}
	
	// our program dynamically adjusts the positions of the enemies
	// and spaceship depending on the width and height of the RocketView,
	// which adjusts itself to the size of the device that it's running on.
	// This function is called when the view actually knows its size,
	// as opposed to onCreate() above where retrieving the width and height
	// of the view actually gives you zero for both.
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
	{
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		
		// width and height of the RocketView
		width = xNew;
	    height = yNew;
	     
	    leftPosition = (width / 6);
	    middlePosition = (width / 2);
	    rightPosition = (width * 5 / 6);
	    
	    spaceshipX = middlePosition;
	    spaceshipY = height * 7 / 9;
	     
	    System.out.println("view width is: " + width);
	    System.out.println(leftPosition + " " + middlePosition + " " + rightPosition);
		
		isReady = true;
	}
	
	// moves the spaceship to the left
	// the "View v" is the UI element that is calling
	// this function--in this case, a button.
	public void moveShipLeft(View v)
	{
		if(spaceshipX == middlePosition)
			spaceshipX = leftPosition;
		
		else if(spaceshipX == rightPosition)
			spaceshipX = middlePosition;
	}
	
	// moves the spaceship to the right
	// the "View v" is the UI element that is calling
	// this function--in this case, a button.
	public void moveShipRight(View v)
	{
		if(spaceshipX == middlePosition)
			spaceshipX = rightPosition;
		
		else if(spaceshipX == leftPosition)
			spaceshipX = middlePosition;
	}
	
	// tells the view to shoot a bullet from the spaceship.
	// the "View v" is the UI element that is calling
	// this function--in this case, a button.
	public void shoot(View v)
	{
		// only if there is not a bullet already on the screen,
		// can we create a new bullet
		if(bullet == null)
		{
			// make new bullet and play the shoot sound
			bullet = new Bullet(spaceshipX, spaceshipY - Bullet.BULLET_HEIGHT);
			if(!player.isPlaying())
				shootingSound.doInBackground();
		}
	}
	
	// instantiate a SmallAsteroid and add it to the ArrayList of enemies
	// with the image of an asteroid
	public void makeAsteroid(int x, int y)
	{
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
		SmallAsteroid a1 = new SmallAsteroid(x, y, bm);
		enemies.add(a1);
	}
	
	// instantiate an Alien, add it to the enemies ArrayList
	// with an image of an enemy ship
	public void makeAlien(int x, int y)
	{
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.enemyship);
		Alien a1 = new Alien(x, y, bm);
		enemies.add(a1);
	}
	
	// set the speed of all the enemies
	public void setSpeed(int setspeed)
	{
		speed = setspeed;
	}
	
	// set the state of densmore mode, which instead of drawing a spaceship image
	// as the spaceship, it instead places a lovely portrit of Prof. Densmore as the spaceship,
	// heroically saving Earth from the alien enemies and asteroids
	public void setDensmore(boolean d)
	{
		densmore = d;
		if(densmore)
			// turns the PNGs and JPEGs we have into bitmap images
			shipImage = BitmapFactory.decodeResource(getResources(), R.drawable.densmore);
		else
			shipImage = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
	}
	
	// a class responsible for playing the shooting/bullet sound in the background
	public class ShootSound extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... arg0) {
			player.start();
			return null;
		}
	}
	
	// a class responsible for playing the background music in the background
	public class BackgroundSound extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... arg0) {
			backgroundPlayer.start();
			backgroundPlayer.setLooping(true);
			return null;
		}
	}

	// this is the over-arching separate thread that contains a while() loop that runs
	// and sleeps the Thread with a 50ms delay between loops, thus animating all of the images
	// on the screen. It also has the ability to be paused and resumed by using a ReadWriteLock
	// and some functions of that class (see below)
	@Override
	public void run() {
		boolean hasLost = false;
		
		while (!hasLost) {
			
			// basically, if the player shoots and eliminates an enemy, automatically
			// generate a new one above the top of the screen, depending on a random
			// number that determines which kind of enemy to spawn
			if(enemies.size() <= 4 && isReady)
			{
				Random random = new Random();
				int track = random.nextInt(3);
				
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
			
			// animate the bullet, if it exists,
			// and remove it from the screen if it has exited the
			// boundary of the screen
			if(bullet != null)
			{
				bullet.setY(bullet.getY() - 30);
				
				if(bullet.getY() <= 0)
					bullet = null;
			}
			
			// now loop through all the enemies and animate them, checking for collisions
			// with the player, bullet, and the bottom of the screen
			for(Enemy e : enemies)
			{
				// move the enemy
				e.setY(e.getY() + speed);
				
				// first, is the enemy in the same column/track as the player?
				// if so, if the enemy has reached the spaceship's y-position,
				// the enemy must've crashed into the player and thus the game should end
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
					// otherwise, if the player lets an enemy go by and reach Earth (oh no!!),
					// the player should lose the game
					if(e.getY() >= spaceshipY + spaceshipHeight)
					{
						enemies.remove(e);
						hasLost = true;
						break;
					}
				}
				
				// now, check for collisions between the bullet and an enemy.
				// remove the enemy and the bullet if they collide.
				if(bullet != null)
				{
					if(e.getY() + e.getHeight() >= bullet.getY() && e.getX() == bullet.getX())
					{
						bullet = null;
						if(e.getHealth()<=1)
						{
							enemies.remove(e);
							enemiesEliminated++; // increment the number of enemies killed
						}
						else 
							e.setHealth(e.getHealth()-1);
					}
				}
			}
			
			// refresh the canvas
			postInvalidate();
			
			// increment the score, depending on the current speed
			currscore += (speed*.05);
			
			// every ten seconds, increase the speed of all the enemies
			// to make the game more fun
			if (((int)time % 11.00) == 0.00)
			{
				time = 1.00;
				speed+=1;
			}
			else 
				time += 0.05; // otherwise, keep counting up in time
			
			// handle the case where the user exits the app while the loop
			// is running. This pauses the while() loop and the whole thread,
			// and keeps it from using the CPU while in the background
			synchronized(pauseLock)
			{
				while(paused)
				{
					try {
						pauseLock.wait();
					} catch(InterruptedException e) { e.printStackTrace(); }
				}
			}
			
			// pause the thread for 50ms 
	        try {
	            Thread.sleep(50);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
		
		///
		///following gets run when the player loses:
		///
		
		// stop the sound effects
		backgroundPlayer.pause();
		player.pause();
		
		// calculate the final score		
		score = (int)currscore +enemiesEliminated*20;
		
		// move to the next screen (LostActivity) and pass it the final score
		Intent goToLostScreen = new Intent(this.getContext(), LostActivity.class);
		goToLostScreen.putExtra("score", score);
		getContext().startActivity(goToLostScreen);
	}
}
