package com.example.rocket;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class ShowInstructions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_instructions);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	// Also go to the MainActivity if the software back button is pressed (not controlled by our app)
	@Override
	public void onBackPressed() {
	    Intent theIntent = new Intent(this, MainActivity.class);
	    startActivity(theIntent);
	}
	
	// go to MainActivity if the "Main Menu" button is pressed
	public void goToMain(View v)
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			
			// move back to MainActivity if the navigation back button is pressed
			Intent intent2 = new Intent(this, MainActivity.class);
			startActivity(intent2);
		}
		return true;
	}
}
