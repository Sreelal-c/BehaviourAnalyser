package com.example.mobihelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.widget.Toast;

public class SMS_RECEIVER extends BroadcastReceiver {

	String namespace="http://dbc/";
	String method="ins_msg";
	String url="http://192.168.43.225:8080/mobi_helper/web?wsdl";
	String soapaction="http://dbc/ins_msg";
		
//	SharedPreferences sh1=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//	String ip=sh1.getString("ip","");
//	String url="http://"+ip+":8080/mobi_helper/web?wsdl";
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
		SharedPreferences sh1=PreferenceManager.getDefaultSharedPreferences(arg0);
		String ip=sh1.getString("ip","");
		 url="http://"+ip+":8080/mobi_helper/web?wsdl";
		Toast.makeText(arg0, "Message Received", Toast.LENGTH_LONG).show();
		Bundle b = arg1.getExtras();
		Object[] obj = (Object[]) b.get("pdus");
		SmsMessage[] sms_list = new SmsMessage[obj.length];
		

		for (int i = 0; i < obj.length; i++) {
			sms_list[i] = SmsMessage.createFromPdu((byte[]) obj[i]);			
		}
		String this_msg = sms_list[0].getMessageBody();
		String this_phone = sms_list[0].getOriginatingAddress();	
	
        SimpleDateFormat Sdat=new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		
		String aa=Sdat.format(new Date());
		
	Toast.makeText(arg0, aa +"\n"+ this_msg +"\n"+ this_phone, Toast.LENGTH_LONG).show();
	
	TelephonyManager tel=(TelephonyManager)arg0.getSystemService(arg0.TELEPHONY_SERVICE);
	String imei=tel.getDeviceId().toString();
	
	
	try {
		if(android.os.Build.VERSION.SDK_INT>9){
			StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	try {
//		if(this_msg.equalsIgnoreCase("DATA_ON"))
//		{
//			
//		}
		
		
		SoapObject sop=new SoapObject(namespace, method);
		sop.addProperty("imei", imei);
		sop.addProperty("msg", this_msg);
		sop.addProperty("number", this_phone);
		sop.addProperty("type", "incoming");
		
		SoapSerializationEnvelope senv=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		senv.setOutputSoapObject(sop);
		
		HttpTransportSE htp=new HttpTransportSE(url);
		htp.call(soapaction, senv);
		
		String result=senv.getResponse().toString();
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	
		

	}

}
