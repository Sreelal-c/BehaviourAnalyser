package com.group1.behaviouranalyzer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Homepage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
	}  
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homepage, menu);
		return true;
	}

}
