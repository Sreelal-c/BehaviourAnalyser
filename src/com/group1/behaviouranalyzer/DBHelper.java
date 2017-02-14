package com.group1.behaviouranalyzer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

 public DBHelper(Context context, String name, CursorFactory factory,
   int version) {
  super(context, name, factory, version);
  // TODO Auto-generated constructor stub
 }

 @Override
 public void onCreate(SQLiteDatabase db) {
  // TODO Auto-generated method stub
 // db.execSQL("create table if not exists call_history(number varchar, date varchar, time varchar, duration varchar, type varchar)");
 

 
 db.execSQL("create table if not exists reg(rid Integer PRIMARY KEY,name text, email text)");
 db.execSQL("create table if not exists login(lid Integer PRIMARY KEY AUTOINCREMENT, email text, pass text)");
 
 }
 
 @Override
 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  // TODO Auto-generated method stub
  db.execSQL("DROP TABLE IF EXISTS call_history");
     onCreate(db);
 }
 
 public boolean insertdata(String name, String email, String pass)
 {
  SQLiteDatabase sdb=this.getWritableDatabase();
  sdb.execSQL("insert into login(email,pass) values('"+email+"','"+pass+"')");
  Cursor c=sdb.rawQuery("select * from login order by lid desc limit 1", null);
  String id="";
  if(c.getCount()>0)
  {
	  c.moveToFirst();
	 id=c.getString(0); 
  }
 
  
  sdb.execSQL("insert into reg values('"+id+"','"+name+"','"+email+"')");
  return true;
 }
 public Cursor login(String email,String pass)
 {
	 SQLiteDatabase sdb=this.getReadableDatabase();
	  Cursor c=sdb.rawQuery("select * from login where email= '"+email+"'and pass='"+pass+"' ", null);
	  return c;
	 
 }
 public Cursor getData()
 {
  SQLiteDatabase sdb=this.getReadableDatabase();
  Cursor c=sdb.rawQuery("select * from call_history", null);
  return c;
 }
 public void deleteTable()
 {
  SQLiteDatabase db=this.getWritableDatabase();
  db.execSQL("DROP TABLE IF EXISTS call_history");
  onCreate(db);
 }

 // Updating single call
 public int updateCall(String number,String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put("duration", duration);
        return db.update("call_history", values, "number = ?",
                new String[] { number });
 }
 
    // Deleting single call
 public void deleteContact(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("call_history", "number= ?",
                new String[] { number});
        db.close();
 }

}