package com.example.rocket;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends Activity {
	
	RocketView rView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_game);
    	
    	rView = (RocketView)findViewById(R.id.rocketView);
        
        Button left = (Button)findViewById(R.id.button1);
        left.bringToFront();
        
        left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rView.moveShipLeft(v);
			}
        });
        
        Button right = (Button)findViewById(R.id.button2);
        right.bringToFront();
        
        right.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		rView.moveShipRight(v);
        	}
        });
        
        Button shoot = (Button)findViewById(R.id.button3);
        shoot.bringToFront();
        
        shoot.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		rView.shoot(v);
        	}
        });
        
        
        rView.setSpeed(getIntent().getIntExtra("Speed", 1));
        rView.setDensmore(getIntent().getBooleanExtra("Densmore", false));
        
        /*int ui = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        		| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;*/
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    @Override
    public void onBackPressed() {
    	
    };
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    	
    	rView.backgroundPlayer.pause();
    	rView.player.pause();
    	
    	synchronized(rView.pauseLock) {
    		rView.paused = true;
    	}
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
    	synchronized(rView.pauseLock) {
    		rView.paused = false;
    		rView.pauseLock.notifyAll();
    	}
    	
    	/*try {
			rView.backgroundPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		rView.backgroundPlayer.start();
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }
}

/*Display display = getWindowManager().getDefaultDisplay();
Point size = new Point();
display.getSize(size);
int width = size.x;
int height = size.y;*/

//Spaceship ship = new Spaceship(350, 900);
//final Button leftbutton = new Button(null, null)

/*dView = new DrawView(this);
dView.setBackgroundColor(Color.BLACK);
dView.ship = ship;*/
//dView.leftbutton = leftbutton;

/*Button leftbutton = (Button)findViewById(R.id.button1);
leftbutton.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//ship.x = ship.x - 20;
		
	}
});*/

//Spaceship ship = new Spaceship(350, 900);
//View aView = (View)findViewById(R.id.view1);
//aView.setBackgroundColor(Color.BLACK);

//View theView = (View)findViewById(R.id.view1);
//theView.setBackgroundColor(Color.BLUE);
