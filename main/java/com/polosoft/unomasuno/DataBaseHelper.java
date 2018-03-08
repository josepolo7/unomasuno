package com.polosoft.unomasuno;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * Created by JAPH on 3/6/18.
 */

public class DataBaseHelper {
	private static final String DATABASE_NAME = "unomasuno_rates.db";
	private static final int DATABASE_VERSION = 100;
	private SQLiteDatabase db;
	//**********************************************************************************************
	public DataBaseHelper(Context context) {
	  OpenHelper openHelper = new OpenHelper(context);
	  db = openHelper.getWritableDatabase();
	}
	//**********************************************************************************************
	public void delete(String table, String where) {
	  db.delete(table, where, null);
	}
	//**********************************************************************************************
	public void deleteAll(String table){
	  db.delete(table, null, null);
	}
	//**********************************************************************************************
	public long insert(String table, ContentValues values){
	   return db.insert(table, null, values);
	}
	//**********************************************************************************************
	public long update(String table, ContentValues values, String where){
	   return db.update(table, values, where, null);
	}
	//**********************************************************************************************
	public long numRows(String table){
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table, null);
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			int ret = cursor.getInt(0);
			cursor.close();
			return ret;
		}
		return 0;
	}
	//**********************************************************************************************
	public String getLastUpdateDate(){
		Cursor cursor = db.rawQuery("SELECT date FROM last_update", null);
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			String dateStr = cursor.getString(0);
			cursor.close();
			return dateStr;
		}
		return null;
	}
	//**********************************************************************************************
	public Cursor rawSelect(String sql, String[] selarg) {
	  Cursor cursor = db.rawQuery(sql, selarg);
	  return cursor;
	}
	//**********************************************************************************************
	private class OpenHelper extends SQLiteOpenHelper {
	  OpenHelper(Context context) {
		 super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }
	//**********************************************************************************************
	  @Override
	  public void onCreate(SQLiteDatabase db){
		  //----------------------------------------------------------------------------------------
		  db.execSQL("CREATE TABLE last_update(date TEXT)");
		  //----------------------------------------------------------------------------------------
		  db.execSQL("CREATE TABLE rate(rate_cod TEXT, rate_number REAL)");
		  //----------------------------------------------------------------------------------------
	  }
		//******************************************************************************************
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		  //TODO
	  }
	}
	//**********************************************************************************************
}
