package com.ozeh.apps.superwaiter.dialogs;

import com.ozeh.apps.superwaiter.R;
import com.ozeh.apps.superwaiter.contracts.AttendanceContract;
import com.ozeh.apps.superwaiter.entities.Attendance;
import com.ozeh.apps.superwaiter.interfaces.IDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditAttendanceDialog extends DialogFragment {

	IDialogListener mListener;

	TextView tvDate;
	EditText etClockin;
	EditText etClockout;
	EditText etTipsCash;
	EditText etTipsCredit;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {

			mListener = (IDialogListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement IDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		String date = getArguments().getString(AttendanceContract.SHIFT_DATE);
		String clockin = getArguments().getString(AttendanceContract.CLOCKIN);
		String clockout = getArguments().getString(AttendanceContract.CLOCKOUT);
		String tipsCash = getArguments()
				.getString(AttendanceContract.TIPS_CASH);
		String tipsCredit = getArguments().getString(
				AttendanceContract.TIPS_CREDIT);

		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View v = factory.inflate(R.layout.dialog_edit, null);

		tvDate = (TextView) v.findViewById(R.id.dialog_date);
		etClockin = (EditText) v.findViewById(R.id.dialog_clockin);
		etClockout = (EditText) v.findViewById(R.id.dialog_clockout);
		etTipsCash = (EditText) v.findViewById(R.id.dialog_tipsCash);
		etTipsCredit = (EditText) v.findViewById(R.id.dialog_tipsCredit);

		tvDate.setText(date);
		etClockin.setText(clockin);
		etClockout.setText(clockout);
		etTipsCash.setText(tipsCash);
		etTipsCredit.setText(tipsCredit);

		int style = DialogFragment.STYLE_NORMAL | DialogFragment.STYLE_NO_FRAME;
		setStyle(style, R.style.ASModaListDialogStyle);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_edit_message)
				.setView(v)
				.setPositiveButton(android.R.string.ok, null)
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener
										.onDialogNegativeClick(EditAttendanceDialog.this);
							}
						});

		return builder.create();

	}

	@Override
	public void onResume() {
		super.onResume();
		AlertDialog alertDialog = (AlertDialog) getDialog();
		// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Attendance att = new Attendance(etClockin.getText().toString(),
						etClockout.getText().toString(), tvDate.getText()
								.toString(), 0, 0, Double
								.parseDouble(etTipsCash.getText().toString()),
						Double.parseDouble(etTipsCredit.getText().toString()),
						0);

				mListener.onDialogPositiveClick(EditAttendanceDialog.this, att);
			}
		});
	}

}
