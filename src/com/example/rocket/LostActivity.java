package com.example.rocket;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LostActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost);
		
		Intent theIntent = getIntent();
		int score = theIntent.getIntExtra("score", 0);
		
		TextView scoreText = (TextView)findViewById(R.id.textView1);
		scoreText.setText("Final Score: " + score);
		
	//	Button mymain = (Button)findViewById(R.id.button1);
		
		//mymain.setOnClickListener(new OnClickListener() {
			//	@Override
				//public void onClick(View v) {
					
			//	}
		// });
	}
	
	public void goToMain(View v)
	{
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
