package com.ozeh.apps.superwaiter.contracts;

import com.ozeh.apps.superwaiter.providers.DataProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class AttendanceContract {

	public final static String ID =  "_id";
	public final static String CLOCKIN = "clockin";
	public final static String CLOCKOUT = "clockout";
	public final static String SHIFT_DATE = "shift_date";
	public final static String TOTAL_HOURS = "total_hours";
	public final static String PAYOFF = "payoff";
	public final static String TIPS_CASH = "tips_cash";
	public final static String TIPS_CREDIT = "tips_credit";
	public final static String PROFIT = "profit";
	
	public static String AUTHORITY = "com.ozeh.apps.superwaiter";
	public static String PATH = "/"+DataProvider.ATTENDANCE_TABLE;  
	public static String URL = "content://"+AUTHORITY+PATH;
	public static Uri CONTENT_URI = Uri.parse(URL);
	

	public static String getClockin(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(CLOCKIN);
		return cursor.getString(colIndex);
	}

	public static void putClockin(ContentValues values, String clockin) {
		values.put(CLOCKIN, clockin);
	}
	public static String getClockout(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(CLOCKOUT);
		return cursor.getString(colIndex);
	}

	public static void putClockout(ContentValues values, String clockout) {
		values.put(CLOCKOUT, clockout);
	}
	public static String getShiftDate(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(SHIFT_DATE);
		return cursor.getString(colIndex);
	}

	public static void putShiftDate(ContentValues values, String shiftDate) {
		values.put(SHIFT_DATE, shiftDate);
	}
	public static String getTotalHours(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(TOTAL_HOURS);
		return cursor.getString(colIndex);
	}

	public static void putTotalHours(ContentValues values, String totalHours) {
		values.put(TOTAL_HOURS, totalHours);
	}
	public static String getPayoff(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(PAYOFF);
		return cursor.getString(colIndex);
	}

	public static void putPayoff(ContentValues values, String payoff) {
		values.put(PAYOFF, payoff);
	}
	public static String getTipsCash(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(TIPS_CASH);
		return cursor.getString(colIndex);
	}

	public static void putTipsCash(ContentValues values, String tips) {
		values.put(TIPS_CASH, tips);
	}
	public static String getTipsCredit(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(TIPS_CREDIT);
		return cursor.getString(colIndex);
	}

	public static void putTipsCredit(ContentValues values, String tips) {
		values.put(TIPS_CREDIT, tips);
	}
	public static String getProfit(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(PROFIT);
		return cursor.getString(colIndex);
	}

	public static void putProfit(ContentValues values, String profit) {
		values.put(PROFIT, profit);
	}
	public static String getId(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(ID);
		return cursor.getString(colIndex);
	}

	public static void putId(ContentValues values, String id) {
		values.put(ID, id);
	}
}

