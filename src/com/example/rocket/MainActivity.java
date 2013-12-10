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
	
	MediaPlayer player;
	IntroSound intro;
	RadioGroup rGroup;
	int speed = 1;
	boolean densmore = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		rGroup = (RadioGroup)findViewById(R.id.radioGroup1);
		/*rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup radioGroup1, int arg1) {
				if(arg1 == 0 || arg1 == 3)
					speed = 1;
				else if(arg1 == 1)
					speed = 3;
				else if(arg1 == 2)
					speed = 6;
			}
			
		});*/
		
		player = new MediaPlayer();
		intro = new IntroSound();
		AssetFileDescriptor afd;
		
		try {
			afd = getAssets().openFd("Intro.m4a");
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			player.prepare();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onRadioButtonClicked(View view)
	{
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
	
	public void showInstructions(View view){
		Intent intent = new Intent(this, ShowInstructions.class);
		startActivity(intent);
	}
	
	public void showCredits(View view){
		//Intent intent = new Intent(this, CreditsActivity.class);
		//startActivity(intent);
		
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://people.bu.edu/goulakos/credits.html"));
		startActivity(browserIntent);
	}
	
	public void playIntro(View v)
	{
		if(!player.isPlaying())
			intro.doInBackground();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		if(player.isPlaying())
			player.pause();
	}
	
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
	
	public class IntroSound extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... arg0) {
			player.start();
			return null;
		}
		
	}

}
