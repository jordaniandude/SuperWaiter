package com.ozeh.apps.superwaiter;

import com.ozeh.apps.superwaiter.contracts.AttendanceContract;
import com.ozeh.apps.superwaiter.contracts.ParamsContract;
import com.ozeh.apps.superwaiter.extras.Params;
import com.ozeh.apps.superwaiter.extras.Tools;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class SettingsActivity extends Activity {

	EditText etPaymentRate = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		etPaymentRate = (EditText) findViewById(R.id.etPaymentRate);
		String paymentRate = Tools.getParam(Params.PARAM_PAYMENT_RATE, getApplicationContext());
		etPaymentRate.setText(paymentRate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.save) {
			String paymentRate = etPaymentRate.getText().toString();
			ContentValues values = new ContentValues();
			values.put(ParamsContract.PARAM_VALUE, paymentRate);
			getContentResolver().update(ParamsContract.CONTENT_URI, values, ParamsContract.PARAM_NAME + "=?",
					new String[] { Params.PARAM_PAYMENT_RATE });
			
			Tools.showToast("New settings saved.",getApplicationContext());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
