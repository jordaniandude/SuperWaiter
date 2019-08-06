package com.ozeh.apps.superwaiter.extras;

import com.ozeh.apps.superwaiter.contracts.ParamsContract;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

public class Tools {
	public static void showToast(String message, Context context) {
		Toast.makeText(context, message, Toast.LENGTH_LONG)
				.show();
	}
	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	public static String getParam(String paramPaymentRate,Context context) {
		String[] projection = new String[] { ParamsContract.ID,
				ParamsContract.PARAM_VALUE, ParamsContract.ENABLED};
		
		Cursor c = context.getContentResolver().query(ParamsContract.CONTENT_URI,
				projection, ParamsContract.PARAM_NAME + "=?", new String[] { Params.PARAM_PAYMENT_RATE }, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex(ParamsContract.PARAM_VALUE));
	}
	public static  int formatHour(int selectedHour) {
			
		if(selectedHour > 12)
		{
			return selectedHour - 12;
		}
		if(selectedHour == 0)
		{
			return 12;
		}
		else
		{
			return selectedHour;
		}
		
	}
	public static  int convertTimeTo24(int inHour, String clockInAMPM) {
		if(clockInAMPM.equals("AM"))
		{
			if(inHour == 12)
			{
				return 0;
			}
			else
			{
				return inHour;
			}
		}
		else
		{
			if(inHour == 12)
			{
				return inHour;
			}
			else
			{
				return inHour + 12;
			}
		}
	}
}
