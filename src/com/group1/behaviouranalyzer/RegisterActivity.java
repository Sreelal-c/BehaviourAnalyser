package com.group1.behaviouranalyzer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	EditText e1,e2,e3;
	Button b;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		e1=(EditText)findViewById(R.id.editText3);
		e2=(EditText)findViewById(R.id.editText2);
		e3=(EditText)findViewById(R.id.editText1);
		b=(Button)findViewById(R.id.button1);
		
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String nam=e1.getText().toString();
				String em=e2.getText().toString();	
				String pass=e3.getText().toString();
				 DBHelper db=new DBHelper(getApplicationContext(), "myDBMS", null, 2);
		           boolean k=db.insertdata(nam, em, pass);
		           if(k){
		        	   Toast.makeText(getApplicationContext(), "registration successfull", Toast.LENGTH_LONG).show();
		           }
		           else{
		        	   Toast.makeText(getApplicationContext(), "oops....! try again", Toast.LENGTH_LONG).show();
		           }

	}
		});
	}
	
}
