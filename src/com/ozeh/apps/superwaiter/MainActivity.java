package com.ozeh.apps.superwaiter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ozeh.apps.superwaiter.contracts.AttendanceContract;
import com.ozeh.apps.superwaiter.contracts.ParamsContract;
import com.ozeh.apps.superwaiter.extras.Params;
import com.ozeh.apps.superwaiter.extras.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

	final static public String TAG = MainActivity.class.getCanonicalName();
	private TextView tvClockin;
	private TextView tvClockout;
	private EditText etCashTips;
	private EditText etCreditTips;
	private Button btnClockin;
	private Button btnClockout;
	private DatePicker dPicker;

	private int hourClockin;
	private int minuteClockin;
	private int am_pmClockin;

	private int hourClockout;
	private int minuteClockout;
	private int am_pmClockout;

	static final int CLOCKIN_DIALOG_ID = 998;
	static final int CLOCKOUT_DIALOG_ID = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();

		setCurrentTimeOnView();
		addListenerOnClockin();
		addListenerOnClockout();

	}

	private void initialize() {
		etCashTips = (EditText) findViewById(R.id.tips_cash_edit);
		etCreditTips = (EditText) findViewById(R.id.tips_credit_edit);
		btnClockin = (Button) findViewById(R.id.clockin);
		btnClockout = (Button) findViewById(R.id.clockout);
		tvClockin = (TextView) findViewById(R.id.clockin_text);
		tvClockout = (TextView) findViewById(R.id.clockout_text);
		dPicker = (DatePicker) findViewById(R.id.dpResult);
		
	

	}

	private void addListenerOnClockin() {

		btnClockin.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				showDialog(CLOCKIN_DIALOG_ID);

			}

		});
	}

	private void addListenerOnClockout() {

		btnClockout.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				showDialog(CLOCKOUT_DIALOG_ID);

			}

		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CLOCKIN_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hourClockin,
					minuteClockin, false);
		case CLOCKOUT_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener2,
					hourClockout, minuteClockout, false);

		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hourClockin = selectedHour;
			minuteClockin = selectedMinute;

			int hour2 = Tools.formatHour(selectedHour);

			String am_pm = "AM";
			if (selectedHour >= 12) {
				am_pm = "PM";
			}

			// set current time into textview
			tvClockin.setText(new StringBuilder().append(Tools.pad(hour2))
					.append(":").append(Tools.pad(minuteClockin))
					.append(" " + am_pm));

		}

	};

	private TimePickerDialog.OnTimeSetListener timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hourClockout = selectedHour;
			minuteClockout = selectedMinute;

			int hour2 = Tools.formatHour(selectedHour);

			String am_pm = "AM";
			if (selectedHour >= 12) {
				am_pm = "PM";
			}

			// set current time into textview
			tvClockout.setText(new StringBuilder().append(Tools.pad(hour2))
					.append(":").append(Tools.pad(minuteClockout))
					.append(" " + am_pm));

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.submit:
			submit();
			return true;
		case R.id.report:
			Intent intent = new Intent(this, DataSheet.class);
			startActivity(intent);
			return true;
		case R.id.menu_settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// display current time
	public void setCurrentTimeOnView() {

		try {

			final Calendar c = Calendar.getInstance();
			hourClockin = c.get(Calendar.HOUR_OF_DAY);
			
			hourClockin = Tools.formatHour(hourClockin);
			
			minuteClockin = c.get(Calendar.MINUTE);
			am_pmClockin = c.get(Calendar.AM_PM);
			String AM_PM = "PM";
			if (am_pmClockin == 0) {
				AM_PM = "AM";
			}

			tvClockin
					.setText(new StringBuilder().append(Tools.pad(hourClockin))
							.append(":").append(Tools.pad(minuteClockin))
							.append(" ").append(AM_PM));

			tvClockout
					.setText(new StringBuilder().append(Tools.pad(hourClockin))
							.append(":").append(Tools.pad(minuteClockin))
							.append(" ").append(AM_PM));

		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}

	}

	private void submit() {
		if (isFormComplete()) {
			try {
				insertToDatabase();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Intent intent = new Intent(this, DataSheet.class);
			startActivity(intent);
		} else {
			Tools.showToast("Please fill the missing fields",
					this.getApplicationContext());
		}

	}

	private boolean isFormComplete() {
		if (!etCashTips.getText().toString().equals("")
				&& !etCreditTips.getText().toString().equals("")) {
			return true;
		} else {
			return false;
		}

	}

	private void insertToDatabase() throws ParseException {
		int day = dPicker.getDayOfMonth();
		int month = dPicker.getMonth() + 1;
		int year = dPicker.getYear();
		String shift_date = day + "-" + month + "-" + year;

		String clockin = Tools.pad(hourClockin) + ":"
				+ Tools.pad(minuteClockin);
		SimpleDateFormat formatClockin = new SimpleDateFormat("HH:mm");
		Date d1 = formatClockin.parse(clockin);

		String clockout = Tools.pad(hourClockout) + ":"
				+ Tools.pad(minuteClockout);
		SimpleDateFormat formatClockout = new SimpleDateFormat("HH:mm");
		Date d2 = formatClockout.parse(clockout);

		long diff = d2.getTime() - d1.getTime();
		long diffHours = diff / (60 * 60 * 1000) % 24;

		String cashTips = etCashTips.getText().toString();
		String creditTips = etCreditTips.getText().toString();

		Double paymentRate = Double.parseDouble(Tools.getParam(
				Params.PARAM_PAYMENT_RATE, getApplicationContext()));
		ContentValues values = new ContentValues();

		values.put(AttendanceContract.CLOCKIN, tvClockin.getText().toString());
		values.put(AttendanceContract.CLOCKOUT, tvClockout.getText().toString());
		values.put(AttendanceContract.SHIFT_DATE, shift_date);
		values.put(AttendanceContract.TOTAL_HOURS, diffHours);
		values.put(AttendanceContract.PAYOFF, diffHours * paymentRate);
		values.put(AttendanceContract.TIPS_CASH, cashTips);
		values.put(AttendanceContract.TIPS_CREDIT, creditTips);
		values.put(AttendanceContract.PROFIT, (diffHours * paymentRate)
				+ Double.parseDouble(cashTips) + Double.parseDouble(creditTips));

		if (isRecordExists(shift_date)) {
			getContentResolver().update(AttendanceContract.CONTENT_URI, values, AttendanceContract.SHIFT_DATE + "=?", new String[] { shift_date });
			Tools.showToast("An existing record ("+shift_date+") was updated successfully",
					getApplicationContext());
		}
		else
		{
			getContentResolver().insert(AttendanceContract.CONTENT_URI, values);
			Tools.showToast("New record added to the database successfully",
					getApplicationContext());
		}
		
		
	}

	private boolean isRecordExists(String shift_date) {
		String[] projection = new String[] { AttendanceContract.ID,
				AttendanceContract.SHIFT_DATE };
		Cursor c = getContentResolver().query(AttendanceContract.CONTENT_URI,
				projection, AttendanceContract.SHIFT_DATE + "=?",
				new String[] { shift_date },
				AttendanceContract.SHIFT_DATE + " ASC");
		c.moveToFirst();
		if (c.getCount() > 0) {
			return true;
		} else {
			return false;
		}

	}

}
