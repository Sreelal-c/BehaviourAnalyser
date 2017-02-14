package com.group1.behaviouranalyzer;

import android.support.v4.app.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	EditText ed1,ed2;
	Button b,b1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ed1=(EditText)findViewById(R.id.editText1);
		ed2=(EditText)findViewById(R.id.editText2);

		b=(Button)findViewById(R.id.button1);
		b1=(Button)findViewById(R.id.button2);
		
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String un=ed1.getText().toString();
				String pw=ed2.getText().toString();	
				DBHelper db=new DBHelper(getApplicationContext(), "myDBMS", null, 2);
		           Cursor c=db.login(un,pw);
		           String id="";
		           if(c.getCount()>0)
		           {
		         	  c.moveToFirst();
		         	  SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		         	  Editor e=sh.edit();
		         	  e.putString("lid", c.getString(0));
		         	  e.commit();
		         	  
		         	  Intent p=new Intent(getApplicationContext(),OutgoingSms.class);
		         	  startService(p);
		         	 Intent i=new  Intent(getApplicationContext(),Homepage.class);
						startActivity(i);
		         	 
		           }
		           else{
		        	   Toast.makeText(getApplicationContext(), "invalid Email or Password", Toast.LENGTH_LONG).show();Toast.makeText(getApplicationContext(), "invalid Email or Password", Toast.LENGTH_LONG).show();
		           }

				
		          
			}
		});	
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i=new  Intent(getApplicationContext(),RegisterActivity.class);
				startActivity(i);
				
			}
		});
	}



}
