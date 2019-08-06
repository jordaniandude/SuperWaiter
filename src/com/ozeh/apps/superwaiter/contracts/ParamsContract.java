package com.ozeh.apps.superwaiter.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ozeh.apps.superwaiter.providers.DataProvider;

public class ParamsContract {
	public final static String ID =  "_id";
	public final static String PARAM_NAME = "param_name";
	public final static String PARAM_VALUE = "param_value";
	public final static String ENABLED = "enabled";
	
	public static String AUTHORITY = "com.ozeh.apps.superwaiter";
	public static String PATH = "/"+DataProvider.PARAMS_TABLE;  
	public static String URL = "content://"+AUTHORITY+PATH;
	public static Uri CONTENT_URI = Uri.parse(URL);
	

	public static String getParamName(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(PARAM_NAME);
		return cursor.getString(colIndex);
	}

	public static void putParamName(ContentValues values, String paramName) {
		values.put(PARAM_NAME, paramName);
	}
	public static String getParamValue(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(PARAM_VALUE);
		return cursor.getString(colIndex);
	}

	public static void putParamValue(ContentValues values, String paramValue) {
		values.put(PARAM_VALUE, paramValue);
	}
	public static String getEnabled(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(ENABLED);
		return cursor.getString(colIndex);
	}

	public static void putEnabled(ContentValues values, String enabled) {
		values.put(ENABLED, enabled);
	}
	public static String getId(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(ID);
		return cursor.getString(colIndex);
	}

	public static void putId(ContentValues values, String id) {
		values.put(ID, id);
	}
}


