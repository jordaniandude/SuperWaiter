package com.ozeh.apps.superwaiter.providers;


import java.util.HashMap;

import com.ozeh.apps.superwaiter.contracts.AttendanceContract;
import com.ozeh.apps.superwaiter.contracts.ParamsContract;
import com.ozeh.apps.superwaiter.extras.Params;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DataProvider extends ContentProvider {

	public static String DATABASE_NAME = "superwaiter_db5.db";
	public static String ATTENDANCE_TABLE = "Attendance";
	public static String PARAMS_TABLE = "Params";
	public static int DATABASE_VERSION = 1;

	public static String DATABASE_CREATE_ATTENDANCE = "create table " + ATTENDANCE_TABLE
			+ "( " + AttendanceContract.ID + " integer primary key autoincrement, "
			+ AttendanceContract.CLOCKIN + " text not null," 
			+ AttendanceContract.CLOCKOUT + " text not null," 
			+ AttendanceContract.SHIFT_DATE + " text not null," 
			+ AttendanceContract.TOTAL_HOURS + " text,"
			+ AttendanceContract.PAYOFF + " text,"
			+ AttendanceContract.TIPS_CASH + " text,"
			+ AttendanceContract.TIPS_CREDIT + " text,"
			+ AttendanceContract.PROFIT + " text);";
	
	public static String DATABASE_CREATE_PARAMS = "create table " + PARAMS_TABLE
			+ "( " + ParamsContract.ID + " integer primary key autoincrement, "
			+ ParamsContract.PARAM_NAME + " text not null," 
			+ ParamsContract.PARAM_VALUE + " text not null,"
			+ ParamsContract.ENABLED + " text);";

	
	public static String DATABASE_UPGRADE = "DROP TABLE IF EXISTS";
	private SQLiteDatabase db;

	private static final int ALL_ROWS = 1;
	private static final int SINGLE_ROW = 2;

	private static HashMap<String, String> PROJECTION_MAP;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AttendanceContract.AUTHORITY, ATTENDANCE_TABLE, ALL_ROWS);
		uriMatcher.addURI(AttendanceContract.AUTHORITY, ATTENDANCE_TABLE + "/#",
				SINGLE_ROW);
		uriMatcher.addURI(ParamsContract.AUTHORITY, PARAMS_TABLE, ALL_ROWS);
		uriMatcher.addURI(ParamsContract.AUTHORITY, PARAMS_TABLE + "/#",
				SINGLE_ROW);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_ATTENDANCE);
			db.execSQL(DATABASE_CREATE_PARAMS);
			createDefaultParams(db);

		}
		
		private void createDefaultParams(SQLiteDatabase db) {
			ContentValues values = new ContentValues();
			values.put(ParamsContract.PARAM_NAME, Params.PARAM_PAYMENT_RATE);
			values.put(ParamsContract.PARAM_VALUE, Params.PAYMENT_RATE);
			values.put(ParamsContract.ENABLED, true);
			db.insert(PARAMS_TABLE, null, values);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DATABASE_UPGRADE + " " + ATTENDANCE_TABLE);
			db.execSQL(DATABASE_UPGRADE + " " + PARAMS_TABLE);
			
			onCreate(db);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;

		switch (uriMatcher.match(uri)) {
		case ALL_ROWS:
			if (uri == AttendanceContract.CONTENT_URI) {
				count = db.delete(ATTENDANCE_TABLE, selection, selectionArgs);
			}
			
			break;
		case SINGLE_ROW:
			String id = uri.getPathSegments().get(1);
			
			db.delete(
					ATTENDANCE_TABLE,
					AttendanceContract.ID
							+ " = "
							+ id
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {

		case ALL_ROWS:
			if (uri == AttendanceContract.CONTENT_URI) {
				return "vnd.android.cursor/vnd." + AttendanceContract.AUTHORITY + "."
						+ ATTENDANCE_TABLE;
			} 

		case SINGLE_ROW:
			if (uri == AttendanceContract.CONTENT_URI) {
				return "vnd.android.cursor/vnd.edu.stevens.cs522.chatoneway.peers";
			} 
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long rowID = 0;
		if (uri == AttendanceContract.CONTENT_URI) {
			rowID = db.insert(ATTENDANCE_TABLE, "", values);

		}
		else if(uri == ParamsContract.CONTENT_URI)
		{
			rowID = db.insert(PARAMS_TABLE, "", values);
		}
		if (rowID > 0) {
			Uri _uri = null;
			if (uri == AttendanceContract.CONTENT_URI) {
				_uri = ContentUris.withAppendedId(AttendanceContract.CONTENT_URI,
						rowID);
			}
			else if(uri == ParamsContract.CONTENT_URI)
			{
				_uri = ContentUris.withAppendedId(ParamsContract.CONTENT_URI,
						rowID);
			}

			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		/**
		 * Create a write able database which will trigger its creation if it
		 * doesn't already exist.
		 */
		db = dbHelper.getWritableDatabase();
		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		if (uri == AttendanceContract.CONTENT_URI) {
			qb.setTables(ATTENDANCE_TABLE);
		}
		else if (uri == ParamsContract.CONTENT_URI) {
			qb.setTables(PARAMS_TABLE);
		}

		switch (uriMatcher.match(uri)) {
		case ALL_ROWS:
			qb.setProjectionMap(PROJECTION_MAP);
			break;
		case SINGLE_ROW:
			qb.appendWhere(AttendanceContract.ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == "") {
			sortOrder = AttendanceContract.ID;
		}

		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);
		/**
		 * register to watch a content URI for changes
		 */
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;

		switch (uriMatcher.match(uri)) {
		case ALL_ROWS:
			if (uri == AttendanceContract.CONTENT_URI) {
				count = db
						.update(ATTENDANCE_TABLE, values, selection, selectionArgs);
			} 
			else if(uri == ParamsContract.CONTENT_URI)
			{
				count = db
						.update(PARAMS_TABLE, values, selection, selectionArgs);
			}
			break;
		case SINGLE_ROW:
			if (uri == AttendanceContract.CONTENT_URI) {
				count = db.update(ATTENDANCE_TABLE, values,
						AttendanceContract.ID
								+ " = "
								+ uri.getPathSegments().get(1)
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);
			} 
			else if (uri == ParamsContract.CONTENT_URI) {
				count = db.update(PARAMS_TABLE, values,
						ParamsContract.ID
								+ " = "
								+ uri.getPathSegments().get(1)
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);
			} 
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}

