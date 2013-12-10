package com.example.rocket;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class GameActivity extends Activity {
	
	// the instance of RocketView that is responsible for drawing the game
	RocketView rView;
	
	// called when the view comes onto the screen and is instantiated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_game);
    	
    	// get the instance of RocketView
    	rView = (RocketView)findViewById(R.id.rocketView);
        
    	// set up the event listener for the left button
    	// that tells the ship to move left
        Button left = (Button)findViewById(R.id.button1);
        left.bringToFront();
        left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rView.moveShipLeft(v);
			}
        });
        
        // set up the event listener for the right button
    	// that tells the ship to move right
        Button right = (Button)findViewById(R.id.button2);
        right.bringToFront();
        right.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		rView.moveShipRight(v);
        	}
        });
        
        // set up the event listener for the shoot button
    	// that tells the ship to shoot
        Button shoot = (Button)findViewById(R.id.button3);
        shoot.bringToFront();
        shoot.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		rView.shoot(v);
        	}
        });
        
        // set some properties of rView that changes
        // the game dynamics
        rView.setSpeed(getIntent().getIntExtra("Speed", 1));
        rView.setDensmore(getIntent().getBooleanExtra("Densmore", false));
        
        // make this thing fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    @Override
    public void onBackPressed() {
    	// block the back button in GameActivity
    	// the user must lose to exit the game
    };
    
    @Override
    protected void onPause()
    {
    	// if not called, it throws an exception and crashes
    	super.onPause();
    	
    	// stop the sounds from playing
    	rView.backgroundPlayer.pause();
    	rView.player.pause();
    	
    	// pause the game's while() loop in RocketView
    	synchronized(rView.pauseLock) {
    		rView.paused = true;
    	}
    }
    
    @Override
    protected void onResume()
    {
    	// if not called, it throws an exception and crashes
    	super.onResume();
    	
    	// resume the game loop in RocketView
    	synchronized(rView.pauseLock) {
    		rView.paused = false;
    		rView.pauseLock.notifyAll();
    	}
    	
    	// start the music again
    	rView.backgroundPlayer.start();
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }
}
