package com.example.rocket;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LostActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost);
		
		// retrieve the score from the previous screen (GameActivity)
		Intent theIntent = getIntent();
		int score = theIntent.getIntExtra("score", 0);
		
		// set the final score text to the retrieved final score
		TextView scoreText = (TextView)findViewById(R.id.textView1);
		scoreText.setText("Final Score: " + score);
	}
	
	public void goToMain(View v)
	{
		// goes back to MainActivity (main screen)
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lost, menu);
		return true;
	}
	

}
