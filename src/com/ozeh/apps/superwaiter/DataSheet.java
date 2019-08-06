package com.ozeh.apps.superwaiter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ozeh.apps.superwaiter.contracts.AttendanceContract;
import com.ozeh.apps.superwaiter.dialogs.DeleteAttendanceDialog;
import com.ozeh.apps.superwaiter.dialogs.EditAttendanceDialog;
import com.ozeh.apps.superwaiter.dialogs.NotificationDialog;
import com.ozeh.apps.superwaiter.entities.Attendance;
import com.ozeh.apps.superwaiter.extras.Params;
import com.ozeh.apps.superwaiter.extras.Tools;
import com.ozeh.apps.superwaiter.interfaces.IDialogListener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Build;

public class DataSheet extends Activity implements IDialogListener {

	TableLayout table;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_sheet);
		fillReport();
	}

	private void fillReport() {

		ArrayList<Attendance> attList = getDataFromDatabase();
		table = (TableLayout) findViewById(R.id.table_data);
		Double sum_total_hours = 0.0, sum_payoff = 0.0, sum_tips_cash = 0.0, sum_tips_credit = 0.0, sum_profit = 0.0;

		for (int i = 0; i < attList.size(); i++) {
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT, 50));

			tr.addView(createField(attList.get(i).shift_date));
			tr.addView(createField(attList.get(i).clockin));
			tr.addView(createField(attList.get(i).clockout));
			tr.addView(createField(Double.toString(attList.get(i).total_hours)));
			tr.addView(createField(Double.toString(attList.get(i).payoff)));
			tr.addView(createField(Double.toString(attList.get(i).tips_cash)));
			tr.addView(createField(Double.toString(attList.get(i).tips_credit)));
			tr.addView(createField(Double.toString(attList.get(i).profit)));

			sum_total_hours += attList.get(i).total_hours;
			sum_payoff += attList.get(i).payoff;
			sum_tips_cash += attList.get(i).tips_cash;
			sum_tips_credit += attList.get(i).tips_credit;
			sum_profit += attList.get(i).profit;

			addOnClickListener(tr);
			addOnLongClickListener(tr);

			table.addView(tr, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT, 50));
		}

		TableRow trSeparation = addSeparationRow();
		table.addView(trSeparation, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT, 50));

		TableRow trTotal = addTotalsRow(sum_total_hours, sum_payoff,
				sum_tips_cash, sum_tips_credit, sum_profit);
		table.addView(trTotal, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT, 50));

	}

	private TableRow addTotalsRow(Double sum_total_hours, Double sum_payoff,
			Double sum_tips_cash, Double sum_tips_credit, Double sum_profit) {
		TableRow trTotal = new TableRow(this);
		trTotal.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT, 50));
		trTotal.addView(createField("Total:",
				Color.parseColor(Params.COLOR_REPORT_TOTAL)));
		trTotal.addView(createField(""));
		trTotal.addView(createField(""));
		trTotal.addView(createField(Double.toString(sum_total_hours),
				Color.parseColor(Params.COLOR_REPORT_TOTAL)));
		trTotal.addView(createField(Double.toString(sum_payoff),
				Color.parseColor(Params.COLOR_REPORT_TOTAL)));
		trTotal.addView(createField(Double.toString(sum_tips_cash),
				Color.parseColor(Params.COLOR_REPORT_TOTAL)));
		trTotal.addView(createField(Double.toString(sum_tips_credit),
				Color.parseColor(Params.COLOR_REPORT_TOTAL)));
		trTotal.addView(createField(Double.toString(sum_profit),
				Color.parseColor(Params.COLOR_REPORT_TOTAL)));
		return trTotal;

	}

	private TableRow addSeparationRow() {
		TableRow trSeparation = new TableRow(this);
		trSeparation.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT, 50));
		trSeparation.addView(createField("----"));
		trSeparation.addView(createField(""));
		trSeparation.addView(createField(""));

		trSeparation.addView(createField("----"));
		trSeparation.addView(createField("----"));
		trSeparation.addView(createField("----"));
		trSeparation.addView(createField("----"));
		trSeparation.addView(createField("----"));
		return trSeparation;
	}

	private TextView createField(String value) {
		TextView textView = new TextView(this);
		textView.setText(value);
		textView.setGravity(Gravity.CENTER);
		return textView;
	}

	private TextView createField(String value, int color) {
		TextView textView = new TextView(this);
		textView.setText(value);
		textView.setTextColor(color);
		textView.setTypeface(null, Typeface.BOLD);
		textView.setGravity(Gravity.CENTER);
		return textView;
	}

	private void addOnLongClickListener(TableRow tr) {
		tr.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				TableRow tRow = (TableRow) v;
				tRow.setBackgroundColor(Color.RED);
				Bundle args = new Bundle();
				TextView tvDate = (TextView) tRow.getChildAt(0);
				args.putString(AttendanceContract.SHIFT_DATE, tvDate.getText()
						.toString());

				DialogFragment dialog = new DeleteAttendanceDialog();
				dialog.setArguments(args);
				dialog.show(getFragmentManager(), Params.DIALOG_DELETE_TAG);
				return true;
			}
		});
	}

	private void addOnClickListener(TableRow tr) {
		tr.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TableRow tRow = (TableRow) v;
				TextView tvDate = (TextView) tRow.getChildAt(0);
				TextView tvClockin = (TextView) tRow.getChildAt(1);
				TextView tvClockout = (TextView) tRow.getChildAt(2);
				TextView tvTipsCash = (TextView) tRow.getChildAt(5);
				TextView tvTipsCredit = (TextView) tRow.getChildAt(6);
				tRow.setBackgroundColor(Color.parseColor(Params.COLOR_BLUE));

				DialogFragment dialog = new EditAttendanceDialog();
				Bundle args = new Bundle();
				args.putString(AttendanceContract.SHIFT_DATE, tvDate.getText()
						.toString());
				args.putString(AttendanceContract.CLOCKIN, tvClockin.getText()
						.toString());
				args.putString(AttendanceContract.CLOCKOUT, tvClockout
						.getText().toString());
				args.putString(AttendanceContract.TIPS_CASH, tvTipsCash
						.getText().toString());
				args.putString(AttendanceContract.TIPS_CREDIT, tvTipsCredit
						.getText().toString());
				dialog.setArguments(args);
				dialog.show(getFragmentManager(), Params.DIALOG_EDIT_TAG);
			}
		});

	}

	private ArrayList<Attendance> getDataFromDatabase() {
		String[] projection = new String[] { AttendanceContract.ID,
				AttendanceContract.SHIFT_DATE, AttendanceContract.CLOCKIN,
				AttendanceContract.CLOCKOUT, AttendanceContract.PAYOFF,
				AttendanceContract.TOTAL_HOURS, AttendanceContract.TIPS_CASH,
				AttendanceContract.TIPS_CREDIT, AttendanceContract.PROFIT };
		Cursor c = getContentResolver().query(AttendanceContract.CONTENT_URI,
				projection, null, null, AttendanceContract.SHIFT_DATE + " ASC");
		ArrayList<Attendance> attList = new ArrayList<Attendance>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

			Attendance attendance = new Attendance(
					c.getString(c.getColumnIndex(AttendanceContract.CLOCKIN)),
					c.getString(c.getColumnIndex(AttendanceContract.CLOCKOUT)),
					c.getString(c.getColumnIndex(AttendanceContract.SHIFT_DATE)),
					Double.parseDouble(c.getString(c
							.getColumnIndex(AttendanceContract.TOTAL_HOURS))),
					Double.parseDouble(c.getString(c
							.getColumnIndex(AttendanceContract.PAYOFF))),
					Double.parseDouble(c.getString(c
							.getColumnIndex(AttendanceContract.TIPS_CASH))),
					Double.parseDouble(c.getString(c
							.getColumnIndex(AttendanceContract.TIPS_CREDIT))),
					Double.parseDouble(c.getString(c
							.getColumnIndex(AttendanceContract.PROFIT))));

			attList.add(attendance);
		}
		return attList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data_sheet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.help) {
			DialogFragment dialog = new NotificationDialog();
			dialog.show(getFragmentManager(), Params.DIALOG_NOTIFICATION_TAG);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog,
			Attendance attendance) {
		if (dialog.getTag().equals(Params.DIALOG_EDIT_TAG)) {

			try {
				Date d1 = null;
				Date d2 = null;
				String clockInAMPM,clockoutAMPM;
				String inMinStr,outMinStr;
				try {
					String[] strClockin = attendance.clockin.split(":");
					int inHour = Integer.parseInt(strClockin[0]);
					
					String[] part2 = strClockin[1].split(" ");
					if(part2[0].contains("AM"))
					{
						inMinStr = part2[0].replace("AM", "");
						clockInAMPM = "AM";
					}
					else if(part2[0].contains("PM"))
					{
						inMinStr = part2[0].replace("PM", "");
						clockInAMPM = "PM";
					}
					else
					{
						inMinStr = part2[0];
						clockInAMPM = part2[1].toString();
					}
					
					int formattedHour = Tools.convertTimeTo24(inHour,clockInAMPM);
					int inMin = Integer.parseInt(inMinStr);
					
					String clockin = Tools.pad(formattedHour) + ":" + Tools.pad(inMin);
					SimpleDateFormat formatClockin = new SimpleDateFormat(
							"HH:mm");

					try {
						d1 = formatClockin.parse(clockin);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
					
					String[] strClockout = attendance.clockout.split(":");
					int outHour = Integer.parseInt(strClockout[0]);
					
					String[] part2out = strClockout[1].split(" ");
					if(part2out[0].contains("AM"))
					{
						outMinStr = part2[0].replace("AM", "");
						clockoutAMPM = "AM";
					}
					else if(part2out[0].contains("PM"))
					{
						outMinStr = part2out[0].replace("PM", "");
						clockoutAMPM = "PM";
					}
					else
					{
						outMinStr = part2out[0];
						clockoutAMPM = part2out[1].toString();
					}
					
					int formattedHourOut = Tools.convertTimeTo24(outHour,clockoutAMPM);
					int outMin = Integer.parseInt(outMinStr);
					String clockout = Tools.pad(formattedHourOut) + ":"
							+ Tools.pad(outMin);
					SimpleDateFormat formatClockout = new SimpleDateFormat(
							"HH:mm");

					try {
						d2 = formatClockout.parse(clockout);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					//Tools.showToast("clockin:"+ clockin + " clockout:"+clockout, getApplicationContext());
					
				} catch (Exception ex) {
					throw new Exception(
							"Invalid time, make sure the time format is correct.");
				}

				
				
				long diff = d2.getTime() - d1.getTime();
				long diffHours = diff / (60 * 60 * 1000) % 24;

				ContentValues values = new ContentValues();
				values.put(AttendanceContract.CLOCKIN, attendance.clockin);
				values.put(AttendanceContract.CLOCKOUT, attendance.clockout);
				values.put(AttendanceContract.TOTAL_HOURS, diffHours);
				values.put(AttendanceContract.PAYOFF, diffHours
						* Params.PAYMENT_RATE);
				values.put(AttendanceContract.TIPS_CASH, attendance.tips_cash);
				values.put(AttendanceContract.TIPS_CREDIT,
						attendance.tips_credit);
				values.put(AttendanceContract.PROFIT,
						(diffHours * Params.PAYMENT_RATE)
								+ attendance.tips_cash + attendance.tips_credit);

				getContentResolver().update(AttendanceContract.CONTENT_URI,
						values, AttendanceContract.SHIFT_DATE + "=?",
						new String[] { attendance.shift_date });
						
				dialog.dismiss();
				finish();
				startActivity(getIntent());
				

			} catch (Exception ex) {
				Tools.showToast(ex.getMessage(), dialog.getActivity()
						.getApplicationContext());
			}
		} else {
			getContentResolver().delete(AttendanceContract.CONTENT_URI,
					AttendanceContract.SHIFT_DATE + "=?",
					new String[] { attendance.shift_date });

			dialog.dismiss();
			finish();
			startActivity(getIntent());

		}

	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().cancel();

	}

}
