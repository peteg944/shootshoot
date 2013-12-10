package com.example.rocket;

import java.io.IOException;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
	
	MediaPlayer player; // used to play the introduction audio
	IntroSound intro; // an instance of AsyncTask that runs the audio in the background
	RadioGroup rGroup; // group of radio buttons that determine difficulty
	int speed = 1; // variable passed to GameActivity to set the starting speed of the game
	boolean densmore = false; // are we in densmore/mystery mode?

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// get the radiogroup from the generated XML file
		rGroup = (RadioGroup)findViewById(R.id.radioGroup1);
		
		// initialize the sound playing/controlling variables 
		player = new MediaPlayer();
		intro = new IntroSound();
		AssetFileDescriptor afd;
		
		// open the .m4a file from the assets folder
		try {
			afd = getAssets().openFd("Intro.m4a");
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			player.prepare();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// called whenever a radio button in the difficulty selector
	// is pressed
	public void onRadioButtonClicked(View view)
	{
		// the "view" passed into this method is actually
		// a RadioButton instance, so we cast it
		boolean checked = ((RadioButton)view).isChecked();
		if(checked)
		{
			switch(view.getId())
			{
				case R.id.radio0:
					speed = 1;
					densmore = false;
					break;
					
				case R.id.radio1:
					speed = 3;
					densmore = false;
					break;
					
				case R.id.radio2:
					speed = 6;
					densmore = false;
					break;
					
				case R.id.radio3:
					speed = 1;
					densmore = true;
					break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	// opens up the instructions activity
	public void showInstructions(View view){
		Intent intent = new Intent(this, ShowInstructions.class);
		startActivity(intent);
	}
	
	// opens up the phone's browser to go to the credits html page on people.bu.edu
	public void showCredits(View view){
		//Intent intent = new Intent(this, CreditsActivity.class);
		//startActivity(intent);
		
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://people.bu.edu/goulakos/credits.html"));
		startActivity(browserIntent);
	}
	
	// start the music, if it is not playing
	public void playIntro(View v)
	{
		if(!player.isPlaying())
			intro.doInBackground();
	}
	
	// handles the event when the user switches out of the app,
	// so we pause the audio
	@Override
	protected void onPause()
	{
		super.onPause();
		
		if(player.isPlaying())
			player.pause();
	}
	
	// starts the game by moving to GameActivity, and also
	// pass a few parameters that determine how the game works
	// like mystery/densmore mode and starting speed of the game
	public void startGame(View v)
	{
		if(player.isPlaying())
			player.pause();
		
		Intent intent = new Intent(this, GameActivity.class);
		//int difficulty = rGroup.getCheckedRadioButtonId();
		//int difficulty = 2;
		/*if(difficulty == 0 || difficulty == 3)
			intent.putExtra("Speed", 1);
		else if(difficulty == 1)
			intent.putExtra("Speed", 3);
		else if(difficulty == 2)
			intent.putExtra("Speed", 6);*/
		
		intent.putExtra("Speed", speed);
		intent.putExtra("Densmore", densmore);
		
		startActivity(intent);
	}
	
	// responsible for playing the sound, but in the background,
	// so the user can navigate away and start the game when they want to
	// and not have to wait for the sound to stop
	public class IntroSound extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... arg0) {
			player.start();
			return null;
		}
		
	}

}
