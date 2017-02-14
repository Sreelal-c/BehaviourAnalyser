package com.example.mobihelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class OutgoingSms extends Service {
	String namespace="http://dbc/";
	String method="ins_msg";
	//String url="http://192.168.43.225:8080/mobi_helper/web?wsdl";
	String soapaction="http://dbc/ins_msg";
	
	String thisSms;
	String address;
	String body;

	private final Uri SMS_URI = Uri.parse("content://sms");
    private final String[] COLUMNS = new String[] {"date", "address", "body", "type"};
    private static final String CONDITIONS = "type = 2 AND date > ";
    private static final String ORDER = "date ASC";
    
    private SharedPreferences prefs;
    private long timeLastChecked;
    private Cursor cursor;
    public static long tmpdate=0;
	
    Handler hd=new Handler();
 
	Runnable runn=new Runnable() {
		
		@Override
		public void run() {
			
			
			
			try{
			
		//	Toast.makeText(getApplicationContext(), "sms out", Toast.LENGTH_SHORT).show();
		
			timeLastChecked = prefs.getLong("time_last_checked", -1L);
			ContentResolver cr = getApplicationContext().getContentResolver();
			 
			// get all sent SMS records from the date last checked, in descending order
			cursor = cr.query(SMS_URI, COLUMNS, CONDITIONS + timeLastChecked, null, ORDER);
			
			// if there are any new sent messages after the last time we checked
			if (cursor.moveToNext()) 
			{
			    Set<String> sentSms = new HashSet<String>();
			    timeLastChecked = cursor.getLong(cursor.getColumnIndex("date"));
			    do 
			    {
			        long date = cursor.getLong(cursor.getColumnIndex("date"));
			        
			        if(date!=tmpdate)
			        {			        
			        Log.d("hhhhhhhhhhhhh0000000000000000000", ""+date);
			        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			        
			        

			        // Create a calendar object that will convert the date and time value in milliseconds to date. 
			        Calendar calendar = Calendar.getInstance();
     
			        String date1=formatter.format(new Date());
    
			        String address = cursor.getString(cursor.getColumnIndex("address"));
			        String body = cursor.getString(cursor.getColumnIndex("body"));
			        thisSms = date1 + "," + address + "," + body;
			        
			        Log.d("message",thisSms);
	
			        Toast.makeText(getApplicationContext(), "thisSms", Toast.LENGTH_LONG).show();
			        
			        if (sentSms.contains(thisSms)) {
			            continue; // skip that thing
			        }
			        
			        // else, add it to the set
			        sentSms.add(thisSms);
			        Log.d("Test", "target number: " + address);
			        Log.d("Test", "msg..: " + body);
			        Log.d("Test", "date..: " + date1);
			       // Log.d("Test", "date sent: " + tm);	
			        
			        
			        
			    	
			    	TelephonyManager tel=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
			    	String imei=tel.getDeviceId().toString();
			    	
			    	SharedPreferences sh1=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			    	String ip=sh1.getString("ip","");
			    	String url="http://"+ip+":8080/mobi_helper/web?wsdl";
			        
			        try {
			        	

			        	SoapObject sop=new SoapObject(namespace, method);
			        	sop.addProperty("imei", imei);
			        	sop.addProperty("msg", body);
			        	sop.addProperty("number", address);
			        	sop.addProperty("type", "outgoing");
			        	
			        	SoapSerializationEnvelope senv=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			        	senv.setOutputSoapObject(sop);
			        	
			        	HttpTransportSE htp=new HttpTransportSE(url);
			        	htp.call(soapaction, senv);
			        	
			        	String result=senv.getResponse().toString();
			        	
			        } catch (Exception e) {
			        	// TODO: handle exception
			        }

			        
			        
			        
			        
			        
			        }
			       tmpdate=date;
			        

			        
			    }while(cursor.moveToNext());
			 
			}
			
		
	
		cursor.close();
		Editor editor = prefs.edit();
		editor.putLong("time_last_checked", timeLastChecked);
		editor.commit();
			}catch(Exception e){
				
			}
		
			hd.postDelayed(runn, 5000);
		}
		
		
	
	};
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {

		long  time=java.lang.System.currentTimeMillis();
		prefs=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = prefs.edit();
		editor.putLong("time_last_checked", time);
		editor.commit();
		
		
		try {
			if(android.os.Build.VERSION.SDK_INT>9){
				StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		hd.postDelayed(runn, 5000);
		
		super.onCreate();
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;





	

}

}
